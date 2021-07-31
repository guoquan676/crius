package com.gzq.crius.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author guozhenquan
 * @version 1.0
 * @description: 日志实战代码片段
 * @date 2021/7/31 11:58
 */
@Slf4j
public class LogActual {
    public static void main(String[] args) throws InterruptedException {
        stopWatchActual();
    }

    private static void stopWatchActual() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("第一段代码开始");
        /*执行的代码*/
        Thread.sleep(1000);
        stopWatch.stop();
        stopWatch.start("第二段代码开始");
        Thread.sleep(1000);
        /*执行的代码*/
        stopWatch.stop();
        log.info("所有执行时间:{}",stopWatch.prettyPrint());
    }
}
