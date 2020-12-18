package com.pbkj.crius;

/**
 * @author yuanyang
 * @version 1.0
 * @date 2020/12/18 9:22
 */
public class IntegerTest {
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = -129;
        Integer f = -129;
        Long g = 3L;
        Long h = 2L;
        long aa = 3L;
        int bb = 3;
        System.out.println("1.包装类和基础类比较会自动拆箱");
        System.out.println("2.不同基础类比较会隐式转换，向上转换，如：int 与 long 进行比较时，int 会自动进行隐式的类型转换，将int提升为 long 类型");
        System.out.println("3.隐式转换，自动提升高精度类型");
        System.out.println(aa == bb);
        System.out.println(c==d);
        System.out.println("要想获取对象的内存地址应使用System.identityHashCode()方法");
        System.out.println("整型范围在-128~127整型对象被缓存：所以使用==比较地址时，用的是同一个对象");
        System.out.println("e.hashCode:"+System.identityHashCode(e) + " f.hashCode:"+System.identityHashCode(f));
        System.out.println(e==f);
        System.out.println("整型包装类和基础类型");
        System.out.println(e==-129);
        System.out.println("查看valueOf源码，可以看到缓存");
        Integer integer = Integer.valueOf(1);
        System.out.println("自动拆包，怎么印证？");
        System.out.println(c==(a+b));

        System.out.println(c.equals(a+b));
        System.out.println("g==(a+b)" +(g==(a+b)));
        System.out.println(g.equals(a+b));
        System.out.println(g.equals(a+h));
        System.out.println("------");
        System.out.println(g.equals(3));
        System.out.println(g.equals(3L));
        System.out.println(e.equals(f));
    }
}
