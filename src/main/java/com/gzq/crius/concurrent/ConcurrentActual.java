package com.gzq.crius.concurrent;

import com.gzq.crius.concurrent.utils.ResultA;
import com.gzq.crius.concurrent.utils.Worker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author guozhenquan
 * @version 1.0
 * @description: 多线程实战代码片段
 * @date 2021/7/31 14:27
 */
@Slf4j
public class ConcurrentActual {

    private static ExecutorService executorService = Executors.newFixedThreadPool(6);

    public static void main(String[] args) {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(2);
            Worker worker = new Worker(countDownLatch);
            Worker worker1 = new Worker(countDownLatch);
            Future<List<ResultA>> submit = executorService.submit(worker);
            Future<List<ResultA>> submit1 = executorService.submit(worker1);
            //阻塞线程直到，所有执行完毕。
            countDownLatch.await();
            List<ResultA> resultAS = submit.get();
            List<ResultA> resultAS1 = submit1.get();
            log.info("结果:[{}]", resultAS);
            log.info("结果:[{}]", resultAS1);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
