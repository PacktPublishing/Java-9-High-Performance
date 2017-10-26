package com.packt.java9hp.ch10_microservices.services;

import com.packt.java9hp.ch10_microservices.services.handlers.DbHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.stream.Collectors;

public class DbServiceHttp extends AbstractVerticle {
    private int port;
    private DbHandler dbHandler;

    public DbServiceHttp(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        System.out.println(this.getClass().getSimpleName() + "(" + port + ") starts...");
        dbHandler = new DbHandler(vertx);

        Router router = Router.router(vertx);
        router.put("/insert/:name").handler(this::insert);
        router.get("/process").handler(this::process);
        router.get("/readProcessed").handler(this::readProcessed);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
    }

    private void insert(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String name = routingContext.request().getParam("name");

        printAction("insert " + name);

        Action1<UpdateResult> onSuccess = ur ->
                response.setStatusCode(200)
                        .end(ur.getUpdated() + " record for " + name + " is inserted");
        Action1<Throwable> onError = ex -> {
            printStackTrace("process", ex);
            response.setStatusCode(400).end("No record inserted due to backend error");
        };
        dbHandler.insert(name, onSuccess, onError);

    }

    private void process(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        printAction("process all");
        response.setStatusCode(200).end("Processing...");

        Func1<JsonArray, Observable<JsonArray>> process = jsonArray -> {
            String name = jsonArray.getString(0);
            JsonArray js = new JsonArray().add(name).add(name.length());
            return Observable.just(js);
        };

        Action1<Throwable> onError = ex -> {
            printStackTrace("process", ex);
            response.setStatusCode(400).end("Backend error");
        };
        dbHandler.process(process, onError);
    }

    private void readProcessed(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String count = routingContext.request().getParam("count");

        printAction("readProcessed " + count + " entries");

        Action1<ResultSet> onSuccess = rs -> {
            Observable.just(rs.getResults().size()>0?rs.getResults().stream().map(Object::toString)
                        .collect(Collectors.joining("\n")):"")
                    .subscribe(s->response.setStatusCode(200).end(s));
        };
        Action1<Throwable> onError = ex -> {
            printStackTrace("readProcessed", ex);
            response.setStatusCode(400).end("Backend error");
        };
        dbHandler.readProcessed(count, onSuccess, onError);
    }

    private void printAction(String action) {
        System.out.println(this.getClass().getSimpleName() + "." + action);
    }

    private void printStackTrace(String action, Throwable ex) {
        System.err.println(this.getClass().getSimpleName() + "." + action + " got exception:");
        ex.printStackTrace();
    }
}

