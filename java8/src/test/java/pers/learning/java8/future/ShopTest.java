package pers.learning.java8.future;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * @author zhengxiaohui
 * @email zhengxiaohui@shenzhijie.com
 * @date 2018/11/21 11:16
 */
public class ShopTest {

    @Test
    public void getPriceAsync() throws Exception {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Invocation returned after " + invocationTime + " ms");

        // 执行更多其它任务
        System.out.println("执行更多其它任务");

        // 最后在需要结果时，再获取future结果
        double price = futurePrice.get();   // 若异步任务未执行完，则阻塞等待直到结果返回
        System.out.printf("商品价格为 %.2f%n", price);

        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " ms");
    }

    @Test
    public void producePriceAsync() {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.producePriceAsync("my favorite product");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Invocation returned after " + invocationTime + " ms");

        // 执行更多其它任务
        System.out.println("执行更多其它任务");

        // 最后在需要结果时，再获取future结果
        try {
            double price = futurePrice.get();   // 若异步任务未执行完，则阻塞等待直到结果返回
            System.out.printf("商品价格为 %.2f%n", price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // ExecutionException可以获取异步任务线程的异常
            e.printStackTrace();
        }

        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " ms");
    }

    @Test
    public void producePriceAsyncWithExecutor() {

    }
}