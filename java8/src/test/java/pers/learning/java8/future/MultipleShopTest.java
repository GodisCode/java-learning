package pers.learning.java8.future;

import org.junit.Test;
import pers.learning.java8.util.TimeUtils;

/**
 * @author zhengxiaohui
 * @email zhengxiaohui@shenzhijie.com
 * @date 2018/11/21 14:32
 */
public class MultipleShopTest {
    private MultipleShop multipleShop = new MultipleShop();
    private String product = "Apple";

    @Test
    public void findPrices() {
        TimeUtils.consume(() -> System.out.println(multipleShop.findPrices(product)));
        // 耗时：15005ms
    }

    @Test
    public void findPricesInParallel() {
        TimeUtils.consume(() -> System.out.println(multipleShop.findPricesInParallel(product)));
        // 耗时：3014ms
    }

    @Test
    public void findPricesConcurrently() {
        TimeUtils.consume(() -> System.out.println(multipleShop.findPricesConcurrently(product)));
        // 耗时：3005ms
    }

    @Test
    public void findPricesWithExecutorConcurrently() {
        TimeUtils.consume(() -> System.out.println(multipleShop.findPricesWithExecutorConcurrently(product)));
        // 耗时：1005ms
    }

    // 打印线程数
    public static void printThreadNum() {
        System.out.println(" process thread number：" +Runtime.getRuntime().availableProcessors());
    }
}