package com.github.fit51.reactiveconfig.examples

import java.time.Clock

import cats.{Functor, MonadError}
import cats.data.OptionT
import cats.effect.concurrent.MVar
import cats.effect.{Async, Bracket, Concurrent, ContextShift, ExitCase, Sync, Timer}
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.examples.CommandLineShop.CommandLineShopService
import com.github.fit51.reactiveconfig.parser.CirceConfigDecoder.decoder
import com.github.fit51.reactiveconfig.parser.CirceConfigParser.parser
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import io.circe.Json
import io.circe.generic.auto._
import monix.eval.{Task, TaskLike}
import monix.execution.Scheduler

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, _}
import scala.io.StdIn
import scala.util.Try
import scala.util.control.NonFatal

/**
  * Start etcd before running.
  * Run docker command, it will start etcd on 127.0.0.1:2379 without authentication:
  * sudo docker run -e ALLOW_NONE_AUTHENTICATION=yes -p 2379:2379 bitnami/etcd:latest
  * For run with authentication:
  * sudo docker run -e ETCD_ROOT_PASSWORD=test -p 2379:2379 bitnami/etcd:latest
  */
object EtcdConfigApplication extends App {

  import AdvertsModule._
  import StoreModule._

  implicit val scheduler = Scheduler.global
  implicit val clock     = Clock.systemDefaultZone()

  implicit val chManager = ChannelManager.noAuth("http://127.0.0.1:2379")
  //implicit val chManager = ChannelManager("http://127.0.0.1:2379", Credentials("root", "test"))

  def init[F[_]: ContextShift: Async: Concurrent: Timer: TaskLike](
      etcdClient: EtcdClient[F] with Watch[F],
      goods: Map[ProductId, Count]
  )(implicit scheduler: Scheduler): F[CommandLineShopService[F]] = {
    for {
      _      <- FillConfig.fill
      config <- ReactiveConfigEtcd[F, Json](etcdClient)

      storeConfig                              <- config.reloadable[StoreConfig]("store.store")
      implicit0(storeService: StoreService[F]) <- StoreModule.StoreService[F](goods, storeConfig)

      advertsList <- config.reloadable[List[ProductId]]("store.adverts")
      advertsConfig <- storeConfig.combine(advertsList) { (store, list) =>
        AdvertsConfig(store.priceList, list)
      }
      implicit0(advertsService: AdvertsService[F]) = new AdvertsService[F](advertsConfig)
    } yield new CommandLineShopService[F]()
  }

  val future =
    (for {
      client      <- Task.pure(EtcdClient.withWatch[Task](chManager, SimpleDelayPolicy(10 seconds)))
      shopService <- init(client, FillConfig.store)
      _           <- shopService.flow
    } yield ()).runToFuture

  Await.result(future, Duration.Inf)
}

object CommandLineShop {
  import StoreModule._

  object Commands {
    val buyRegexp = """^2\s(\S+)\s([\d]+\.[\d])$""".r
    def showPrice(s: String): Option[Command] =
      if (s.equalsIgnoreCase("1")) Some(ShowPrice) else None

    def exit(s: String): Option[Command] =
      if (s.equalsIgnoreCase("0")) Some(Exit) else None

    def buy(s: String): Option[Buy] =
      Try(
        buyRegexp.findFirstMatchIn(s).map(m => Buy(m.group(1), m.group(2).toDouble))
      ).toOption.flatten

    def fromString(s: String): Option[Command] = {
      showPrice(s) orElse buy(s) orElse exit(s)
    }

    sealed trait Command
    case object ShowPrice                             extends Command
    case object Exit                                  extends Command
    case class Buy(productId: ProductId, cash: Money) extends Command
  }

