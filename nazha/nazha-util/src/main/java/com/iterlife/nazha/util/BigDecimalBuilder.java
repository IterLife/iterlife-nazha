package com.iterlife.nazha.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Desc: BigDecimal 包装工具类
 * @Author:Lu Jie
 * @Date: 2019-04-11 10:05:44
 **/
public class BigDecimalBuilder {


    //默认小数点位数
    private static final int DEFAULT_BIGDECIMAL_SCALE = 6;
    //丢弃位处理方式，参考DecimalFormat API
    private static final RoundingMode DEFAULT_BIGDECIMAL_ROUNDINGMODE = RoundingMode.HALF_UP;

    private BigDecimal value;
    private int scale;
    private RoundingMode roundMode;

    /**
     * 无参数构造函数
     */
    public BigDecimalBuilder() {
        this(new BigDecimal(0).setScale(DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_SCALE));
    }

    /**
     * 构造函数
     */
    public BigDecimalBuilder(String value) {
        this(new BigDecimal(value).setScale(DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_SCALE));
    }

    /**
     * 构造函数
     */
    public BigDecimalBuilder(BigDecimal value) {
        this(value, DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * 构造函数
     */
    public BigDecimalBuilder(Integer value) {
        this(new BigDecimal(value == null ? 0 : value),
                DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * 构造函数
     *
     * @param value     b
     * @param scale
     * @param roundMode
     */
    public BigDecimalBuilder(BigDecimal value, int scale, RoundingMode roundMode) {
        this.value = value;
        this.scale = scale;
        this.roundMode = roundMode;
    }

    /**
     * @return 当前BigDecimal取值
     */
    public BigDecimal valueof() {
        return this.value.setScale(DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * @return 当前Integerl取值, 即取BigDecimal的整数部分
     */
    public Integer intValue() {
        return value.setScale(0, RoundingMode.HALF_UP).intValue();
    }

    /**
     * @param quantile 小数点移位,向左移动取负数，向右移动取正数
     * @return 位移后Integer取值，小数部分舍去
     * @desc 场景:单位互换，如货币元和货币分互转
     **/
    public Integer intValue(int quantile) {
        BigDecimal temp = value;
        return quantile > 0 ? temp.movePointRight(Math.abs(quantile)).intValue() : temp.movePointLeft(Math.abs(quantile)).intValue();
    }

    /**
     * @param intVal 加数
     * @return 被加数与Integer类型加数进行加法计算
     */
    public BigDecimalBuilder add(Integer intVal) {
        intVal = (intVal == null ? 0 : intVal);
        BigDecimal val = new BigDecimal(intVal);
        val.setScale(this.scale, this.roundMode);
        value = value.add(val);
        return this;
    }

    /**
     * @param val 加数
     * @return 被加数与BigDecimal类型加数进行加法计算
     */
    public BigDecimalBuilder add(BigDecimal val) {
        val = (val == null ? new BigDecimal(0) : val);
        value = value.add(val);
        return this;
    }

    /**
     * @param intVal 减数
     * @return 被加数与Integer类型减数进行减法计算
     */
    public BigDecimalBuilder subtract(Integer intVal) {
        intVal = (intVal == null ? 0 : intVal);
        BigDecimal val = new BigDecimal(intVal);
        val.setScale(this.scale, this.roundMode);
        value = value.subtract(val);
        return this;
    }

    /**
     * @param val 减数
     * @return 被加数与BigDecimal类型减数进行减法计算
     */
    public BigDecimalBuilder subtract(BigDecimal val) {
        val = (val == null ? new BigDecimal(0) : val);
        value = value.subtract(val);
        return this;
    }

    /**
     * @param intVal 乘数
     * @return 被加数与Integer类型乘数进行乘法计算
     */
    public BigDecimalBuilder multiply(Integer intVal) {
        intVal = (intVal == null ? 0 : intVal);
        BigDecimal val = new BigDecimal(intVal);
        val.setScale(this.scale, this.roundMode);
        value = value.multiply(val);
        return this;
    }

    /**
     * @param val 乘数
     * @return 被加数与BigDecimal类型乘数进行减法计算
     */
    public BigDecimalBuilder multiply(BigDecimal val) {
        val = (val == null ? new BigDecimal(0) : val);
        value = value.multiply(val);
        return this;
    }

    /**
     * @param intVal 除数
     * @return 被加数与BigDecimal类型除数进行除法计算
     */
    public BigDecimalBuilder divide(Integer intVal) {
        intVal = (intVal == null ? 0 : intVal);
        BigDecimal val = new BigDecimal(intVal);
        val.setScale(this.scale, this.roundMode);
        value = value.divide(val, DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
        return this;
    }

    /**
     * @param val 除数
     * @return 被加数与BigDecimal类型乘数进行除法计算
     */
    public BigDecimalBuilder divide(BigDecimal val) {
        val = (val == null ? new BigDecimal(1) : val);
        value = value.divide(val, DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
        return this;
    }


    public static void main(String args[]) {
        /**计算:9.00*2-5+2.20=15.20*/
        System.out.println(new BigDecimalBuilder(new BigDecimal("9.00")).multiply(2).subtract(5).add(new BigDecimal("2.20")).valueof());
    }
}
