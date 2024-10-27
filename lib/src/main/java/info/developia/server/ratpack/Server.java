package info.developia.server.ratpack;

import info.developia.server.ratpack.handler.HealthHandler;
import ratpack.core.handling.Chain;
import ratpack.core.handling.Handler;
import ratpack.core.server.RatpackServer;
import ratpack.func.Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private final Map<String, List<Action<Chain>>> pathHandlers = new LinkedHashMap<>();
    private final List<Handler> filters = new ArrayList<>();
    private int port = 5050;
    private String basePath = "";

    public static Server build() {
        return new Server();
    }

    public Server port(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        try {
            RatpackServer.start(spec -> spec
                    .serverConfig(config -> config.port(port))
                    .handlers(chain -> {
                        filters.forEach(filter -> insertFilter(chain, filter));
                        pathHandlers.forEach((path, handlers) -> handlers.forEach(handler -> addPrefix(path, chain, handler)));
                        insertHandler(chain, new HealthHandler());
                    })
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void insertFilter(Chain chain, Handler filter) {
        chain.all(filter);
    }

    private void addPrefix(String path, Chain chain, Action<Chain> handler) {
        try {
            chain.prefix(path, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void insertHandler(Chain chain, Action<Chain> handler) {
        try {
            chain.insert(handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Server filter(Handler filter) {
        filters.add(filter);
        return this;
    }

    public Server filters(Handler... filters) {
        this.filters.addAll(Arrays.asList(filters));
        return this;
    }

    public Server handler(Action<Chain> handler) {
        return handler("", handler);
    }

    public Server handlers(Action<Chain>... handlers) {
        Arrays.stream(handlers).forEach(handler -> handler("", handler));
        return this;
    }

    public Server handler(String path, Action<Chain> handler) {
        var fullPath = basePath.isEmpty() ? path : basePath + "/" + path;
        if (pathHandlers.containsKey(fullPath)) {
            pathHandlers.get(fullPath).add(handler);
        } else {
            var handlers = new ArrayList<Action<Chain>>();
            handlers.add(handler);
            pathHandlers.put(fullPath, handlers);
        }
        return this;
    }

    public Server basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }
}
