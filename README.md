## $5 Tech Unlocked 2021!
[Buy and download this Book for only $5 on PacktPub.com](https://www.packtpub.com/product/java-9-high-performance/9781787120785)
-----
*If you have read this book, please leave a review on [Amazon.com](https://www.amazon.com/gp/product/1787120783).     Potential readers can then use your unbiased opinion to help them make purchase decisions. Thank you. The $5 campaign         runs from __December 15th 2020__ to __January 13th 2021.__*

# Java 9 High Performance
This is the code repository for [Java 9 High Performance](https://www.packtpub.com/application-development/java-9-high-performance?utm_source=github&utm_medium=repository&utm_campaign=9781787120785), published by [Packt](https://www.packtpub.com/?utm_source=github). It contains all the supporting project files necessary to work through the book from start to finish.
## About the Book
Finally, a book that focuses on the practicalities rather than theory of Java application performance tuning. This book will be your one-stop guide to optimize the performance of your Java applications.

We will begin by understanding the new features and APIs of Java 9. You will then be taught the practicalities of Java application performance tuning, how to make the best use of garbage collector, and find out how to optimize code with microbenchmarking. Moving ahead, you will be introduced to multithreading and learning about concurrent programming with Java 9 to build highly concurrent and efficient applications. You will learn how to fine tune your Java code for best results. You will discover techniques on how to benchmark performance and reduce various bottlenecks in your applications. We'll also cover best practices of Java programming that will help you improve the quality of your codebase.

By the end of the book, you will be armed with the knowledge to build and deploy efficient, scalable, and concurrent applications in Java.
## Instructions and Navigation
All of the code is organized into folders. Each folder starts with a number followed by the application name. For example, Chapter02.

Chapters 4, 5, and 8 does not have code files

The code will look like the following:
```
public static void lookupPrime(int start, int end) {

        for (int i = start; i <= end; i++)         
        {
          int primeCheckCounter = 0;
          for(int j=i;(int)Math.sqrt(j)>=1;j --) {
              if(i%j == 0) {
                  primeCheckCounter =  primeCheckCounter + 1;
              }
          }
          if(primeCheckCounter == 2){
              System.out.print(i + " ");
          }
        }
        
    }
```



## Related Products
* [Java 9 Concurrency - High-Level Elements [Video]](https://www.packtpub.com/application-development/java-9-concurrency-high-level-elements-video?utm_source=github&utm_medium=repository&utm_campaign=9781788479639)

* [Java 9 Performance Optimization and Modularization [Video]](https://www.packtpub.com/application-development/java-9-performance-optimization-and-modularization-video?utm_source=github&utm_medium=repository&utm_campaign=9781788398084)

* [Reactive Programming With Java 9](https://www.packtpub.com/application-development/reactive-programming-java-9?utm_source=github&utm_medium=repository&utm_campaign=9781787124233)

