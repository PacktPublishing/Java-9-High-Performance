package com.packt.java9hp.ch10_microservices.services.handlers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.rxjava.ext.sql.SQLRowStream;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class DbHandler {
    private JDBCClient dbClient;

    private static String SQL_CREATE_WHO_CALLED = "CREATE TABLE IF NOT EXISTS who_called(name VARCHAR(10) NOT NULL, " +
            "create_ts TIMESTAMP(6) WITH TIME ZONE DEFAULT now() NOT NULL)";
    private static String SQL_CREATE_PROCESSED = "CREATE TABLE IF NOT EXISTS processed(name VARCHAR(10) NOT NULL, length INTEGER, " +
            "create_ts TIMESTAMP(6) WITH TIME ZONE DEFAULT now() NOT NULL) ";
    private static String SQL_INSERT_WHO_CALLED = "INSERT INTO who_called(name) VALUES (?)";
    private static String SQL_SELECT_TO_PROCESS = "SELECT name FROM who_called w where name not in (select name from processed) order by w.create_ts for update";
    private static String SQL_INSERT_PROCESSED = "INSERT INTO processed(name, length) values(?, ?)";
    private static String SQL_READ_PROCESSED = "SELECT name, length, create_ts FROM processed order by create_ts desc limit ?";

    public DbHandler(Vertx vertx){
        JsonObject config = new JsonObject()
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("url", "jdbc:hsqldb:mem:test?shutdown=true");
        dbClient = JDBCClient.createNonShared(vertx, config);
        dbClient.rxGetConnection()
                .flatMap(conn -> conn.rxUpdate(SQL_CREATE_WHO_CALLED)
                        .doAfterTerminate(conn::close))
                .subscribe(r -> System.out.println("Table who_called created if not existed"),
                        Throwable::printStackTrace);
        dbClient.rxGetConnection()
                .flatMap(conn -> conn.rxUpdate(SQL_CREATE_PROCESSED)
                        .doAfterTerminate(conn::close))
                .subscribe(r -> System.out.println("Table processed created if not existed"),
                        Throwable::printStackTrace);
    }

    public void insert(String name, Action1<UpdateResult> onSuccess, Action1<Throwable> onError){
        printAction("inserts " + name);
        dbClient.rxGetConnection()
                .flatMap(conn -> conn.rxUpdateWithParams(SQL_INSERT_WHO_CALLED, new JsonArray().add(name))
                        .doAfterTerminate(conn::close))
                .subscribe(onSuccess, onError);
    }

    public void process(Func1<JsonArray, Observable<JsonArray>> process, Action1<Throwable> onError) {
        printAction("process all records not processed yet");
        dbClient.rxGetConnection()
                .flatMapObservable(conn -> conn.rxQueryStream(SQL_SELECT_TO_PROCESS)
                        .flatMapObservable(SQLRowStream::toObservable)
                        .flatMap(process)
                        .flatMap(js -> conn.rxUpdateWithParams(SQL_INSERT_PROCESSED, js)
                                .flatMapObservable(ur->Observable.just(js)))
                        .doAfterTerminate(conn::close))
                .subscribe(js -> printAction("processed " + js), onError);
    }

    public void readProcessed(String count, Action1<ResultSet> onSuccess, Action1<Throwable> onError) {
        printAction("reads " + count + " last processed records");
        dbClient.rxGetConnection()
                .flatMap(conn -> conn.rxQueryWithParams(SQL_READ_PROCESSED, new JsonArray().add(count))
                        .doAfterTerminate(conn::close))
                .subscribe(onSuccess, onError);
    }

    private void printAction(String action) {
        System.out.println(this.getClass().getSimpleName() + " " + action);
    }

}
