package com.packt.java9hp.ch09_threads;

import java.util.stream.IntStream;
import java.lang.Thread;
import java.lang.Runnable;

public class Demo01Runnable {

    public static void main(String... args) {
        demo_thread_01();
        demo_thread_02();
        demo_thread_03();
        demo_thread_04();

        demo_runnable_01();
        demo_runnable_02();

        demo_lambda_01();
        demo_lambda_02();
        demo_lambda_03();
    }

    private static void demo_thread_01() {
        System.out.print("demo_thread_01(): ");
        MyThread00 t1 = new MyThread00();
        t1.start();
        System.out.println("Thread name=" + t1.getName());
    }

    private static void demo_thread_02() {
        System.out.print("demo_thread_02(): ");
        MyThread t1 = new MyThread("Thread01");
        t1.calcualteAverageSqrt();
        System.out.println(t1.getName() + ": result=" + t1.getResult());
    }

    private static void demo_thread_03() {
        System.out.print("demo_thread_03(): ");
        MyThread01 t1 = new MyThread01("Thread01");
        t1.start();
        System.out.println(t1.getName() + ": result=" + t1.getResult());
    }

    private static void demo_thread_04() {
        System.out.print("demo_thread_04(): ");
        MyThread01 t1 = new MyThread01("Thread01");
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t1.getName() + ": result=" + t1.getResult());
        System.out.println("Thread name=" + Thread.currentThread().getName());
    }

    private static void demo_runnable_01() {
        System.out.print("demo_runnable_01(): ");
        MyRunnable01 myRunnable = new MyRunnable01(1);
        Thread t1 = new Thread(myRunnable);
        t1.start();
        System.out.println("Worker " + myRunnable.getId() + ": result=" + myRunnable.getResult());
    }

    private static void demo_runnable_02() {
        System.out.print("demo_runnable_02(): ");
        MyRunnable01 myRunnable = new MyRunnable01(1);
        Thread t1 = new Thread(myRunnable);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Worker " + myRunnable.getId() + ": result=" + myRunnable.getResult());
    }

    private static void demo_lambda_01() {
        System.out.print("demo_lambda_01(): ");
        String id = "1";
        Thread t1 = new Thread(() -> IntStream.rangeClosed(1, 99999).asDoubleStream()
                .map(Math::sqrt).average().ifPresent(d -> System.out.println("Worker " + id + ": result=" + d)));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void demo_lambda_02() {
        System.out.print("demo_lambda_02(): ");
        String id = "1";
        Runnable r = () -> IntStream.rangeClosed(1, 99999).asDoubleStream()
                    .map(Math::sqrt).average().ifPresent(d -> System.out.println("Worker " + id + ": result=" + d));
        Thread t1 = new Thread(r);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void calculateAverage(String id) {
        IntStream.rangeClosed(1, 99999).asDoubleStream()
                .map(Math::sqrt).average().ifPresent(d -> System.out.println("Worker " + id + ": result=" + d));
    }

    private static void demo_lambda_03() {
        System.out.println();
        System.out.print("demo_lambda_03(): ");
        Thread t1 = new Thread(() -> calculateAverage("1"));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

}

class MyThread00 extends Thread {
}

class MyThread extends Thread {
    private double result;

    public MyThread(String name) {
        super(name);
    }

    public void calcualteAverageSqrt() {
        result = IntStream.rangeClosed(1, 99999).asDoubleStream().map(Math::sqrt).average().getAsDouble();
    }

    public double getResult() {
        return this.result;
    }
}

class MyThread01 extends Thread {
    private double result;

    public MyThread01(String name) {
        super(name);
    }

    public void run() {
        result =  IntStream.rangeClosed(1, 99999).asDoubleStream().map(Math::sqrt).average().getAsDouble();

        for (int i = 1; i < 100000; i++) {
            double s = Math.sqrt(1. * i);
            result = result + s;
        }
        result = result / 99999;

    }

    public double getResult() {
        return this.result;
    }
}

class MyRunnable01 implements Runnable {
    private String id;
    private double result;

    public MyRunnable01(int id) {
        this.id = String.valueOf(id);
    }

    public String getId() {
        return this.id;
    }

    public double getResult() {
        return this.result;
    }

    public void run() {
        result = IntStream.rangeClosed(1, 99999).asDoubleStream().map(Math::sqrt).average().getAsDouble();
    }

}


