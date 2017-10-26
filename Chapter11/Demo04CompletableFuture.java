package com.packt.java9hp.ch11_newapis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.time.StopWatch;

public class Demo04CompletableFuture {
    static StopWatch stopWatch = new StopWatch();
    static Stage failedStage;
    static String SUCCESS = "Success";

    public static void main(String... args) {
        failedStage = Stage.Walls;
        demo_sequentially();
        demo_Future1();
        demo_Future2();
        demo_Future3();
        demo_CompletableFuture1();
        demo_CompletableFuture2();
        demo_CompletableFuture3();
        demo_CompletableFuture4();
        demo_CompletableFuture5();
        failedStage = Stage.WallsMaterials;
        demo_CompletableFuture6();
        demo_CompletableFuture7();
        demo_CompletableFuture8();
        demo_CompletableFuture9();
        demo_CompletableFuture10();
        demo_CompletableFuture11();
    }

    private static String doStage(Stage stage) {
        String result = SUCCESS;
        boolean failed = stage.equals(failedStage);
        if (failed) {
            sleepSec(2);
            result = stage + " were not collected";
            System.out.println(result);
        } else {
            sleepSec(1);
            System.out.println(stage + " are ready");
        }
        return result;
    }

    private static String doStage(Stage stage, String previousStageResult) {
        String result = SUCCESS;
        boolean failed = stage.equals(failedStage);
        if (isSuccess(previousStageResult)) {
            if (failed) {
                sleepSec(2);
                result = stage + " stage was not completed";
                System.out.println(result);
            } else {
                sleepSec(1);
                System.out.println(stage + " stage is completed");
            }
        } else {
            result = stage + " stage was not started because: " + previousStageResult;
            System.out.println(result);
        }
        return result;
    }

    private static String doStageEx(Stage stage) {
        boolean failed = stage.equals(failedStage);
        if (failed) {
            sleepSec(2);
            throw new RuntimeException(stage + " stage was not completed");
        } else {
            sleepSec(1);
            System.out.println(stage + " stage is completed");
        }
        return SUCCESS;
    }

