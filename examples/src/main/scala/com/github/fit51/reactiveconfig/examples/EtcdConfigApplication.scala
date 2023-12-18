package com.github.fit51.reactiveconfig.examples

import cats.Functor
import cats.data.OptionT
import cats.effect.{Concurrent, Resource, Sync}
import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Temporal
import cats.effect.kernel.MonadCancel
import cats.effect.kernel.Outcome
import cats.effect.std.Dispatcher
import cats.effect.std.Queue
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.etcd._
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.circe._
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.examples.CommandLineShop.CommandLineShopService
import io.circe.Json
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Try
import scala.util.control.NonFatal

/** Start etcd before running. Run docker command, it will start etcd on 127.0.0.1:2379 without authentication: sudo
  * docker run -e ALLOW_NONE_AUTHENTICATION=yes -p 2379:2379 bitnami/etcd:latest For run with authentication: sudo
  * docker run -e ETCD_ROOT_PASSWORD=test -p 2379:2379 bitnami/etcd:latest
  */
object EtcdConfigApplication extends IOApp {

  import AdvertsModule._
  import StoreModule._

  val chManager = ChannelManager.noAuth("127.0.0.1:2379", options = ChannelOptions(20 seconds))
  // implicit val chManager = ChannelManager("etcd://127.0.0.1:2379", Credentials("root", "test"))

  def init[F[_]: Sync: Concurrent: Temporal](
      etcdClient: EtcdClient[F],
      config: ReactiveConfig[F, Json],
      goods: Map[ProductId, Count]
  ): Resource[F, CommandLineShopService[F]] =
    for {
      _           <- Resource.eval(FillConfig.fill(etcdClient))
      storeConfig <- config.reloadable[StoreConfig]("store.store")
      advertsList <- config.reloadable[List[ProductId]]("store.adverts")
      advertsConfig <- storeConfig.combine(advertsList) { (store, list) =>
        AdvertsConfig(store.priceList, list)
      }

      implicit0(storeService: StoreService[F]) <- Resource.eval(StoreModule.StoreService[F](goods, storeConfig))
      implicit0(advertsService: AdvertsService[F]) = new AdvertsService[F](advertsConfig)(Sync[F])
    } yield new CommandLineShopService[F]()

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      dispatcher <- Dispatcher.parallel[IO]
      channel    <- Resource.make(IO.delay(chManager.channel))(ch => IO.delay(ch.shutdown()))
      etcdClient <- Resource.pure[IO, EtcdClient[IO]](EtcdClient[IO](dispatcher, channel))
      config     <- EtcdReactiveConfig[IO, Json](dispatcher, channel, cats.data.NonEmptySet.one("store"))
      service    <- init(etcdClient, config, FillConfig.store)
    } yield service).use(_.flow).as(ExitCode.Success)
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

    def fromString(s: String): Option[Command] =
      showPrice(s) orElse buy(s) orElse exit(s)

    sealed trait Command
    case object ShowPrice                             extends Command
    case object Exit                                  extends Command
    case class Buy(productId: ProductId, cash: Money) extends Command
  }

  class CommandLineShopService[F[_]](implicit
      store: StoreModule.StoreService[F],
      adverts: AdvertsModule.AdvertsService[F],
      F: Sync[F]
  ) {
    import Commands._
    private val FT: Temporal[F] = ???

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
          goodsWithPriceList.map { case (productId, count, price) =>
            String.format(format, productId.toString, count.toString, price.toString)
          }.mkString("\n")
        )
        advertsHeader <- F.pure("----------------Special offers!---------------")
        adverts       <- adverts.getBanners
        _             <- F.delay(println(List(annotation, header, content, advertsHeader, adverts).mkString("\n")))
      } yield ()
    }

    def buy(c: Buy): F[Unit] =
      F.handleErrorWith(for {
        change <- store.sell(c.productId, 1, c.cash)
        _      <- F.delay(println(s"Thanks for purchasing ${c.productId}. Here is your change $change"))
      } yield ()) { case NonFatal(e) =>
        F.delay(println(s"Error: ${e.getMessage}"))
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
    def apply[F[_]: Concurrent](
        goods: Map[ProductId, Count],
        config: Reloadable[F, StoreConfig]
    ): F[StoreService[F]] =
      for {
        goodsMVar <- Queue.bounded[F, Map[ProductId, Count]](1)
        _         <- goodsMVar.offer(goods)
      } yield new StoreService(goodsMVar, config)
  }

  class StoreService[F[_]: MonadCancel[*[_], Throwable]](
      goodsMVar: Queue[F, Map[ProductId, Count]],
      reloadable: Reloadable[F, StoreConfig]
  ) {
    private val F = implicitly[MonadCancel[F, Throwable]]

    def updateGoods[A](f: Map[ProductId, Count] => F[(Map[ProductId, Count], A)]): F[A] =
      F.bracketCase(goodsMVar.take)(goods =>
        f(goods) >>= { input =>
          goodsMVar.offer(input._1).as(input._2)
        }
      ) {
        case (goods, Outcome.Canceled())   => goodsMVar.offer(goods)
        case (goods, Outcome.Errored(_))   => goodsMVar.offer(goods)
        case (goods, Outcome.Succeeded(_)) => F.unit
      }

    def getPriceList: F[Seq[(ProductId, Count, Money)]] =
      for {
        goods  <- goodsMVar.take
        _      <- goodsMVar.offer(goods)
        config <- reloadable.get
        withPrices = config.priceList.flatMap { case (productId, price) =>
          goods.get(productId).map(count => (productId, count, price))
        }
      } yield withPrices.toSeq

    def getPriceListVersion: F[String] = reloadable.get.map(_.version)

    // Sells product for cash, returns change
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
    def getBanners: F[String] =
      reloadable.get.map { config =>
        config.priceList
          .filterKeys(config.adverts.contains)
          .map { case (id, price) =>
            s"\t- Get $id only for $price!"
          }
          .mkString("\n")
      }
  }
}
