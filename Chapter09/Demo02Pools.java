package com.packt.java9hp.ch09_threads;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Demo02Pools {

    public static void main(String... args) {

        demo_pool();
        demo_pools(3, 1, TimeUnit.MILLISECONDS);
        demo_pools(3, 100, TimeUnit.MILLISECONDS);
        demo_pools(3, 1, TimeUnit.SECONDS);

        demo_pools(6, 1, TimeUnit.MILLISECONDS);
        demo_pools(6, 100, TimeUnit.MILLISECONDS);
        demo_pools(6, 1, TimeUnit.SECONDS);
    }

    private static void demo_pool() {
        ExecutorService pool = Executors.newCachedThreadPool();
        IntStream.rangeClosed(1, 3).forEach(i -> pool.execute(new MyRunnable02(i)));
        System.out.println("Before shutdown: isShutdown()=" + pool.isShutdown()
                + ", isTerminated()=" + pool.isTerminated());
        pool.shutdown(); // New threads cannot be submitted
        System.out.println("After  shutdown: isShutdown()=" + pool.isShutdown()
                + ", isTerminated()=" + pool.isTerminated());
        try {
            pool.execute(new MyRunnable02(100));
        } catch(RejectedExecutionException ex){
            System.err.println("Cannot add another worker-thread to the service queue:\n" + ex.getMessage());
        }
        System.out.println("");
        System.out.println("");
        try {
            long timeout = 1;
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            System.out.println("Waiting for all threads completion " + timeout + " " + timeUnit + "...");
            // Blocks until timeout or all threads complete execution
            boolean isTerminated = pool.awaitTermination(timeout, timeUnit);
            System.out.println("isTerminated()=" + isTerminated);
            if (!isTerminated) {
                System.out.println("Calling shutdownNow()...");
                List<Runnable> list = pool.shutdownNow(); // Cancel all running threads
                printRunningThreadIds(list);
                System.out.println("Waiting for threads completion " + timeout + " " + timeUnit + "...");
                isTerminated = pool.awaitTermination(timeout, timeUnit);
                if (!isTerminated){
                    System.out.println("Some threads running...");
                }
                System.out.println("Exiting.");
            }
            System.out.println();
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static void printRunningThreadIds(List<Runnable> l){
        String list = l.stream()
                .map(r -> (MyRunnable02)r)
                .map(mr -> mr.getId())
                .collect(Collectors.joining(","));
        System.out.println(l.size() + " thread"
                + (l.size() == 1 ? " is" : "s are") + " running"
                + (l.size() > 0 ? ": " + list : "") + ".");
    }

    private static void demo_pools(int n, int timeout, TimeUnit timeUnit){
        usePool(n,"SingleThreadExecutor", Executors.newSingleThreadExecutor(), timeout, timeUnit);
        usePool(n,"FixedThreadPool", Executors.newFixedThreadPool(3), timeout, timeUnit);
        usePool(n,"CachedThreadPool", Executors.newCachedThreadPool(), timeout, timeUnit);
    }

    private static void usePool(int n,  String poolName, ExecutorService pool, int timeout, TimeUnit timeUnit) {
        IntStream.rangeClosed(1, n).forEach(i -> pool.execute(new MyRunnable02(i)));
        shutdownAndAwaitTermination(poolName, pool, timeout, timeUnit);
    }

    private static void shutdownAndAwaitTermination(String poolName, ExecutorService pool, int timeout, TimeUnit timeUnit) {
        System.out.println(poolName + ": ");
        pool.shutdown(); // New threads cannot be submitted
        try {
            System.out.println("    waiting for termination " + timeout + " " + timeUnit + "...");
            // Blocks until timeout or all threads complete execution
            if (!pool.awaitTermination(timeout, timeUnit)) {
                List<Runnable> l = pool.shutdownNow(); // Cancel all running threads
                String list = l.stream()
                        .map(r -> (MyRunnable02)r)
                        .map(mr -> mr.getId())
                        .collect(Collectors.joining(","));
                System.out.println("    " + l.size() + " thread"
                        + (l.size() == 1 ? " is" : "s are") + " running"
                        + (l.size() > 0 ? ": " + list : "") + ".");
                System.out.println("    waiting for termination " + timeout + " " + timeUnit + "...");
                if (!pool.awaitTermination(timeout, timeUnit))
                    System.out.println("    some threads are still running...");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("    Exiting.");
    }
}


class MyRunnable02 implements Runnable {
    private String id;

    public MyRunnable02(int id) {
        this.id = String.valueOf(id);
    }

    public String getId(){ return this.id; }

    public void run() {
        double result = IntStream.rangeClosed(1, 100)
                .flatMap(i -> IntStream.rangeClosed(1, 99999))
                .takeWhile(i -> !Thread.currentThread().isInterrupted())
                .asDoubleStream().map(Math::sqrt).average().getAsDouble();

        if(Thread.currentThread().isInterrupted()){
            System.out.println("    Worker " + getId() + ": result=ignored: " + result);
        } else {
            System.out.println("    Worker " + getId() + ": result=" + result);
        }
    }
}