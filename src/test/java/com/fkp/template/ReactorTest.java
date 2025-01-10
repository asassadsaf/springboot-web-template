package com.fkp.template;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2025/1/9 10:01
 */
public class ReactorTest {

    @Test
    void testHello(){
        Mono.just("FKP")
                .map(x -> x.toLowerCase(Locale.ROOT))
                .map(x2 -> "Hello " + x2 + "!")
                .subscribe(System.out::println);
    }

    // ------------------------------------------创建Flux操作---------------------------------------------------------

    /**
     * 通过对象创建反应式流
     */
    @Test
    void testCreateByObject(){
        // 通过字符串对象来创建Flux
        Flux<String> flux = Flux.just("Apple", "Orange", "Banana", "Grape");
        // 添加订阅者，使数据开始流动并被消费
        flux.subscribe(System.out::println);

        // 通过StepVerifier来测试Flux，测试数据流的顺序，若不匹配抛出异常：java.lang.AssertionError: expectation "expectNext(Grape1)" failed (expected value: Grape1; actual value: Grape)
        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Banana")
                .expectNext("Grape")
                .verifyComplete();
    }

    /**
     * 通过array,iterable,stream创建flux
     */
    @Test
    void testCreateByArrayIterableStream(){
        // 通过数组创建Flux
        String[] strArray = new String[]{"Apple", "Orange", "Banana", "Grape"};
        Flux.fromArray(strArray)
                .subscribe(System.out::println);

        System.out.println("----------------------------------------------------------");

        // 通过集合创建Flux
        Iterable<String> iterator = Arrays.asList("Apple", "Orange", "Banana", "Grape");
        Flux.fromIterable(iterator)
                .subscribe(System.out::println);

        System.out.println("----------------------------------------------------------");

        // 通过java stream创建Flux
        Stream<String> stream = Stream.of("Apple", "Orange", "Banana", "Grape");
        Flux.fromStream(stream)
                .subscribe(System.out::println);
    }

