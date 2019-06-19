package com.iterlife.nazha.util;

import java.util.Arrays;

/**
 * @Desc: Integer 封装工具类
 * @Author:Lu Jie
 * @Date: 2019-04-11 10:05:44
 **/

public class IntegerUtil {


    /**
     * 获取一个Integer对象的取值，过滤null,默认为0
     */
    public static Integer valueof(Integer val) {
        return val == null ? 0 : val;
    }

    /**
     * Integer加法计算，支持多个加数连续相加
     */
    public static Integer add(Integer... vals) {
        return Arrays.stream(vals).mapToInt(val -> valueof(val)).sum();
    }

    public static void main(String args[]) {
        System.out.println(add(1, 2, 3, 4));
    }
}
