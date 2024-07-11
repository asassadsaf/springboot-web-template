package com.fkp.template;

import java.util.*;

public class QuestionBank {
    private Map<String, String> questions; // 题目和正确答案的映射
    private Map<String, Integer> appearanceCount; // 每道题目的出现次数

    public QuestionBank() {
        questions = new HashMap<>();
        appearanceCount = new HashMap<>();
        // 初始化题库，这里仅做示例，实际题目可以从文件或数据库加载
        for (int i = 1; i <= 1000; i++) {
            questions.put("题目" + i, "答案" + i);
        }

        // 初始化每道题目的出现次数为0
        for (String question : questions.keySet()) {
            appearanceCount.put(question, 0);
        }
    }

    // 随机选择一道题目，考虑题目的出现次数
    public String selectRandomQuestion() {
        List<String> questionList = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        // 将题目和对应的权重加入列表
        for (String question : questions.keySet()) {
            questionList.add(question);
            weights.add(appearanceCount.get(question) + 1); // 加1以防止权重为0的情况
        }

        // 使用加权随机算法选择题目
        Random rand = new Random();
        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomNum = rand.nextInt(totalWeight);
        int cumulativeWeight = 0;
        String selectedQuestion = "";

        for (int i = 0; i < questionList.size(); i++) {
            cumulativeWeight += weights.get(i);
            if (randomNum < cumulativeWeight) {
                selectedQuestion = questionList.get(i);
                break;
            }
        }

        // 增加该题目的出现次数
        int newWeights = appearanceCount.get(selectedQuestion) - 1;
        appearanceCount.put(selectedQuestion, Math.max(newWeights, 0));
        return selectedQuestion;
    }

    // 判断用户答案是否正确，并根据答题表现调整题目的出现概率
    public boolean checkAnswer(String question, String answer, boolean isCorrect) {
        if (questions.containsKey(question)) {
            // 如果答案正确，则降低该题目的出现概率
            if (isCorrect) {
                appearanceCount.put(question, appearanceCount.get(question) - 1);
                if (appearanceCount.get(question) < 0) {
                    appearanceCount.put(question, 0);
                }
            } else { // 如果答案错误，则增加该题目的出现概率
                appearanceCount.put(question, appearanceCount.get(question) + 1);
            }
            return Objects.equals(answer, questions.get(question));
        }
        return false;
    }

    // 获取题目的正确答案
    public String getCorrectAnswer(String question) {
        return questions.get(question);
    }
}
