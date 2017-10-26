package com.packt.java9hp.ch10_microservices.services;

import com.packt.java9hp.ch10_microservices.services.handlers.DbHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.rxjava.core.AbstractVerticle;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DbServiceBus extends AbstractVerticle {
    private int id;
    private String instanceId;
    private DbHandler dbHandler;
    public static final String INSERT = "INSERT";
    public static final String PROCESS = "PROCESS";
    public static final String READ_PROCESSED = "READ_PROCESSED";

    public DbServiceBus(int id) {
        this.id = id;
    }

    public void start() throws Exception {
        this.instanceId = this.getClass().getSimpleName() + "(" + id + ")";
        System.out.println(instanceId + " starts...");
        this.dbHandler = new DbHandler(vertx);

        vertx.eventBus().consumer(INSERT).toObservable()
                .subscribe(msg -> {
                    printRequest(INSERT, msg.body().toString());
                    Action1<UpdateResult> onSuccess = ur ->
                            msg.reply(constructReply(INSERT,ur.getUpdated() + " record for " + msg.body() + " is inserted"));
                    Action1<Throwable> onError = ex -> {
                        printStackTrace(INSERT, ex);
                        msg.reply(constructReply(INSERT, "Backend error"));
                    };
                    dbHandler.insert(msg.body().toString(), onSuccess, onError);
                });

        vertx.eventBus().consumer(PROCESS).toObservable()
                .subscribe(msg -> {
                    printRequest(PROCESS, msg.body().toString());
                    msg.reply(constructReply(PROCESS,"Processing " + msg.body().toString() +"..."));

                    Func1<JsonArray, Observable<JsonArray>> process = jsonArray -> {
                        JsonArray js = new JsonArray();
                        try {
                            String name = jsonArray.getString(0);
                            js.add(name).add(name.length());
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {}
                        return Observable.just(js);
                    };

                    Action1<Throwable> onError = ex -> {
                        printStackTrace(PROCESS, ex);
                        msg.reply(constructReply(PROCESS, "Backend error"));
                    };
                    dbHandler.process(process, onError);
                });

        vertx.eventBus().consumer(READ_PROCESSED).toObservable()
                .subscribe(msg -> {
                    printRequest(READ_PROCESSED, msg.body().toString());
                    Action1<ResultSet> onSuccess = rs -> {
                        Observable.just(rs.getResults().size()>0?rs.getResults().stream().map(Object::toString)
                                .collect(Collectors.joining("\n")):"")
                                .subscribe(s->msg.reply(constructReply(READ_PROCESSED, "\n        " + s)));

                    };
                    Action1<Throwable> onError = ex -> {
                        printStackTrace(READ_PROCESSED, ex);
                        msg.reply(constructReply(READ_PROCESSED, "Backend error"));
                    };
                    dbHandler.readProcessed(msg.body().toString(), onSuccess, onError);
                });
    }

    private void printRequest(String action, String msg) {
        System.out.println(instanceId + "." + action + " got request: " + msg);
    }

    private String constructReply(String action, String msg) {
        return instanceId + "." + action + ": " + msg;
    }

    private void printStackTrace(String action, Throwable ex) {
        System.err.println(this.getClass().getSimpleName() + "." + action + " got exception:");
        ex.printStackTrace();
    }

}
