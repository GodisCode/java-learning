package pers.learning.java8.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * @author zhengxiaohui
 * @email zhengxiaohui@shenzhijie.com
 * @date 2018/11/21 13:49
 */
public class MultipleShop {
    private List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("GoodIdea"),
            new Shop("VeryGood"),
            new Shop("Excellent"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("GoodIdea"),
            new Shop("VeryGood"),
            new Shop("Excellent"),
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("GoodIdea"),
            new Shop("VeryGood"),
            new Shop("Excellent")
    );

    /**
     * 同步处理
     * @param product
     * @return
     */
    public List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    /**
     * 并行流处理
     * @param product
     * @return
     */
    public List<String> findPricesInParallel(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    /**
     * 并发处理。
     *
     * <p>注意：以下代码用两个Stream流来分开处理的原因是：如果用一个stream流作处理，则就会变成shops的一个元素
     * 处理完再处理下一个元素，每个元素都是先建立异步任务再获取结果，接着再处理下一个元素，从而导致变成顺序处理。
     * 所以应该分成两个Stream流来实现，第一个流把shops所有元素的异步任务建立并执行，第二流再获取所有异步任务的
     * 结果。
     *
     * @param product
     * @return
     */
    public List<String> findPricesConcurrently(String product) {
        List<CompletableFuture<String>> completableFutureList = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))))
                .collect(Collectors.toList());

        return completableFutureList.stream()
                .map(CompletableFuture::join)   // 利用join方法一个接一个获取结果，不用处理异常
                .collect(Collectors.toList());
    }


    /**
     * 并发处理配合自定义线程池
     * @param product
     * @return
     */
    public List<String> findPricesWithExecutorConcurrently(String product) {
        final Executor executor = Executors.newFixedThreadPool(
                Math.min(shops.size(), 100),
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

        List<CompletableFuture<String>> completableFutureList = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                            () -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)),
                            executor
                        )
                )
                .collect(Collectors.toList());

        return completableFutureList.stream()
                .map(CompletableFuture::join)   // 利用join方法一个接一个获取结果，不用处理异常
                .collect(Collectors.toList());
    }
}
