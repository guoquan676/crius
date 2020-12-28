package com.pbkj.crius.test.cglibproxy;

import org.springframework.cglib.core.DebuggingClassWriter;

/**
 * @author GZQ
 * @description $
 * @date 2020/12/22 10:03
 **/
public class Test2 {
    public static void main(String[] args) {
        AliSmsService aliSmsService = (AliSmsService) CglibProxyFactory.getProxy(AliSmsService.class);
        aliSmsService.send("java");
    }
}