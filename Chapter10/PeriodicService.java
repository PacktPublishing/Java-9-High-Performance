package com.packt.java9hp.ch10_microservices;

import io.vertx.rxjava.core.AbstractVerticle;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class PeriodicService extends AbstractVerticle {
    public void start() throws Exception {
        System.out.println("Vertical PeriodicServiceBusSend is deployed");
        LocalTime start = LocalTime.now();
        vertx.setPeriodic(1000, v-> {
            System.out.println("Go!");
            if(ChronoUnit.SECONDS.between(start, LocalTime.now()) > 3 ){
                vertx.undeploy(deploymentID());
            }
        });
    }

    public void stop() throws Exception {
        System.out.println("Vertical PeriodicServiceBusSend is un-deployed");
    }
}