  class CommandLineShopService[F[_]: MonadError[*[_], Throwable]: Sync: Timer](
      implicit store: StoreModule.StoreService[F],
      adverts: AdvertsModule.AdvertsService[F]
  ) {
    import Commands._
    private val F  = implicitly[Sync[F]]
    private val FT = implicitly[Timer[F]]
    private val FE = implicitly[MonadError[F, Throwable]]

    def printInstruction: F[Unit] =
      F.delay(println("""
          |Input options:
          |1 : show priceList. (Ex: 1)
          |2 [productId] [cash] : buy products for cash and get charge. (Ex: 2 product2 10.0)
          |0 : exit. (Ex: 0)
          |----------------------------------------------------------
          |""".stripMargin))

    def printPriceList(): F[Unit] = {
      val format = "%-40s%-10s%-10s"
      val annotation =
        s"""
          |${String.format(format, "productId", "left", "price")}
          |---------------------------------------------------------""".stripMargin
      for {
        header             <- store.getPriceListVersion.map(v => s"PriceList v_$v")
        goodsWithPriceList <- store.getPriceList
        content <- F.pure(
          goodsWithPriceList.map {
            case (productId, count, price) => String.format(format, productId.toString, count.toString, price.toString)
          }.mkString("\n")
        )
        advertsHeader <- F.pure("----------------Special offers!---------------")
        adverts       <- adverts.getBanners
        _             <- F.delay(println(List(annotation, header, content, advertsHeader, adverts).mkString("\n")))
      } yield ()
    }

    def buy(c: Buy): F[Unit] = {
      FE.handleErrorWith(for {
        change <- store.sell(c.productId, 1, c.cash)
        _      <- F.delay(println(s"Thanks for purchasing ${c.productId}. Here is your change $change"))
      } yield ()) {
        case NonFatal(e) => F.delay(println(s"Error: ${e.getMessage}"))
      }
    }

    def flow: F[_] =
      for {
        _       <- FT.sleep(800 millis)
        _       <- printInstruction
        input   <- F.delay(StdIn.readLine())
        command <- F.delay(Commands.fromString(input))
        _ <- command match {
          case Some(command: Buy) =>
            buy(command) >> flow
          case Some(_ @ShowPrice) =>
            printPriceList() >> flow
          case Some(_ @Exit) =>
            F.delay(println("Good buy!"))
          case None =>
            F.delay(println("Error parsing command... Try again")) >> flow
        }
      } yield ()
  }

}

object StoreModule {
  type ProductId = String
  type Count     = Int
  type Money     = Double

  case class StoreConfig(priceList: Map[ProductId, Money], version: String)

  object StoreService {
    def apply[F[_]: Bracket[*[_], Throwable]: Concurrent](
        goods: Map[ProductId, Count],
        config: Reloadable[F, StoreConfig]
    ): F[StoreService[F]] =
      for {
        goodsMVar <- MVar.of(goods)
      } yield new StoreService(goodsMVar, config)
  }

  class StoreService[F[_]: Bracket[*[_], Throwable]](
      goodsMVar: MVar[F, Map[ProductId, Count]],
      reloadable: Reloadable[F, StoreConfig]
  ) {
    private val F = implicitly[Bracket[F, Throwable]]

    def updateGoods[A](f: Map[ProductId, Count] => F[(Map[ProductId, Count], A)]): F[A] =
      F.bracketCase(goodsMVar.take)(
        goods =>
          f(goods) >>= { (input) =>
            goodsMVar.put(input._1).as(input._2)
          }
      ) {
        case (goods, _ @(ExitCase.Canceled | ExitCase.Error(_))) => goodsMVar.put(goods)
        case _                                                   => F.unit
      }

    def getPriceList: F[Seq[(ProductId, Count, Money)]] =
      for {
        goods  <- goodsMVar.read
        config <- reloadable.get
        withPrices = config.priceList.flatMap {
          case (productId, price) => goods.get(productId).map(count => (productId, count, price))
        }
      } yield withPrices.toSeq

    def getPriceListVersion: F[String] = reloadable.get.map(_.version)

    //Sells product for cash, returns change
    def sell(product: ProductId, count: Count, cash: Money): F[Money] = updateGoods { goods =>
      for {
        productCount <- OptionT
          .fromOption(goods.get(product))
          .getOrElseF(F.raiseError(new Exception("Wrong productId")))
        _      <- F.raiseError(new Exception(s"No more products with id $product left")).whenA(productCount < 1)
        config <- reloadable.get
        price <- OptionT
          .fromOption(config.priceList.get(product))
          .getOrElseF(F.raiseError(new Exception("InternalError, not price found")))
        change       <- F.pure(cash - price)
        _            <- F.raiseError(new Exception(s"Not enough cash, need ${-change} more")).whenA(change < 0)
        updatedGoods <- F.pure(goods + (product -> (productCount - 1)))
      } yield (updatedGoods, change)
    }
  }
}

object AdvertsModule {
  import StoreModule._

  case class AdvertsConfig(priceList: Map[ProductId, Money], adverts: List[ProductId])

  class AdvertsService[F[_]: Functor](reloadable: Reloadable[F, AdvertsConfig]) {
    def getBanners: F[String] = {
      reloadable.get.map { config =>
        config.priceList
          .filterKeys(config.adverts.contains)
          .map {
            case (id, price) => s"\t- Get $id only for $price!"
          }
          .mkString("\n")
      }
    }
  }
}
