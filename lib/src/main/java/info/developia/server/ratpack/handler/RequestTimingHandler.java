package info.developia.server.ratpack.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

public class RequestTimingHandler implements Handler {
    private static final Logger LOG = LoggerFactory.getLogger(RequestTimingHandler.class);

    @Override
    public void handle(Context ctx) {
        long startTime = System.currentTimeMillis();
        LOG.info("{} Request {} received", ctx.getRequest().getMethod(), ctx.getRequest().getUri());
        ctx.getResponse().beforeSend(response -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            LOG.info("{} Request {} processed in {} ms", ctx.getRequest().getMethod(), ctx.getRequest().getUri(), elapsedTime);
        });
        ctx.next();
    }
}
