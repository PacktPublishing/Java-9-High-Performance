package com.packt.java9hp.ch10_microservices;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Client extends AbstractVerticle {
    private int port;
    public Client(int port) {
        this.port = port;
    }
    public void start() throws Exception {
        HttpClient client = vertx.createHttpClient();
        LocalTime start = LocalTime.now();
        vertx.setPeriodic(1000, v -> {
            client.getNow(port, "localhost", "?name=Nick",
                    response -> response.bodyHandler(System.out::println));
            if(ChronoUnit.SECONDS.between(start, LocalTime.now()) > 3 ){
                vertx.undeploy(deploymentID());
            }
        });
    }
}
