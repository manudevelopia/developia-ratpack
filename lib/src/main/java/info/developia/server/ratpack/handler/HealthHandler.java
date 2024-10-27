package info.developia.server.ratpack.handler;

import ratpack.core.handling.Chain;
import ratpack.func.Action;

public class HealthHandler implements Action<Chain> {

    @Override
    public void execute(Chain chain) {
        chain
                .get("db", ctx -> ctx.getResponse()
                        .send("application/json", "{\"message\": \"database is Ok\"}"))
                .get("health", ctx -> ctx.getResponse()
                        .send("application/json", "{\"status\": \"Server is running\"}"))
        ;
    }
}
