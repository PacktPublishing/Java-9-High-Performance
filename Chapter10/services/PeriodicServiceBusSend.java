package com.packt.java9hp.ch10_microservices.services;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class PeriodicServiceBusSend extends AbstractVerticle {
    private EventBus eb;
    private LocalTime start;
    private String address;
    private String[] caller;
    private int delaySec;

    public PeriodicServiceBusSend(String address, String[] caller, int delaySec) {
        this.address = address;
        this.caller = caller;
        this.delaySec = delaySec;
    }

    public void start() throws Exception {
        System.out.println(this.getClass().getSimpleName() + "(" + address + ", " + delaySec + ") starts...");
        this.eb = vertx.eventBus();
        this.start  = LocalTime.now();
        vertx.setPeriodic(delaySec * 1000, v -> {
            int i = (int)ChronoUnit.SECONDS.between(start, LocalTime.now()) - 1;
            System.out.println(this.getClass().getSimpleName()
                    + " to address " + address + ": " + caller[i]);
            eb.rxSend(address, caller[i]).subscribe(reply -> {
                System.out.println(this.getClass().getSimpleName() + " got reply from address "
                        + address + ":\n    " + reply.body());
                if(i + 1 >= caller.length ){
                    vertx.undeploy(deploymentID());
                }
            }, Throwable::printStackTrace);
        });
    }
}