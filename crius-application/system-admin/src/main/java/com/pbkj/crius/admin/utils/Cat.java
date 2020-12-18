package com.pbkj.crius.admin.utils;

/**
 * @author GZQ
 * @description $
 * @date 2020/10/12 11:26
 **/
public class Cat {
    static{
        System.out.println("=======================");
    }
    public int getC() {
        return 1+1;
    }
    public static void main(String[] args) {
        System.out.println("123");
        Thread thread = new Thread("thread1"){
            @Override
            public void run() {
                while (true){}
            }
        };
        thread.start();
    }
}