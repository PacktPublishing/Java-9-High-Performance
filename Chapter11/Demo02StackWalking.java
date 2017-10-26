package com.packt.java9hp.ch11_newapis;

import com.packt.java9hp.ch11_newapis.walk.Clazz01;

import java.util.Arrays;

public class Demo02StackWalking {
    public static void main(String... args) {
        demo_standard_output1();
        demo_standard_output2();
        demo_reading_stream1();
        demo_reading_stream2();
        demo_walking();
    }

    private static void demo_standard_output1(){
        Thread.currentThread().dumpStack();
    }

    private static void demo_standard_output2(){
        new Throwable().printStackTrace();
    }

    private static void demo_reading_stream1(){
        Arrays.stream(Thread.currentThread().getStackTrace())
                .forEach(System.out::println);
    }

    private static void demo_reading_stream2(){
        Arrays.stream(new Throwable().getStackTrace())
                .forEach(System.out::println);
    }

    private static void demo_walking(){
        System.out.println("demo() is called by " +
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass().getSimpleName());
        new Clazz01().method();
    }

}


