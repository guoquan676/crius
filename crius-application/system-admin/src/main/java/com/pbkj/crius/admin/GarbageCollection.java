package com.pbkj.crius.admin;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GZQ
 * @description 垃圾回收dome$
 * @date 2020/11/4 11:18
 **/
public class GarbageCollection {
    /**
     * -Xmx20m -XX:+PrintGCDetails -verbose:gc
     */
    private static final int _4MB = 4 * 1024 * 1024;

    public static void main(String[] args) {
//        List<byte[]> objects = new ArrayList<>();
//        for (int a = 0; a < 5; a++) {
//            byte[] bytes = new byte[_4MB];
//            objects.add(bytes);
//        }
//        soft();
        weak();
    }

    public static void soft() {
        List<SoftReference<byte[]>> list = new ArrayList<>();
        for (int a = 0; a < 5; a++) {
            SoftReference<byte[]> softReference = new SoftReference<>(new byte[_4MB]);
            list.add(softReference);
        }
        System.out.println("----------------");
        for (SoftReference<byte[]> reference : list) {
            System.out.println(reference.get());
        }
    }

    public static void weak() {
        //list-->WeakReference-->byte[]
        List<WeakReference<byte[]>> list = new ArrayList<>();
        for (int a = 0; a < 5; a++) {
            WeakReference<byte[]> weakReference = new WeakReference<>(new byte[_4MB]);
            list.add(weakReference);
            for ( WeakReference<byte[]> w :list){
                System.out.print(w.get());
            }
            System.out.println();
        }
    }
}