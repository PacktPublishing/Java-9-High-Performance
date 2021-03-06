package com.packt.java9hp.ch10_microservices;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

public class Server2 extends AbstractVerticle{
    private int port;
    public Server2(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        System.out.println();
        HttpServer server = vertx.createHttpServer();
        server.requestStream().toObservable()
                .subscribe(request -> request.response()
                        .end("Hi, " + request.getParam("name") + "! Hello from " + Thread.currentThread().getName() + " on port " + port + "!")
                );
        server.rxListen(port).subscribe();
        System.out.println(Thread.currentThread().getName()
                + " is waiting on port " + port + "...");
    }
}
