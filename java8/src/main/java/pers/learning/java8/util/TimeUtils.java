package pers.learning.java8.util;

/**
 * @author Huy
 * @date 2018/11/21 11:05
 */
public class TimeUtils {
    /**
     * 延时1秒
     */
    public static void delay() {
        delay(1);
    }

    /**
     * 延时seconds秒
     */
    public static void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算耗时
     * @param runnable  需要计算耗时的方法
     */
    public static void consume(Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long time = (System.nanoTime() - start) / 1_000_000;
        System.out.println("耗时：" + time + "ms");
    }
}