    private static void demo_sequentially() {
        stopWatch.start();
        String result11 = doStage(Stage.FoundationMaterials);
        String result12 = doStage(Stage.Foundation, result11);
        String result21 = doStage(Stage.WallsMaterials);
        String result22 = doStage(Stage.Walls, getResult(result21, result12));
        String result31 = doStage(Stage.RoofMaterials);
        String result32 = doStage(Stage.Roof, getResult(result31, result22));
        System.out.println("House was" + (isSuccess(result32) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
    }

    private static void demo_Future1() {
        stopWatch.start();
        ExecutorService execService = Executors.newCachedThreadPool();
        Callable<String> t11 = () -> doStage(Stage.FoundationMaterials);
        Future<String> f11 = execService.submit(t11);
        List<Future<String>> futures = new ArrayList<>();
        futures.add(f11);

        Callable<String> t21 = () -> doStage(Stage.WallsMaterials);
        Future<String> f21 = execService.submit(t21);
        futures.add(f21);

        Callable<String> t31 = () -> doStage(Stage.RoofMaterials);
        Future<String> f31 = execService.submit(t31);
        futures.add(f31);

        String result1 = getSuccessOrFirstFailure(futures);

        String result2 = doStage(Stage.Foundation, result1);
        String result3 = doStage(Stage.Walls, getResult(result1, result2));
        String result4 = doStage(Stage.Roof, getResult(result1, result3));
        System.out.println("House was" + (isSuccess(result4) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
    }

    private static void demo_Future2() {
        stopWatch.start();
        ExecutorService execService = Executors.newCachedThreadPool();
        Callable<String> t11 = () -> doStage(Stage.FoundationMaterials);
        Future<String> f11 = execService.submit(t11);
        List<Future<String>> futures = new ArrayList<>();
        futures.add(f11);

        Callable<String> t21 = () -> doStage(Stage.WallsMaterials);
        Future<String> f21 = execService.submit(t21);
        futures.add(f21);

        Callable<String> t31 = () -> doStage(Stage.RoofMaterials);
        Future<String> f31 = execService.submit(t31);
        futures.add(f31);

        String result1 = getSuccessOrFirstFailure(futures);

        String result3 = doStage(Stage.Walls, getResult(result1, doStage(Stage.Foundation, result1)));
        String result4 = doStage(Stage.Roof, getResult(result1, result3));
        System.out.println("House was" + (isSuccess(result4) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
    }

    private static void demo_Future3() {
        stopWatch.start();
        ExecutorService execService = Executors.newCachedThreadPool();
        Callable<String> t11 = () -> doStage(Stage.FoundationMaterials);
        Future<String> f11 = execService.submit(t11);
        List<Future<String>> futures = new ArrayList<>();
        futures.add(f11);

        Callable<String> t21 = () -> doStage(Stage.WallsMaterials);
        Future<String> f21 = execService.submit(t21);
        futures.add(f21);

        Callable<String> t31 = () -> doStage(Stage.RoofMaterials);
        Future<String> f31 = execService.submit(t31);
        futures.add(f31);

        String result1 = getSuccessOrFirstFailure(futures);

        Supplier<String> supplier1 = () -> doStage(Stage.Foundation, result1);
        Supplier<String> supplier2 = () -> getResult(result1, supplier1.get());
        Supplier<String> supplier3 = () -> doStage(Stage.Walls, supplier2.get());
        Supplier<String> supplier4 = () -> getResult(result1, supplier3.get());
        Supplier<String> supplier5 = () -> doStage(Stage.Roof, supplier4.get());
        System.out.println("House was" + (isSuccess(supplier5.get()) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
    }

    private static void demo_CompletableFuture1() {
        stopWatch.start();
        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.FoundationMaterials));
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.WallsMaterials));
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.RoofMaterials));

        CompletableFuture<Void> cfAll
                = CompletableFuture.allOf(cf1, cf2, cf3);

        try {
            cfAll.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void demo_CompletableFuture2() {
        stopWatch.start();
        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.FoundationMaterials));
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.WallsMaterials));
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.RoofMaterials));

        String result1 = Stream.of(cf1, cf2, cf3)
                .map(CompletableFuture::join)
                .collect(Collectors.toSet())
                .stream().filter(r -> !isSuccess(r))
                .findAny().orElse(SUCCESS);
        //Still blocking while getting result

        String result2 = doStage(Stage.Foundation, result1);
        String result3 = doStage(Stage.Walls, getResult(result1, result2));
        String result4 = doStage(Stage.Roof, getResult(result1, result3));
        System.out.println("House was" + (isSuccess(result4) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
    }

    private static void demo_CompletableFuture3() {
        stopWatch.start();
        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStage(Stage.FoundationMaterials));
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStage(Stage.WallsMaterials));
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStage(Stage.RoofMaterials));

        String result1 = Stream.of(cf1, cf2, cf3)
                .map(CompletableFuture::join)
                .collect(Collectors.toSet())
                .stream().filter(r -> !isSuccess(r))
                .findAny().orElse(SUCCESS);

        String result4
                = CompletableFuture.supplyAsync(() -> doStage(Stage.Foundation, result1))
                .thenApply(result2 -> doStage(Stage.Walls, getResult(result1, result2)))
                .thenApply(result3 -> doStage(Stage.Roof, getResult(result1, result3)))
                .join();
        System.out.println("House was" + (isSuccess(result4) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
        System.out.println("Out!!");
    }

    private static void demo_CompletableFuture4() {
        stopWatch.start();
        ExecutorService pool = Executors.newCachedThreadPool();

        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStage(Stage.FoundationMaterials), pool);
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStage(Stage.WallsMaterials), pool);
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStage(Stage.RoofMaterials), pool);

        String result1 = Stream.of(cf1, cf2, cf3)
                .map(CompletableFuture::join)
                .collect(Collectors.toSet())
                .stream().filter(r -> !isSuccess(r))
                .findAny().orElse(SUCCESS);

        String result4
                = CompletableFuture.supplyAsync(() -> doStage(Stage.Foundation, result1), pool)
                .thenApplyAsync(result2 -> doStage(Stage.Walls, getResult(result1, result2)), pool)
                .thenApplyAsync(result3 -> doStage(Stage.Roof, getResult(result1, result3)), pool)
                .join();
        System.out.println("House was" + (isSuccess(result4) ? "" : " not") + " built in " + stopWatch.getTime() / 1000. + " sec");
        System.out.println("Out!!");
    }

    private static void demo_CompletableFuture5() {
        stopWatch.start();
        ExecutorService pool = Executors.newCachedThreadPool();

        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.FoundationMaterials), pool);
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.WallsMaterials), pool);
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.RoofMaterials), pool);

        CompletableFuture.allOf(cf1, cf2, cf3)
                .thenComposeAsync(result -> CompletableFuture.supplyAsync(() -> SUCCESS), pool)
                .thenApplyAsync(result -> doStage(Stage.Foundation, result), pool)
                .thenApplyAsync(result -> doStage(Stage.Walls, result), pool)
                .thenApplyAsync(result -> doStage(Stage.Roof, result), pool)
                .handleAsync((result, ex) -> {
                    System.out.println("House was" +
                            (isSuccess(result) ? "" : " not") +
                            " built in " + stopWatch.getTime() / 1000. + " sec");
                    if (result == null) {
                        System.out.println("Because: " + ex.getMessage());
                        return ex.getMessage();
                    } else {
                        return result;
                    }
                }, pool);
        System.out.println("Out!!!!!");
    }

    private static void demo_CompletableFuture6() {
        stopWatch.start();
        ExecutorService pool = Executors.newCachedThreadPool();

        CompletableFuture<String> cf1
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.FoundationMaterials), pool);
        CompletableFuture<String> cf2
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.WallsMaterials), pool);
        CompletableFuture<String> cf3
                = CompletableFuture.supplyAsync(() -> doStageEx(Stage.RoofMaterials), pool);

        CompletableFuture<Void> cfAllMaterialsVoid = CompletableFuture.allOf(cf1, cf2, cf3);

        CompletableFuture<String> cfAllMaterialsResult = cfAllMaterialsVoid
                .thenComposeAsync(result -> CompletableFuture.supplyAsync(() -> SUCCESS), pool);

        CompletableFuture<String> cfFoundation = cfAllMaterialsResult
                .thenApplyAsync(result -> doStage(Stage.Foundation, result), pool);

        CompletableFuture<String> cfWalls = cfFoundation
                .thenApplyAsync(result -> doStage(Stage.Walls, result), pool);

        CompletableFuture<String> cfRoof = cfWalls
                .thenApplyAsync(result -> doStage(Stage.Roof, result), pool);

        cfRoof.handleAsync((result, ex) -> {
            System.out.println("House was" +
                    (isSuccess(result) ? "" : " not") +
                    " built in " + stopWatch.getTime() / 1000. + " sec");
            if (result == null) {
                System.out.println("Because: " + ex.getMessage());
                return ex.getMessage();
            } else {
                return result;
            }
        }, pool);
        System.out.println("Out!!!!!");
    }

    private static void demo_CompletableFuture7() {
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> getData(), pool);

        CompletableFuture<SomeClass> cf2 = cf1
                .thenApplyAsync(result -> doSomething(result), pool);

        CompletableFuture<AnotherClass> cf3 = cf2
                .thenApplyAsync(result -> doMore(result), pool);

        CompletableFuture<YetAnotherClass> cf4 = cf3
                .thenApplyAsync(result -> doSomethingElse(result), pool);

        CompletableFuture<Integer> cf5 = cf4
                .thenApplyAsync(result -> doFinalProcessing(result), pool);

        cf5.handleAsync((result, ex) -> {
            System.out.println("And the answer is " + result);
            if (result == null) {
                System.out.println("Because: " + ex.getMessage());
                return -1;
            } else {
                return result;
            }
        }, pool);

        CompletableFuture<AnotherType> cf3a = cf2
                .thenApplyAsync(result -> doSomethingAlternative(result), pool);

        CompletableFuture<YetAnotherType> cf4a = cf3a
                .thenApplyAsync(result -> doMoreAltProcessing(result), pool);

        CompletableFuture<Integer> cf5a = cf4a
                .thenApplyAsync(result -> doFinalAltProcessing(result), pool);

        cf5a.handleAsync((result, ex) -> myHandler(result, ex), pool);

        System.out.println("Out!!!!!");
    }

    private static int myHandler(Integer result, Throwable ex) {
        System.out.println("And the answer is " + result);
        if (result == null) {
            System.out.println("Because: " + ex.getMessage());
            return -1;
        } else {
            return result;
        }
    }

    private static void demo_CompletableFuture8() {
        ExecutorService pool = Executors.newCachedThreadPool();

        CompletableFuture<SomeClass> completableFuture =
                CompletableFuture.supplyAsync(() -> getData(), pool)
                        .thenApplyAsync(result -> doSomething(result), pool);

        completableFuture
                .thenApplyAsync(result -> doMore(result), pool)
                .thenApplyAsync(result -> doSomethingElse(result), pool)
                .thenApplyAsync(result -> doFinalProcessing(result), pool)
                .handleAsync((result, ex) -> myHandler(result, ex), pool);

        completableFuture
                .thenApplyAsync(result -> doSomethingAlternative(result), pool)
                .thenApplyAsync(result -> doMoreAltProcessing(result), pool)
                .thenApplyAsync(result -> doFinalAltProcessing(result), pool)
                .handleAsync((result, ex) -> myHandler(result, ex), pool);

        System.out.println("Out!!!!!");
    }

    private static void demo_CompletableFuture9() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> doFinalProcessing(new YetAnotherClass()), pool)
                //.thenApplyAsync(i -> i + 1, pool)
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> i + 1, pool) //Wrapping up the async call, for example
                        .thenApplyAsync(j -> j + 1, pool))
                .handleAsync((result, ex) -> myHandler(result, ex), pool);

        System.out.println("Out!!!!!");
    }

    private static void demo_CompletableFuture10() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CompletableFuture.supplyAsync(() -> "Hello,", pool)
                .thenApplyAsync(s -> s + " World", pool)
                .thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Done!"));

        System.out.println("Out!!!!!");
    }

    private static void demo_CompletableFuture11() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> doFinalProcessing(new YetAnotherClass()), pool);
        cf.complete(43); // sets return value if not yet completed and completes

        try {
            System.out.println(cf.get()); //does not block
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        System.out.println("Out!!!!!");
    }

    private static boolean isSuccess(String result) {
        return SUCCESS.equals(result);
    }

    private static String getResult(String result1, String result2) {
        if (isSuccess(result1)) {
            if (isSuccess(result2)) {
                return SUCCESS;
            } else {
                return result2;
            }
        } else {
            return result1;
        }
    }

    private static String getSuccessOrFirstFailure(List<Future<String>> futures) {
        String result = "";
        int count = 0;
        try {
            while (count < futures.size()) {
                for (Future<String> future : futures) {
                    if (future.isDone()) {
                        result = getResult(future);
                        if (!isSuccess(result)) {
                            break;
                        }
                        count++;
                    } else {
                        sleepSec(1);
                    }
                }
                if (!isSuccess(result)) {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static String getResult(Future<String> future) {
        String result = null;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void sleepSec(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
        }
    }

    private static String getData() {
        System.out.println("Getting data from some source...");
        sleepSec(1);
        return "Some input";
    }

    private static SomeClass doSomething(String input) {
        System.out.println("Doing something and returning SomeClass object...");
        sleepSec(1);
        return new SomeClass();
    }

    private static AnotherClass doMore(SomeClass input) {
        System.out.println("Doing more of something and returning AnotherClass object...");
        sleepSec(1);
        return new AnotherClass();
    }

    private static YetAnotherClass doSomethingElse(AnotherClass input) {
        System.out.println("Doing something else and returning YetAnotherClass object...");
        sleepSec(1);
        return new YetAnotherClass();
    }

    private static int doFinalProcessing(YetAnotherClass input) {
        System.out.println("Processing and finally returning result...");
        sleepSec(1);
        return 42;
    }

    private static AnotherType doSomethingAlternative(SomeClass input) {
        System.out.println("Doing something alternative and returning AnotherType object...");
        sleepSec(1);
        return new AnotherType();
    }

    private static YetAnotherType doMoreAltProcessing(AnotherType input) {
        System.out.println("Doing more alternative and returning YetAnotherType object...");
        sleepSec(1);
        return new YetAnotherType();
    }

    private static int doFinalAltProcessing(YetAnotherType input) {
        System.out.println("Alternative processing and finally returning result...");
        sleepSec(1);
        return 43;
    }

}


enum Stage {
    FoundationMaterials,
    WallsMaterials,
    RoofMaterials,
    Foundation,
    Walls,
    Roof
}

class SomeClass {

}

class AnotherClass {

}

class YetAnotherClass {

}

class AnotherType {

}

class YetAnotherType {

}
