package com.packt.java9hp.ch09_threads;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import rx.observables.MathObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;


public class Demo05Reactive {
    public static void main(String... args) {
        demo_ForLoop();
        demo_Stream();
        demo_Flow();
        demo_Observable1();
        demo_Observable2();
        demo_Observable3();
        demo_Observable4();
        demo_Observable5();
        demo_Schedulers();
    }

    private static void demo_ForLoop(){
        List<Double> r = new ArrayList<>();
        for(int i = 1; i < 6; i++){
            System.out.println(i);
            if(i%2 == 0){
                System.out.println(i);
                r.add(doSomething(i));
            }
        }
        double sum = 0d;
        for(double d: r){
            sum += d;
        }
        System.out.println(sum / r.size());
    }

    private static double doSomething(int i){
        if(i == 2){
            //throw new RuntimeException("Test excepetion propagation");
        }
        return Math.sqrt(1.*i);
    }

    private static void demo_Stream(){
        double a = IntStream.rangeClosed(1, 5)
                .peek(System.out::println)
                .filter(i -> i%2 == 0)
                .peek(System.out::println)
                .mapToDouble(Demo05Reactive::doSomething)
                .average().getAsDouble();
        System.out.println(a);
    }

    private static void demo_Observable1(){
        Observable.just(1,2,3,4,5)
                .doOnNext(System.out::println)
                .filter(i -> i%2 == 0)
                .doOnNext(System.out::println)
                .map(Demo05Reactive::doSomething)
                .reduce((r, d) -> r + d)
                .map(r -> r / 2)
                .subscribe(System.out::println);
    }

    private static void demo_Observable2(){
        Observable<Double> observable = Observable
                .just(1,2,3,4,5)
                .doOnNext(System.out::println)
                .filter(i -> i%2 == 0)
                .doOnNext(System.out::println)
                .map(Demo05Reactive::doSomething);
                //.cache();

        observable
                .reduce((r, d) -> r + d)
                .map(r -> r / 2)
                .subscribe(System.out::println);

        observable
                .reduce((r, d) -> r + d)
                .subscribe(System.out::println);

    }

    private static Observable<Double> getObservable(){
        return Observable.just(1,2,3,4,5)
                .doOnNext(System.out::println)
                .filter(i -> i%2 == 0)
                .doOnNext(System.out::println)
                .map(Demo05Reactive::doSomething);
    }

    private static void demo_Observable3(){
        getObservable()
                .window(5)
                .flatMap(d->{
                    MathObservable<Double> mathObservable = MathObservable.from(RxJavaInterop.toV1Observable(d, BackpressureStrategy.BUFFER));
                    return Observable.just(mathObservable.averageDouble(d1->d1));
                })
                .subscribe(System.out::println);
    }

    private static Observable<Double> average(Observable<Double> observable){
        MathObservable<Double> mathObservable = MathObservable.from(RxJavaInterop.toV1Observable(observable, BackpressureStrategy.BUFFER));
        return Observable.just(mathObservable.averageDouble(d1->d1).last().toBlocking().single());

    }

    private static Observable<Double> sum(Observable<Double> observable){
        MathObservable<Double> mathObservable = MathObservable.from(RxJavaInterop.toV1Observable(observable, BackpressureStrategy.BUFFER));
        return Observable.just(mathObservable.sumDouble(d1->d1).last().toBlocking().single());

    }

    private static void demo_Observable4(){
        getObservable()
                .window(5)
                .flatMap(d->average(d))
                .subscribe(v -> System.out.println("Result=" + v),
                        e -> {
                            System.out.println("Error: " + e.getMessage());
                            e.printStackTrace();
                        },
                        () -> System.out.println("All the data processed"));
    }

    private static void demo_Observable5(){
        average(getObservable())
                .subscribe(System.out::println);
    }

    private static void demo_Schedulers(){
        Observable<Double> observable = getObservable();//.cache();

        System.out.println("Just print:");
        observable.subscribeOn(Schedulers.io())
                .subscribe(System.out::println);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Average:");
        average(observable)
                .subscribe(System.out::println);

        System.out.println("Sum:");
        sum(observable)
                .subscribe(System.out::println);
    }

    private static void demo_Flow() {
        int[] start = {0};
        ExecutorService execService = Executors.newFixedThreadPool(2);
        try (PeriodicPublisher<Integer> publisher = new PeriodicPublisher<>(execService, 10,
                ()-> start[0] += 1, 100, TimeUnit.MILLISECONDS)){
            publisher.subscribe(new Subscriber<>(Task.SUM));
            publisher.subscribe(new Subscriber<>(Task.AVERAGE));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

enum Task {
    SUM,
    AVERAGE
}

class Subscriber<T> implements Flow.Subscriber<T> {
    private Task task;
    private int number;
    private String result;
    private Flow.Subscription subscription;

    public Subscriber(Task task){
        this.task = task;
    }

    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    public void onNext(T item) {
        if(item != null) {
            number = (int)item;
            switch (task){
                case SUM:
                    calcSum(number);
                    break;
                case AVERAGE:
                    calcAverage(number);
            }
        }
        this.subscription.request(1);
    }

    public void onError(Throwable ex) {
        ex.printStackTrace();
    }

    public void onComplete() {
        System.out.println(task + "(" + number + "): " + result);
    }

    private void calcSum(int number){
        int sum = IntStream.rangeClosed(1, number).sum();
        result = String.valueOf(sum);
        System.out.println(task + "(" + number + "): " + result);
    }

    private void calcAverage(int number){
        double[] av = {0.0};
        IntStream.rangeClosed(1, number).average().ifPresent(d -> av[0]=d);
        result = String.valueOf(av[0]);
        System.out.println(task + "(" + number + "): " + result);
    }

}

class Subscription<T> implements Flow.Subscription {
    public void request(long n) {
        System.out.println("request " + n);
    }

    public synchronized void cancel() {
        System.out.println("cancel");
    }
}

class PeriodicPublisher<T> extends SubmissionPublisher<T> {
    final ExecutorService executor;
    final ScheduledFuture<?> periodicTask;
    final ScheduledExecutorService scheduler;

    PeriodicPublisher(ExecutorService executor, int maxBufferCapacity,
                      Supplier<? extends T> supplier,
                      long period, TimeUnit unit) {
        super(executor, maxBufferCapacity);
        this.executor = executor;
        scheduler = new ScheduledThreadPoolExecutor(1);
        periodicTask = scheduler.scheduleAtFixedRate(
                () -> submit(supplier.get()), 0, period, unit);
    }

    public void subscribe(Subscriber<Integer> subscriber){
        super.subscribe((Flow.Subscriber)subscriber);
        subscriber.onSubscribe(new Subscription());
    }
    public void close() {
        periodicTask.cancel(false);
        scheduler.shutdown();
        super.close();
    }
}

class TransformProcessor<S,T> extends SubmissionPublisher<T> implements Flow.Processor<S,T> {
    final Function<? super S, ? extends T> function;
    Flow.Subscription subscription;
    TransformProcessor(Executor executor, int maxBufferCapacity,
                       Function<? super S, ? extends T> function) {
        super(executor, maxBufferCapacity);
        this.function = function;
    }
    public void onSubscribe(Flow.Subscription subscription) {
        (this.subscription = subscription).request(1);
    }
    public void onNext(S item) {
        subscription.request(1);
        submit(function.apply(item));
    }
    public void onError(Throwable ex) { closeExceptionally(ex); }
    public void onComplete() { close(); }
}