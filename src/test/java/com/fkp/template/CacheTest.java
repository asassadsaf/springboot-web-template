package com.fkp.template;

import com.fkp.template.modules.statistic.entity.KeyStateReport;
import com.github.benmanes.caffeine.cache.Cache;
import com.sun.jmx.remote.internal.ArrayQueue;
import lombok.SneakyThrows;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/12/23 19:16
 */
@SpringBootTest
public class CacheTest {

    @Autowired
    private Cache<String, KeyStateReport> keyStateReportCache;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Test
    void testSize(){
        System.out.println(keyStateReportCache.asMap().size());
        keyStateReportCache.put("a", KeyStateReport.builder().id("a").build());
        keyStateReportCache.put("b", KeyStateReport.builder().id("b").build());
        Collection<@NonNull KeyStateReport> values = keyStateReportCache.asMap().values();
        Set<Map.Entry<@NonNull String, @NonNull KeyStateReport>> entries = keyStateReportCache.asMap().entrySet();
        ArrayList<@NonNull KeyStateReport> keyStateReports = new ArrayList<>(values);
        Set<@NonNull String> set = keyStateReportCache.asMap().keySet();
        Map<@NonNull String, @NonNull KeyStateReport> allPresent = keyStateReportCache.getAllPresent(set);
        System.out.println(set.size());
        System.out.println(allPresent.size());
        System.out.println(values.size());
        System.out.println(entries.size());
        System.out.println(keyStateReports.size());
        keyStateReportCache.invalidateAll();
        System.out.println(set.size());
        System.out.println(allPresent.size());
        System.out.println(values.size());
        System.out.println(entries.size());
        System.out.println(keyStateReports.size());
        keyStateReportCache.put("c", KeyStateReport.builder().id("c").build());
        System.out.println(set.size());
        System.out.println(allPresent.size());
        System.out.println(values.size());
        System.out.println(entries.size());
        System.out.println(keyStateReports.size());

    }

    @Test
    @SneakyThrows
    void testQueue(){

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                while (true){
                    String string = UUID.randomUUID().toString();
                    if(!queue.offer(string)){
                        System.out.println("queue full. ele: " + string);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        scheduler.scheduleAtFixedRate(() -> {
            List<String> list = new ArrayList<>();
            queue.drainTo(list);
            System.out.println(queue.size());
            int batchSize = 100;
            for (int i = 0; i < list.size(); i+=batchSize) {
                List<String> subList = list.subList(i, Math.min(i + batchSize, list.size()));
                System.out.println(subList.size());
            }
        }, 1000);

        Thread thread = Thread.currentThread();
        thread.join();
    }

    @Test
    void testQueue2(){
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(2);
        List<String> list = Arrays.asList("a", "b", "c");
        try {
            boolean b = queue.addAll(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        String ele = null;
        while ((ele = queue.poll()) != null){
            System.out.println(ele);
        }
    }
}
