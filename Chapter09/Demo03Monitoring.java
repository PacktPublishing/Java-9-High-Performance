package com.packt.java9hp.ch09_threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Demo03Monitoring {
    public static void main(String... args) {

        demo_ThreadApi();
        demo_Futures();
        demo_Callables();
        demo_InvokeAll();
        demo_InvokeAny();
    }

    private static void demo_ThreadApi() {
        ExecutorService pool = Executors.newCachedThreadPool();
        MyRunnable03 r1 = new MyRunnable03();
        MyRunnable03 r2 = new MyRunnable03();
        pool.execute(r1);
        pool.execute(r2);
        sleepMs(1000);
        //printAllThreads();
        //printThreadsInfo();
        System.out.println("Worker " + r1.getName() + ": result=" + r1.getResult());
        System.out.println("Worker " + r2.getName() + ": result=" + r2.getResult());
        shutdown(pool);
        //printAllThreads();
        //printThreadsInfo();
    }

    private static void shutdown(ExecutorService pool) {
        pool.shutdown();
        try {
            if(!pool.awaitTermination(1, TimeUnit.SECONDS)){
                pool.shutdownNow();
            }
        } catch (InterruptedException ie) {}
    }

    private static void printAllThreads() {
        System.out.println("printAllThreads():");
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        for(Thread t: map.keySet()){
            System.out.println("    " + t);
            for(StackTraceElement ste: map.get(t)){
                //System.out.println("    " + ste);
            }
        }
    }

    private static void printThreadsInfo() {
        System.out.println("printThreadsInfo():");
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long ids[] = threadBean.getAllThreadIds();
        Arrays.sort(ids);
        ThreadInfo[] tis = threadBean.getThreadInfo(ids, 0);
        for (ThreadInfo ti : tis) {
            if (ti == null) continue;
            System.out.println("    Id=" + ti.getThreadId() + ", state=" + ti.getThreadState() + ", name=" + ti.getThreadName());
        }
    }

    private static void demo_Futures() {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future f1 = pool.submit(new MyRunnable03());
        Future f2 = pool.submit(new MyRunnable03());
        printFuture(f1, 1);
        printFuture(f2, 2);
        shutdown(pool);
        //printFuture(f1, 1);
        //printFuture(f2, 2);
    }

    private static void printFuture(Future future, int id) {
        System.out.println("printFuture():");
        while (!future.isCancelled() && !future.isDone()){
            System.out.println("    Waiting for worker " + id + " to complete...");
            sleepMs(10);
        }
        System.out.println("    Done...");
    }

    private static void demo_Callables() {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future f1 = pool.submit(new MyCallable01<Result>());
        Future f2 = pool.submit(new MyCallable01<Result>());
        printResult(f1, 1);
        printResult(f2, 2);
        shutdown(pool);
        //printResults(List.of(f1, f2));
    }

    private static void printResult(Future<Result> future, int id) {
        System.out.println("printResult():");
        while (!future.isCancelled() && !future.isDone()){
            System.out.println("    Waiting for worker " + id + " to complete...");
            sleepMs(10);
        }
        try {
            Result result = future.get(1, TimeUnit.SECONDS);
            System.out.println("    Worker " + result.getWorkerName()
                    + ": result = " + result.getResult());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void demo_InvokeAll() {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            List<Callable<Result>> callables = List.of(new MyCallable01<Result>(), new MyCallable01<Result>());
            List<Future<Result>> futures = pool.invokeAll(callables);
            printResults(futures);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shutdown(pool);
    }

    private static void demo_InvokeAny() {
        System.out.println("demo_InvokeAny():");
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            List<Callable<Result>> callables = List.of(new MyCallable01<Result>(), new MyCallable01<Result>());
            Result result = pool.invokeAny(callables);
            System.out.println("    Worker " + result.getWorkerName()
                    + ": result = " + result.getResult());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        shutdown(pool);
    }

    private static void printResults(List<Future<Result>> futures) {
        System.out.println("printResults():");
        int i = 1;
        for (Future<Result> future : futures) {
            printResult(future, i++);
        }
    }

    private static void sleepMs(int sleepMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(sleepMs);
        } catch (InterruptedException e) {}
    }
}

class MyRunnable03 implements Runnable {
    private String name;
    private double result;

    public String getName(){ return this.name; }

    public double getResult() {
        return this.result;
    }

    public void run() {
        this.name = Thread.currentThread().getName();
        double result = IntStream.rangeClosed(1, 100)
                .flatMap(i -> IntStream.rangeClosed(1, 99999))
                .takeWhile(i -> !Thread.currentThread().isInterrupted())
                .asDoubleStream().map(Math::sqrt).average().getAsDouble();

        if(!Thread.currentThread().isInterrupted()){
            this.result = result;
        }
    }
}

class MyCallable01<T> implements Callable {

    public Result call() {
        double result = IntStream.rangeClosed(1, 100)
                .flatMap(i -> IntStream.rangeClosed(1, 99999))
                .takeWhile(i -> !Thread.currentThread().isInterrupted())
                .asDoubleStream().map(Math::sqrt).average().getAsDouble();

        if(Thread.currentThread().isInterrupted()){
            return new Result(Thread.currentThread().getName(), 0);
        } else {
            return new Result(Thread.currentThread().getName(), result);
        }
    }
}

class Result {
    private double result;
    private String workerName;

    public Result(String workerName, double result) {
        this.result = result;
        this.workerName = workerName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public double getResult() {
        return result;
    }
}


