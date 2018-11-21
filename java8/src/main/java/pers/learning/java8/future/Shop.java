package pers.learning.java8.future;

import pers.learning.java8.util.TimeUtils;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author zhengxiaohui
 * @email zhengxiaohui@shenzhijie.com
 * @date 2018/11/21 11:06
 */
public class Shop {
    private static final Random random = new Random();

    private String name;

    public Shop() {
    }

    public Shop(String name) {
        this.name = name;
    }

    private double calculatePrice(String product) {
        TimeUtils.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 同步任务
     * @param product
     * @return
     */
    public double getPrice(String product) {
        return calculatePrice(product);
    }

    /**
     * 通过直接new CompletableFuture的方式创建异步任务
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                // 正常执行
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception e) {
                // 为了让外部线程获取异常，用completeExceptionally抛出结果异常
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }

    /**
     * 通过工厂方法的方式创建异步任务。
     * 等同于{@link #getPriceAsync(String)}，也有带异常处理
     * @param product
     * @return
     */
    public Future<Double> producePriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    /**
     * 通过工厂方法的方式创建异步任务，并指定自定义线程池
     * @param product
     * @return
     */
    public Future<Double> producePriceAsyncWithExecutor(String product) {
        final Executor executor = Executors.newFixedThreadPool(
                3,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        // 设置每个线程都为守护线程，防止程序运行过程中将线程回收掉，等程序运行结束后再自动回收
                        t.setDaemon(true);
                        return t;
                    }
                }
        );
        return CompletableFuture.supplyAsync(() -> calculatePrice(product), executor);
    }

    public static Random getRandom() {
        return random;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
