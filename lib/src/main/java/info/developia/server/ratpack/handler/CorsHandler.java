package info.developia.server.ratpack.handler;

import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;
import ratpack.core.http.Response;

public class CorsHandler implements Handler {

    @Override
    public void handle(Context ctx) {
        Response response = ctx.getResponse();
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if (ctx.getRequest().getMethod().isOptions()) {
            response.status(204).send();
        } else {
            ctx.next();
        }
    }
}
