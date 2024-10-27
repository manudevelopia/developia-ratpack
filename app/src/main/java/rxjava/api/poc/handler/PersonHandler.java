package rxjava.api.poc.handler;

import io.reactivex.Single;
import org.davidmoten.rx.jdbc.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Chain;
import ratpack.core.http.MediaType;
import ratpack.func.Action;
import ratpack.rx2.RxRatpack;
import rxjava.api.poc.model.Person;

import java.util.List;

import static ratpack.core.jackson.Jackson.json;

public class PersonHandler implements Action<Chain> {
    private static final Logger LOG = LoggerFactory.getLogger(PersonHandler.class);
    private final Database db = Database.test();

    @Override
    public void execute(Chain chain) {
        chain.get(ctx -> {
            RxRatpack.promise(getData()).then(data -> {
                if (!data.isEmpty()) {
                    ctx.getResponse().contentType(MediaType.APPLICATION_JSON);
                    ctx.render(json(data));
                } else {
                    ctx.notFound();
                }
            });
            LOG.info("waiting for the data!!!");
        });
        chain.get("person", ctx -> {
            RxRatpack.promise(getData()).then(data -> {
                if (!data.isEmpty()) {
                    ctx.getResponse().contentType(MediaType.APPLICATION_JSON);
                    ctx.render(json(data.getFirst()));
                } else {
                    ctx.notFound();
                }
            });
            LOG.info("waiting for the data!!!");
        });
    }

    private Single<List<Person>> getData() {
        return db.select("select name from person")
                .getAs(Person.class).toList();
    }
}
