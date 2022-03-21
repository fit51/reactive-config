package com.github.fit51.reactiveconfig.etcd;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Executor;
import java.net.InetSocketAddress;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.net.HostAndPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.Status;
import io.grpc.internal.GrpcUtil;
import io.grpc.internal.SharedResourceHolder;

// Stolen from jetcd-core://io.etcd.jetcd.resolver
public class MultipleAddressesResolver extends NameResolver {
    public static final int ETCD_CLIENT_PORT = 2379;

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleAddressesResolver.class);

    private final Object lock;
    private final String authority;
    private final URI targetUri;

    private volatile boolean shutdown;
    private volatile boolean resolving;

    private Executor executor;
    private Listener listener;

    private final List<HostAndPort> addresses;

    public MultipleAddressesResolver(URI targetUri, List<HostAndPort> addresses) {
        this.lock = new Object();
        this.targetUri = targetUri;
        this.authority = targetUri.getAuthority() != null ? targetUri.getAuthority() : "";
        this.addresses =  addresses;
    }

    public URI getTargetUri() {
        return targetUri;
    }

    @Override
    public String getServiceAuthority() {
        return authority;
    }

    @Override
    public void start(Listener listener) {
        synchronized (lock) {
            Preconditions.checkState(this.listener == null, "already started");
            this.executor = SharedResourceHolder.get(GrpcUtil.SHARED_CHANNEL_EXECUTOR);
            this.listener = Preconditions.checkNotNull(listener, "listener");
            resolve();
        }
    }

    @Override
    public final synchronized void refresh() {
        resolve();
    }

    @Override
    public void shutdown() {
        if (shutdown) {
            return;
        }
        shutdown = true;

        synchronized (lock) {
            if (executor != null) {
                executor = SharedResourceHolder.release(GrpcUtil.SHARED_CHANNEL_EXECUTOR, executor);
            }
        }
    }

    private void resolve() {
        if (resolving || shutdown) {
            return;
        }
        synchronized (lock) {
            executor.execute(this::doResolve);
        }
    }

    private void doResolve() {
        Listener savedListener;
        synchronized (lock) {
            if (shutdown) {
                return;
            }
            resolving = true;
            savedListener = listener;
        }

        try {
            List<EquivalentAddressGroup> groups = computeAddressGroups();
            if (groups.isEmpty()) {
                throw new RuntimeException("Unable to resolve endpoint " + targetUri);
            }

            savedListener.onAddresses(groups, Attributes.EMPTY);

        } catch (Exception e) {
            LOGGER.warn("Error wile getting list of servers", e);
            savedListener.onError(Status.NOT_FOUND);
        } finally {
            resolving = false;
        }
    }

    protected List<EquivalentAddressGroup> computeAddressGroups() {
        if (addresses.isEmpty()) {
            throw new RuntimeException("Unable to resolve endpoint " + targetUri);
        }

        return addresses.stream()
            .map(address -> {
                return new EquivalentAddressGroup(
                    new InetSocketAddress(
                        address.getHost(),
                        address.getPortOrDefault(ETCD_CLIENT_PORT)),
                    Strings.isNullOrEmpty(getServiceAuthority())
                        ? Attributes.newBuilder()
                            .set(EquivalentAddressGroup.ATTR_AUTHORITY_OVERRIDE, address.toString())
                            .build()
                        : Attributes.EMPTY);
            })
            .collect(Collectors.toList());
    }
}
