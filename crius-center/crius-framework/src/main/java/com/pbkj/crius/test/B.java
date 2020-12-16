package com.pbkj.crius.test;

/**
 * @author GZQ
 * @description $
 * @date 2020/12/9 14:28
 **/
public class B {
    public static void testSafe(StringBuilder sb,String a1) {
        for (long a = 0; a < 100; a++) {
            sb.append(a1);
        }
        System.out.println(sb);
    }

    public static void main(String[] args) throws InterruptedException {
        StringBuilder stringBuilder1 = new StringBuilder();
        new Thread(() -> {
            testSafe(stringBuilder1,"a");
        },"thread1").start();
        new Thread(() -> {
            testSafe(stringBuilder1,"b");
        },"thread2").start();
    }
}