package rxjava.api.poc;

import ratpack.rx2.RxRatpack;
import rxjava.api.poc.handler.DataHandler;
import rxjava.api.poc.handler.PersonHandler;
import info.developia.server.ratpack.handler.CorsHandler;
import info.developia.server.ratpack.handler.RequestTimingHandler;
import info.developia.server.ratpack.Server;

public class Launcher {

    public static void main(String[] args) {
        RxRatpack.initialize();
        Server.build()
                .basePath("api")
                .filters(new CorsHandler(), new RequestTimingHandler())
                .handler(new DataHandler())
                .handler("data", new DataHandler())
                .handler("persons", new PersonHandler())
                .start();
    }
}
