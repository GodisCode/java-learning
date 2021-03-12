package pers.learning.java8.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 将HashMap按key进行排序的方法。此类用函子的方式来写，以支持链式编程
 *
 * <li>TreeMap方法，最简单</li>
 * <li>stream方法，遍历entrySet再插入LinkedHashMap中保存顺序</li>
 */
public class SortMap {
    private Map<String, Integer> codes;

    public static SortMap of(Map<String, Integer> codes) {
        SortMap sortMap = new SortMap();
        sortMap.codes = codes;
        return sortMap;
    }

    public SortMap sortByStream() {
         this.codes = this.codes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));
         return this;
    }

    public SortMap sortByTreeMap() {
        this.codes = new TreeMap<>(codes);
        return this;
    }

    public void print() {
        this.codes.entrySet().forEach(System.out::println);
    }


    public static void main(String[] args) {
        Map<String, Integer> codes = new HashMap<>();
        codes.put("United States", 1);
        codes.put("Germany", 49);
        codes.put("France", 33);
        codes.put("China", 86);
        codes.put("Pakistan", 92);

        SortMap.of(codes).sortByStream().print();
        System.out.println("=================");
        SortMap.of(codes).sortByTreeMap().print();
    }
}
