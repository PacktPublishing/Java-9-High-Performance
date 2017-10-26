package com.packt.java9hp.ch11_newapis.walk;

public class Clazz01 {

    public void method(){
        System.out.println();
        System.out.println("Clazz01 was called by " +
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass().getSimpleName());
        new Clazz02().method();
    }
}
