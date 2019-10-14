package com.github.fit51.reactiveconfig.etcd;

import com.coreos.jetcd.common.exception.ErrorCode;
import com.coreos.jetcd.common.exception.EtcdExceptionFactory;
import com.coreos.jetcd.resolver.URIResolver;
import com.google.common.base.Strings;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class DirectUriResolver implements URIResolver {

    private static final List<String> SCHEMES = Arrays.asList("http", "https");

    private ConcurrentMap<URI, List<SocketAddress>> cache;

    DirectUriResolver() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean supports(URI uri) {
        if (!SCHEMES.contains(uri.getScheme())) {
            return false;
        }

        if (!Strings.isNullOrEmpty(uri.getPath())) {
            return false;
        }

        if (uri.getPort() == -1) {
            return false;
        }

        return true;
    }

    @Override
    public List<SocketAddress> resolve(URI uri) {
        if (!supports(uri)) {
            // Wrap as etcd exception but set a proper cause
            throw EtcdExceptionFactory.newEtcdException(
                    ErrorCode.INVALID_ARGUMENT,
                    "Unsupported URI " + uri
            );
        }

        return this.cache.computeIfAbsent(
                uri,
                u -> Collections.singletonList(new InetSocketAddress(uri.getHost(), uri.getPort()))
        );
    }
}
