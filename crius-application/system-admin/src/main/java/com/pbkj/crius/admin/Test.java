package com.pbkj.crius.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author GZQ
 * @description $
 * @date 2020/10/10 11:09
 **/
public class Test {

    private static Logger logger = LoggerFactory.getLogger(Test.class);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        demo1();
//        demo2();
//        demo3();
//        demo4();
    }

    /**
     * Debug模式,方法进栈演示
     */
    private static void demo4() {
        int b = getB();
        int a = 1+b;
        System.out.println(a);
    }

    private static int getB() {
        Cat cat = new Cat();
        int c = cat.getC();
        return 1 + c;
    }
    /**
     * Callable 方法开启多线程 有点:有返回值
     */
    private static void demo3() throws InterruptedException, ExecutionException {
        Callable callable = new Callable() {
            @Override
            public Object call() {
                job();
                return 1000;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask(callable);
        Thread thread = new Thread(futureTask, "futureTask001");
        thread.start();
        Integer integer = futureTask.get();
        System.out.println(integer);
        job();
    }

    /**
     * Runnable 方法开启多线程,优点:线程开启和执行的任务分离.
     */
    private static void demo2() {
        Runnable runnable = () -> job();
        Thread thread1 = new Thread(runnable, "thread2");
        thread1.start();
        job();
    }

    /**
     * Thread 方法开启多线程
     */
    private static void demo1() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("hello world thread");
            }
        };
        thread.start();
    }

    private static void job() {
        logger.info("thread:[{}]", Thread.currentThread().getName());
    }
}