    /**
     * 创建计数器的flux；基于时间间隔的flux
     */
    @SneakyThrows
    @Test
    void testCreateByRangeInterval(){
        // 创建计数器的flux，发布从0到4的整数
        Flux.range(0, 5)
                .subscribe(System.out::println);

        System.out.println("----------------------------------------------------------");

        // 基于时间间隔的flux，每1秒发布一个值，take(5)将结果限制为前五个条目
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1))
                .take(5);

        StepVerifier.create(flux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();

        // 使进程不退出，观察基于时间的flux，或使用StepVerifier
//        Thread.currentThread().join();
    }

    /**
     * 合并两个flux，合并后顺序为123456
     */
    @SneakyThrows
    @Test
    void testCreateByMerge(){
        // 使用delayElements()方法来减慢它们的速度，使它们每 500 毫秒发布一个条目
        Flux<String> flux = Flux.just("1", "3", "5")
                .delayElements(Duration.ofMillis(500));

        // 使用delaySubscription()方法，使它在订阅后250毫秒才开始发布数据
        Flux<String> flux2 = Flux.just("2", "4", "6")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        //合并两个flux，当由消费者订阅mergeFlux时相当于同时订阅了flux和flux2
        Flux<String> mergeFlux = flux.mergeWith(flux2);

        StepVerifier.create(mergeFlux)
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .expectNext("4")
                .expectNext("5")
                .expectNext("6")
                .verifyComplete();
    }

    /**
     * 使用zip方法将两个flux压缩为一个包含元组的flux
     * zip()方法是一个静态的创建操作。创建出来的 Flux 在角色名和角色喜欢的食物之间会完美对齐。
     * 从这个合并后的 FIux 发出的每个条目都是一个Tuple2（一个容纳两个其他对象的容器对象）的实例，其中包含了来自每个源 Flux 的数据项，并保持着它们发布的顺序
     * 如果你不想使用 Tuple2，而想使用其他类型，可以为 zip 方法提供一个合并函数来生成你想要的任何对象，合并函数会传入这两个数据项
     */
    @Test
    void testCreateByZip(){
        Flux<String> flux = Flux.just("1", "3", "5");
        Flux<String> flux2 = Flux.just("2", "4", "6");

        // 默认zip生成一个二元组的flux
        Flux<Tuple2<String, String>> zipFlux = Flux.zip(flux, flux2);

        StepVerifier.create(zipFlux)
                .expectNextMatches(p -> p.getT1().equals("1") && p.getT2().equals("2"))
                .expectNextMatches(p -> p.getT1().equals("3") && p.getT2().equals("4"))
                .expectNextMatches(p -> p.getT1().equals("5") && p.getT2().equals("6"))
                .verifyComplete();

        // 可以使用Function将其组装为任一状态，例如将其组装为字符串拼接
        Flux<String> zipFunctionFlux = Flux.zip(flux, flux2, (x, y) -> x + "and" + y);
        StepVerifier.create(zipFunctionFlux)
                .expectNextMatches(value -> value.equals("1and2"))
                .expectNextMatches(value -> value.equals("3and4"))
                .expectNextMatches(value -> value.equals("5and6"))
                .verifyComplete();
    }

    /**
     * 将两个flux选择第一个发布的flux并再次发布
     * 创建了一个快速的 Flux 和一个“缓慢”的 Flux(其中“缓慢”意味着它在被订阅后 100毫秒才会发布数据项)。
     * 使用frst操作的相关方法，则会创建一个新的 Flux，只发布第一个发布值的源 Flux 的值
     */
    @Test
    void testCreateByFirst(){
        Flux<String> fastFlux = Flux.just("1", "3", "5");
        Flux<String> slowFLux = Flux.just("2", "4", "6")
                .delaySubscription(Duration.ofMillis(100));
        // 该操作会选择发布来自第一个发布消息的flux的值，也就是fastFlux的值
        Flux<String> firstFlux = Flux.firstWithSignal(slowFLux, fastFlux);
        StepVerifier.create(firstFlux)
                .expectNext("1")
                .expectNext("3")
                .expectNext("5")
                .verifyComplete();
    }

    // ------------------------------------------转换和过滤操作---------------------------------------------------------

    /**
     * 忽略指定数目的前几个数据项；
     *      针对具有多个数据项的 Flux，skip()操作将创建一个新的 Flux，首先跳过指定数量的前几个数据项，然后从源 Flux 中发布剩余的数据项。
     * 跳过一段时间之内出现的数据
     *      使用skip()操作创建了一个在发布值之前等待4秒的 Flux。因为该Flux 是基于一个在发布数据项之间有一秒间隔的 Flux 创建的(使用了 delayElements()操作 )，所以它只会发布出最后两个数据项
     */
    @Test
    void testFilterBySkip(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        // 跳过前3个数据项
        Flux<String> skipedFlux = flux.skip(3);
        StepVerifier.create(skipedFlux)
                .expectNext("4")
                .expectNext("5")
                .verifyComplete();

        Flux<String> delayFlux = Flux
                .just("1", "2", "3", "4", "5")
                //每一个推迟1s发布
                .delayElements(Duration.ofSeconds(1));
        // delayFlux发布数据和时间的关系为,因此跳过前期秒的数据项，剩下4和5
        // 0s----1s----2s----3s----4s----5s
        //         1     2     3     4     5
        // 跳过前四秒发布的数据项
        Flux<String> skipPeriodFlux = delayFlux.skip(Duration.ofSeconds(4));
        StepVerifier.create(skipPeriodFlux)
                .expectNext("4")
                .expectNext("5")
                .verifyComplete();
    }

    /**
     * 保留前几个数据项
     * take() 可以认为是与 skip() 相反的操作。skip() 操作会跳过前几个数据项，而 take() 操作只发布指定数量的前几个数据项(如图所示):
     * 在某段时间内接收并发布与源flux相同的数据项
     * 基于间隔时间而不是数据项数量。它将在某段时间之内接受并发布与源 Flux 相同的数据项，之后 Flux 就会完成。
     */
    @Test
    void testFilterByTake(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        // 跳过前3个数据项
        Flux<String> takedFlux = flux.take(3);
        StepVerifier.create(takedFlux)
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();

        Flux<String> delayFlux = Flux
                .just("1", "2", "3", "4", "5")
                //每一个推迟1s发布
                .delayElements(Duration.ofSeconds(1));
        // delayFlux发布数据和时间的关系为,因此取前4秒的数据项，剩下1和2和3
        // 0s----1s----2s----3s----4s----5s
        //         1     2     3     4     5
        // 跳过前四秒发布的数据项
        Flux<String> takePeriodFlux = delayFlux.take(Duration.ofSeconds(4));
        StepVerifier.create(takePeriodFlux)
                .expectNext("1")
                .expectNext("2")
                .expectNext("3")
                .verifyComplete();
    }

    /**
     * 通用过滤操作则是 filter()
     * 在使用 filter()操作时，我们需要指定一个 Predicate，用于决定数据项是否能通过 Flux，该操作能够让我们根据任意条件进行选择性地发布消息。
     */
    @Test
    void testFilterByPredicate(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");

        // 过滤出双数
        Flux<String> fliteredFlux = flux.filter(x -> Integer.parseInt(x) % 2 == 0);

        StepVerifier.create(fliteredFlux)
                .expectNext("2")
                .expectNext("4")
                .verifyComplete();
    }

    /**
     * 去重
     * 过滤掉已经接收过的数据项。distinct() 操作生成的 Flux 只会发布源 Flux 中尚未发布过的数据项
     */
    @Test
    void testFilterByDistinct(){
        Flux<String> flux = Flux.just("1", "2", "2", "5", "5");
        Flux<String> distinctFlux = flux.distinct();
        StepVerifier.create(distinctFlux)
                .expectNext("1")
                .expectNext("2")
                .expectNext("5")
                .verifyComplete();
    }

    // ------------------------------------------映射操作---------------------------------------------------------

    /**
     * map() 操作会创建一个新的 Flux，该 Flux 在重新发布它所接收到的每个对象之前会对其执行由给定 Function 预先定义的转换
     */
    @Test
    void testMap(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        Flux<Map<String, String>> mapedFlux = flux.map(x -> Collections.singletonMap("number", x));
        StepVerifier.create(mapedFlux)
                .expectNextMatches(value -> value.equals(Collections.singletonMap("number", "1")))
                .expectNextMatches(value -> value.equals(Collections.singletonMap("number", "2")))
                .expectNextMatches(value -> value.equals(Collections.singletonMap("number", "3")))
                .expectNextMatches(value -> value.equals(Collections.singletonMap("number", "4")))
                .expectNextMatches(value -> value.equals(Collections.singletonMap("number", "5")))
                .verifyComplete();
    }

    /**
     * 将对象转换为新的 Mono 或 Flux。结果形成的 Mono 或 Flux 会扁平化为新的 Flux。当与 subscribeOn() 方法结合使用时， flatMap() 操作可以释放 Reactor 反应式的异步能力。
     */
    @Test
    void testFlatMap(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        // 我们为 flatMap()方法指定了一个 lambda 表达式，将传入的 String转换为 Mono 类型的 String。
        // 然后,map() 操作在这个 Mono 上执行,将 String 转换为Player。
        // 每个内部 Flux 上的 String 被映射到一个 Player 后，再被发布到由 flatMap() 返回的单一 Flux 中，从而完成结果的扁平化。
        Flux<Map<String, String>> flatMapFlux = flux.flatMap(value -> Mono.just(value)
                        .map(x -> Collections.singletonMap("number", x))
                )
                // 如果我们到此为止，那么产生的 Flux 将同样包含 Player 对象，与使用 map() 操作的例子相同，顺序同步地生成。
                // 但是我们对 Mono 做的最后一件事情是调用 subscribeOn() 方法声明每个订阅都应该在并行线程中进行，
                // 因此可以异步并行地执行多个 String 对象的转换操作
                //调用 subscribeOn() 方法时，我们可以使用 Schedulers 中的任意一个静态方法来指定并发模型。
                // 在这个例子中，我们使用了parallel() 方法，它使用来自固定线程池(大小与 CPU 核心数量相同)的工作线程。但是 Scheduler 支持多种并发模型
                .subscribeOn(Schedulers.parallel());

        List<Map<String, String>> list = Arrays.asList(
                Collections.singletonMap("number", "1"),
                Collections.singletonMap("number", "2"),
                Collections.singletonMap("number", "3"),
                Collections.singletonMap("number", "4"),
                Collections.singletonMap("number", "5")
        );
        StepVerifier.create(flatMapFlux)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .expectNextMatches(list::contains)
                .verifyComplete();
    }

    /**
     * 在处理流经 Flux 的数据时,将数据流拆分为小块可能会带来一定的收益。如图所示的 buffer() 操作可以帮助我们实现这个目的
     * 假设给定一个包含多个 String 值的 Flux，其中每个值代表一种水果。我们可以创建个新的由 List 集合组成的 Flux，其中每个 List 包含不超过指定数量的元素
     */
    @Test
    void testFilterBuffer(){
        Loggers.useConsoleLoggers();
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        // String 元素的 Flux被缓冲到一个新的由 List 集合组成的 Flux中，其中每个集合的元素数量不超过 3 个
        Flux<List<String>> bufferFlux = flux.buffer(3);
        StepVerifier.create(bufferFlux)
                .expectNext(Arrays.asList("1", "2", "3"))
                .expectNext(Arrays.asList("4", "5"))
                .verifyComplete();

        // 在组合使用 buffer() 方法和 flatMap() 方法时，这样做可以使每一个 List 集合都可以被并行处理
        flux.buffer(3).flatMap(x -> Flux.fromIterable(x)
                .map(y -> Collections.singletonMap("number", y))
                .subscribeOn(Schedulers.parallel())
                // log() 操作记录了所有的反应式事件，以便观察实际发生了什么事情。
                .log()
        ).subscribe();

        // 使用无参buffer方法将flux的所有数据项收集到一个List中
        Flux<List<String>> allBufferFlux = flux.buffer();

        // 使用collectList方法实现和无参buffer方法相同的效果，只不过是放入Mono中而不是Flux
        Mono<List<String>> collectMono = flux.collectList();
        StepVerifier.create(collectMono)
                .expectNext(Arrays.asList("1", "2", "3", "4", "5"))
                .verifyComplete();
    }

    /**
     * collectMap()操作将会产生一个发布 Map 的 Mono。Map 中会填充一些数据项，数据项的键会由给定的 Function 计算得出
     */
    @Test
    void testFilterCollectMap(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        // key使用function计算得出
        Mono<Map<String, String>> collectMapMono = flux.collectMap(x -> "key" + x);
        StepVerifier.create(collectMapMono)
                .expectNextMatches(value -> value.size() == 5
                        && value.get("key1").equals("1")
                        && value.get("key2").equals("2")
                        && value.get("key3").equals("3")
                        && value.get("key4").equals("4")
                        && value.get("key5").equals("5"))
                .verifyComplete();
    }

    // ------------------------------------------逻辑操作---------------------------------------------------------

    /**
     * all()判断flux上每个元素都满足指定条件
     */
    @Test
    void testLogicAll(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");

        // 判断flux上发布的每个元素都是数值类型的字符串
        Mono<Boolean> allResMono = flux.all(StringUtils::isNumeric);
        StepVerifier.create(allResMono)
                .expectNext(true)
                .verifyComplete();

        // 判断flux上发布的每个元素都是双数
        Mono<Boolean> allResMono2 = flux.all(x -> Integer.parseInt(x) % 2 == 0);
        StepVerifier.create(allResMono2)
                .expectNext(false)
                .verifyComplete();
    }

    /**
     * any()判断flux上任一元素满足指定条件
     */
    @Test
    void testLogicAny(){
        Flux<String> flux = Flux.just("1", "2", "3", "4", "5");
        Mono<Boolean> anyResMono = flux.any(x -> x.equals("1"));
        StepVerifier.create(anyResMono)
                .expectNext(true)
                .verifyComplete();
    }
}
