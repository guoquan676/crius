package com.gzq.crius.concurrent.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author guozhenquan
 * @version 1.0
 * @description: 多线程工作类
 * @date 2021/7/31 14:37
 */
public class Worker implements Callable<List<ResultA>> {

    private final CountDownLatch countDownLatch;

    /**
     * 可以通过构造方法传入一些参数
     */
    public Worker(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public List<ResultA> call() throws Exception {
        try {
            //执行业务
            System.out.println("=================");
        }finally {
            countDownLatch.countDown();
        }
        return null;
    }
}
