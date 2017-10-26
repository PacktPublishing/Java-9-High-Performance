package com.packt.java9hp.ch09_threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Demo04Synchronization {
    public static double dResult= 0.0;
    public static int result;
    //private static volatile int result;
/*
    public static synchronized void incrementResult(int i){
        result += i;
    }
*/
    public static void incrementResult(int i){
        synchronized (Demo04Synchronization.class){
            result += i;
        }
    }

    public static void main(String... args) {
        demo_ThreadInterference();
        demo_Synchronized();
        demo_Lock();
    }

    private static void demo_ThreadInterference(){
        System.out.println("demo_ThreadInterference: ");
        MyRunnable04 r1 = new MyRunnable04(1);
        Thread t1 = new Thread(r1);
        MyRunnable04 r2 = new MyRunnable04(2);
        Thread t2 = new Thread(r2);
        t1.start();
        //sleepMs(100);
        t2.start();
        sleepMs(100);
        System.out.println("Result=" + result);
    }

    private static void sleepMs(int sleepMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepMs);
        } catch (InterruptedException e) {}
    }

    private static void demo_Synchronized(){
        System.out.println("demo_Synchronized: ");
        MyRunnable05 r1 = new MyRunnable05();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        t1.start();
        t2.start();
        sleepMs(100);
        System.out.println("Result=" + result);
    }

    private static void demo_Lock(){
        System.out.println("demo_Lock: ");
        MyRunnable06 r1 = new MyRunnable06();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        t1.start();
        t2.start();
        sleepMs(100);
        System.out.println("Result=" + result);
    }


}

class MyRunnable04 implements Runnable {
    private int id;

    public MyRunnable04(int id) {
        this.id = id;
    }

    public void run() {
        IntStream.rangeClosed(1, 250)
                //.peek(i -> System.out.println("Thread " + id + ": " + i))
                //.forEach(i -> Demo04Synchronization.result += i);
                .forEach(Demo04Synchronization::incrementResult);
    }
}

class MyRunnable05 implements Runnable {
    public synchronized void incrementResult(int i){
        Demo04Synchronization.result += i;
    }
    public void run() {
        IntStream.rangeClosed(1, 250)
                .forEach(this::incrementResult);
    }
}

class MyRunnable06 implements Runnable {
    private Lock lock = new ReentrantLock();

    public void incrementResult(int i){
        lock.lock();
        try {
            Demo04Synchronization.result += i;
        } finally {
            lock.unlock();
        }
    }
    public void run() {
        IntStream.rangeClosed(1, 250)
                .forEach(this::incrementResult);
    }
}

