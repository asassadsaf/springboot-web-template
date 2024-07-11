package com.fkp.template;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        QuestionBank questionBank = new QuestionBank();
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> total = new HashMap<>();
        int count = 0;
        while (count < 1000000) {
            // 随机选择一道题目
            count++;
            String selectedQuestion = questionBank.selectRandomQuestion();
            System.out.println("题目：" + selectedQuestion);
            if (total.containsKey(selectedQuestion)) {
                total.put(selectedQuestion, total.get(selectedQuestion) + 1);
            }else {
                total.put(selectedQuestion, 0);
            }
            // 获取用户输入的答案
            System.out.print("请输入答案：");
            String userAnswer = "答案3";

            // 判断答案是否正确
            String correctAnswer = questionBank.getCorrectAnswer(selectedQuestion);
            boolean isCorrect = userAnswer.equals(correctAnswer);
            if (isCorrect) {
                System.out.println("回答正确！");
            } else {
                System.out.println("回答错误！");
            }

            // 根据答题结果调整题目的出现概率
            questionBank.checkAnswer(selectedQuestion, userAnswer, isCorrect);

            // 继续或结束
//            System.out.print("继续答题？(yes/no): ");
//            String continueChoice = scanner.nextLine();
//            if (!continueChoice.equalsIgnoreCase("yes")) {
//                break;
//            }
        }
        System.out.println(total);
//        System.out.println("答题结束，谢谢参与！");
//        scanner.close();
    }
}
