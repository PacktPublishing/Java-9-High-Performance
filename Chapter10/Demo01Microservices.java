package com.packt.java9hp.ch10_microservices;

import com.packt.java9hp.ch10_microservices.services.DbServiceBus;
import static io.vertx.rxjava.core.Vertx.vertx;

import com.packt.java9hp.ch10_microservices.services.DbServiceHttp;
import com.packt.java9hp.ch10_microservices.services.PeriodicServiceBusPublish;
import com.packt.java9hp.ch10_microservices.services.PeriodicServiceBusSend;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.RxHelper;

import java.util.concurrent.TimeUnit;

public class Demo01Microservices {

    public static void main(String... args) {
        demo_DeployServer();
        demo_DeployServerAndClient();
        demo_DeployDbServiceHttp();
        demo_DbServiceBusSend();
        demo_DbServiceBusPublish();
    }

    private static void demo_DeployServer() {
        RxHelper.deployVerticle(vertx(), new Server1(8082));
        RxHelper.deployVerticle(vertx(), new Server1(8083));
    }

    private static void demo_DeployServerAndClient() {
        RxHelper.deployVerticle(vertx(), new Server2(8082));
        RxHelper.deployVerticle(vertx(), new Client(8082));
    }

    private static void demo_DeployDbServiceHttp() {
        RxHelper.deployVerticle(vertx(), new DbServiceHttp(8082));
    }

    private static void demo_DbServiceBusSend() {
        Vertx vertx = vertx();
        RxHelper.deployVerticle(vertx, new DbServiceBus(1));
        RxHelper.deployVerticle(vertx, new DbServiceBus(2));
        delayMs(200);
        String[] msg1 = {"Mayur", "Rohit", "Nick" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.INSERT, msg1, 1));
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.INSERT, msg1, 1));
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.INSERT, msg1, 1));
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.INSERT, msg1, 1));
        String[] msg2 = {"all", "all", "all" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.PROCESS, msg2, 1));
        String[] msg3 = {"1", "1", "2", "8" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.READ_PROCESSED, msg3, 1));
    }

    private static void demo_DbServiceBusPublish() {
        Vertx vertx = vertx();
        RxHelper.deployVerticle(vertx, new DbServiceBus(1));
        RxHelper.deployVerticle(vertx, new DbServiceBus(2));
        delayMs(200);
        String[] msg1 = {"Mayur", "Rohit", "Nick" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusPublish(DbServiceBus.INSERT, msg1, 1));
        delayMs(200);
        String[] msg2 = {"all", "all", "all" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.PROCESS, msg2, 1));
        String[] msg3 = {"1", "1", "2", "8" };
        RxHelper.deployVerticle(vertx, new PeriodicServiceBusSend(DbServiceBus.READ_PROCESSED, msg3, 1));
    }

    private static void delayMs(int ms){
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {}

    }

    private static void demo_DbServiceBus2() {
        Vertx vertx = vertx();
        //vertx.deployVerticle(DbServiceBus.class.getName());
        RxHelper.deployVerticle(vertx, new DbServiceBus(1));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventBus eb = vertx.eventBus();
        eb.rxSend(DbServiceBus.READ_PROCESSED, "Hi, readProcessed!").subscribe(System.out::println, Throwable::printStackTrace);
        //eb.send(DbServiceBus.READ_PROCESSED, "Hi, readProcessed!");
        //vertx().eventBus().publish(DbServiceBus.READ_PROCESSED, "Hi, publish!");

        //EventBus eb = (EventBus) vertx().eventBus();
        vertx.setPeriodic(1000, v -> {
            eb.send(DbServiceBus.READ_PROCESSED, "ping!", reply -> {
                if (reply.succeeded()) {
                    System.out.println("Received reply " + reply.result().body());
                } else {
                    System.out.println("No reply");
                }
            });

        });
    }

}



