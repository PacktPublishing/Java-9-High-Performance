package com.packt.java9hp.ch11_newapis.walk;

public class Clazz02 {

    public void method(){
        System.out.println("Clazz02 was called by " +
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                        .getCallerClass().getSimpleName());
        new Clazz03().method();
    }
}
