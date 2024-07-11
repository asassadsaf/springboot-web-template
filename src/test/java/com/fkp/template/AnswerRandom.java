package com.fkp.template;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author fkp
 * @version 1.0
 * @description
 * @date 2024/7/12 0:01
 */
public class AnswerRandom {

    @Test
    void test(){
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int j = random.nextInt(4);
            System.out.println(j);
        }

    }

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        list.add(Collections.singletonMap("q1", "A"));
        list.add(Collections.singletonMap("q2", "A"));
        list.add(Collections.singletonMap("q3", "C"));
        list.add(Collections.singletonMap("q4", "D"));

        Scanner scanner = new Scanner(System.in);

        while (true){

            Random random = new Random();
            int j = random.nextInt(4);
            Map<String, String> map = list.get(j);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey());
                String next = scanner.next();
                if("q".equals(next)){
                    break;
                }
                if(next.equals(entry.getValue())){
                    System.out.println("true");
                }
            }
        }
    }
}
