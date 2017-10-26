package com.packt.java9hp.ch11_newapis.walk;

import java.util.Arrays;
import java.util.Set;

public class Clazz03 {
    public void method(){
        //System.out.println("Clazz03 was called by "+new Throwable().getStackTrace()[1].getClassName());
        //System.out.println("Clazz03 was called by "+Thread.currentThread().getStackTrace()[2].getClassName());
        System.out.println("Clazz03 was called by "+StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getSimpleName());

/*
        Arrays.stream(Thread.currentThread().getStackTrace()).forEach(ste -> {
            System.out.println();
            System.out.println("ste="+ste);
            System.out.println("ste.getFileName()="+ste.getFileName());
            System.out.println("ste.getClassName()="+ste.getClassName());
            System.out.println("ste.getMethodName()="+ste.getMethodName());
            System.out.println("ste.getLineNumber()="+ste.getLineNumber());
        });
        System.out.println();

        Arrays.stream(new Throwable().getStackTrace()).forEach(ste -> {
            System.out.println();
            System.out.println("ste="+ste);
            System.out.println("ste.getFileName()="+ste.getFileName());
            System.out.println("ste.getMethodName()="+ste.getMethodName());
            System.out.println("ste.getLineNumber()="+ste.getLineNumber());
        });
*/

        StackWalker.getInstance().forEach(System.out::println);
        System.out.println();
        StackWalker.getInstance().walk(sf -> { sf.forEach(System.out::println); return null; });

        StackWalker stackWalker = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 10);
        stackWalker.forEach(sf -> {
            System.out.println();
            System.out.println("sf="+sf);
            System.out.println("sf.getFileName()="+sf.getFileName());
            System.out.println("sf.getClass()="+sf.getClass());
            System.out.println("sf.getMethodName()="+sf.getMethodName());
            System.out.println("sf.getLineNumber()="+sf.getLineNumber());
            System.out.println("sf.getByteCodeIndex()="+sf.getByteCodeIndex());
            System.out.println("sf.getClassName()="+sf.getClassName());
            System.out.println("sf.getDeclaringClass()="+sf.getDeclaringClass());
            System.out.println("sf.toStackTraceElement()="+sf.toStackTraceElement());
        });


    }
}
