package com.fkp.template;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/10/31 19:28
 */
@Slf4j
public class FastJsonTest {

    @Test
    void testDeSerialize(){
        List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f");
        String listJsonStr = JSON.toJSONString(list);
        System.out.println(listJsonStr);
        // 缺少泛型
        List list2 = JSON.parseObject(listJsonStr, List.class);
        System.out.println(list2);
        // 使用TypeReference反序列化为带泛型的类型
        List<String> list3 = JSON.parseObject(listJsonStr, new TypeReference<List<String>>(){});
        System.out.println(list3);

    }

    @Test
    void testClazz(){
        Class<String> stringClass = String.class;
        System.out.println(stringClass);
        System.out.println(stringClass.getSuperclass());
        System.out.println(stringClass.getGenericSuperclass().getClass());

        System.out.println("===========================================================");

        List stringList = new ArrayList<>();
        Class<? extends List> stringListClass = stringList.getClass();
        System.out.println(stringListClass);
        System.out.println(stringListClass.getSuperclass());
        System.out.println(stringListClass.getGenericSuperclass().getClass());
        ParameterizedType parameterizedType = (ParameterizedType) stringListClass.getGenericSuperclass();
        System.out.println(Arrays.toString(parameterizedType.getActualTypeArguments()));
        System.out.println(parameterizedType.getRawType());
        System.out.println(parameterizedType.getOwnerType());

        System.out.println("===========================================================");

        List<String> stringList2 = new ArrayList<>();
        Class<? extends List> stringListClass2 = stringList2.getClass();
        System.out.println(stringListClass2);
        System.out.println(stringListClass2.getSuperclass());
        System.out.println(stringListClass2.getGenericSuperclass().getClass());
        ParameterizedType parameterizedType2 = (ParameterizedType) stringListClass2.getGenericSuperclass();
        System.out.println(Arrays.toString(parameterizedType2.getActualTypeArguments()));
        System.out.println(parameterizedType2.getRawType());
        System.out.println(parameterizedType2.getOwnerType());

        System.out.println("===========================================================");

        // 创建匿名内部类，myList是ArrayList的子类
        List<String> myList = new ArrayList<String>() {};
        Class<? extends List> myListClass = myList.getClass();
        System.out.println(myListClass);
        System.out.println(myListClass.getSuperclass());
        System.out.println(myListClass.getGenericSuperclass().getClass());
        // 获取myList的父类的泛型Type类型，父类也就是ArrayList
        ParameterizedType parameterizedType3 = (ParameterizedType) myListClass.getGenericSuperclass();
        // 获取泛型类型列表
        System.out.println(Arrays.toString(parameterizedType3.getActualTypeArguments()));
        System.out.println(parameterizedType3.getRawType());
        System.out.println(parameterizedType3.getOwnerType());

        System.out.println("===========================================================");

        // TypeReference的实现和上边创建ArrayList匿名内部类的实现基本一致
        // 创建匿名内部类，typeReference是TypeReference的子类
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {};
        System.out.println(typeReference.getClass());
        System.out.println(typeReference.getClass().getSuperclass());
        // 获取typeReference的父类的泛型Type类型，父类也就是TypeReference
        ParameterizedType typeReferenceParameterizedType = (ParameterizedType) typeReference.getClass().getGenericSuperclass();
        // 获取泛型类型列表
        System.out.println(Arrays.toString(typeReferenceParameterizedType.getActualTypeArguments()));
        // 获取的是就相当于上面列表中的值，具体查看getType源码，和上边获取的过程一样
        System.out.println(typeReference.getType());

        System.out.println("===========================================================");

        Map<Integer, String> map = new HashMap<>();
        Class<? extends Map> mapClass = map.getClass();
        System.out.println(mapClass);
        System.out.println(mapClass.getSuperclass());
        System.out.println(mapClass.getGenericSuperclass().getClass());
        ParameterizedType parameterizedType4 = (ParameterizedType) mapClass.getGenericSuperclass();
        System.out.println(Arrays.toString(parameterizedType4.getActualTypeArguments()));
        System.out.println(parameterizedType4.getRawType());
        System.out.println(parameterizedType4.getOwnerType());
    }


    private <T> T convertObj(String jsonStr, Class<T> clazz){

       return null;
    }
}
