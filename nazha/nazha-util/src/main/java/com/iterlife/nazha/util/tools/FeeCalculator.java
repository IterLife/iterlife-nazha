package com.iterlife.nazha.util.tools;


import com.iterlife.nazha.util.BigDecimalBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author lujie
 * @Date 2018/7/16 下午4:28,updated 2019-04-22 13:44:22
 */
public class FeeCalculator {


    //一年中的月份数字常量
    private static final Integer MONTHS_OF_YEARS = 12;
    //默认小数点位数
    private static final int DEFAULT_BIGDECIMAL_SCALE = 15;
    //Int 类型小数点位数
    private static final int INT_TYPE_BIGDECIMAL_SCALE = 0;
    //丢弃位处理方式，参考DecimalFormat API
    private static final RoundingMode DEFAULT_BIGDECIMAL_ROUNDINGMODE = RoundingMode.HALF_UP;

    /**
     * excel中PMT函数,计算贷款分期每期固定付款额或固定利率（pv等于-1时，返回的即是固定利率）
     * 官方说明：https://support.office.com/zh-cn/article/PMT-%E5%87%BD%E6%95%B0-0214DA64-9A63-4996-BC20-214433FA6441
     *
     * @param rate 各期利率（月利率）
     * @param nper 总期数
     * @param pv   贷款金额
     * @return
     */
    public static BigDecimal PMT(BigDecimal rate, BigDecimal nper, BigDecimal pv) {

        BigDecimal v = rate.add(BigDecimal.valueOf(1));

        BigDecimal t = nper.negate();

        BigDecimal temp = pv.multiply(rate).negate();

        BigDecimal t2 = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(Math.pow(v.doubleValue(), t.doubleValue())));
        return temp.divide(t2, DEFAULT_BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * excel中PV函数，与PMT函数相反，用于根据固定利率计算贷款或投资的现值
     * 官方说明：https://support.office.com/zh-cn/article/pv-%E5%87%BD%E6%95%B0-23879d31-0e02-4321-be01-da16e8168cbd
     *
     * @param rate 各期利率
     * @param nper 总期数
     * @param pmt  每期的付款金额或利率
     * @return
     */
    public static BigDecimal PV(BigDecimal rate, BigDecimal nper, BigDecimal pmt) {

        BigDecimal v = rate.add(BigDecimal.valueOf(1));

        BigDecimal t = nper.negate();

        BigDecimal t2 = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(Math.pow(v.doubleValue(), t.doubleValue())));

        return pmt.negate().divide(rate, DEFAULT_BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).multiply(t2);
    }

    /**
     * 根据年化费率计算对应等本等息月手续费费率
     * 现用年化费率根据PMT函数计算出总应还金额，然后在计算等本等息请款下每期应还手续费，最后算出手续费率
     *
     * @return 等本等息月手续费费率
     */
    public static BigDecimal calcMonthFeeRate(BigDecimal annualFeeRate, Integer stageNo, Integer principal) {
        BigDecimal monthAmount = calPMT(annualFeeRate, principal, stageNo);
        return monthAmount.multiply(BigDecimal.valueOf(stageNo)).subtract(BigDecimal.valueOf(principal))
                .divide(BigDecimal.valueOf(stageNo), DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE)
                .divide(BigDecimal.valueOf(principal), DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * excel中PMT函数,计算贷款分期每期固定付款额或固定利率（pv等于-1时，返回的即是固定利率）
     * 官方说明：https://support.office.com/zh-cn/article/PMT-%E5%87%BD%E6%95%B0-0214DA64-9A63-4996-BC20-214433FA6441
     *
     * @param rate 各期利率（月利率）
     * @param nper 总期数
     * @param pv   贷款金额
     * @return
     */
    @Deprecated
    public static double PMT(double rate, double nper, double pv) {

        double v = (1 + rate);

        double t = -nper;

        return -1 * (pv * (rate)) / (1 - Math.pow(v, t));

    }

    /**
     * excel中PV函数，与PMT函数相反，用于根据固定利率计算贷款或投资的现值
     * 官方说明：https://support.office.com/zh-cn/article/pv-%E5%87%BD%E6%95%B0-23879d31-0e02-4321-be01-da16e8168cbd
     *
     * @param rate 各期利率
     * @param nper 总期数
     * @param pmt  每期的付款金额或利率
     * @return
     */
    @Deprecated
    public static double PV(double rate, double nper, double pmt) {

        double v = (1 + rate);

        double t = -nper;

        return -pmt / rate * (1 - Math.pow(v, t));

    }


    /**
     * 等额本息法 总还款金额=[总本金×PMT年化利率1/12×(1+PMT年化利率1/12)^总期数]÷[(1+PMT年化利率1/12)^总期数-1]
     *
     * @param pmtAnnualRate 年化利率 如年化利率3.0%则传入0.03
     * @param principal     本金
     * @param nper          总期数
     * @return 总费用
     */
    public static BigDecimal calPMT(BigDecimal pmtAnnualRate, int principal, int nper) {
        return PMT(toMonthFeeRate(pmtAnnualRate), new BigDecimal(nper), new BigDecimal(principal).negate());
    }

    /**
     * 等额本息法
     */
    public static Integer calEPF(BigDecimal pmt1AnnualRate, BigDecimal pmt2AnnualRate, int principal, int nper) {
        //年利率转换为月利率
        BigDecimal pmt1Fee = calPMT(pmt1AnnualRate, principal, nper);
        BigDecimal pmt2Fee = calPMT(pmt2AnnualRate, principal, nper);
        BigDecimal fee = pmt1Fee.subtract(pmt2Fee);

        return formatBigDecimal2Tnt(fee);
    }

    /**
     * 费率本金法  总还款金额=总本金*年费率/12
     */
    public static Integer calRP(BigDecimal
                                        annualFeeRate, int principal) {
        BigDecimal fee =
                new BigDecimal(principal).multiply(annualFeeRate).divide(new BigDecimal(MONTHS_OF_YEARS), DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
        return formatBigDecimal2Tnt(fee);
    }

    /**
     * 费率本息法 （总本金+总利息）*年费率/12
     */
    public static Integer calRPF(BigDecimal
                                         annualFeeRate, int principal, int fee) {
        int pfVal = principal + fee;
        BigDecimal additionalFee = new BigDecimal(pfVal).multiply(annualFeeRate).divide(new BigDecimal(MONTHS_OF_YEARS), DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
        return formatBigDecimal2Tnt(additionalFee);
    }

    /**
     * 利率占比法 资方利息*(对客利率-资金利率)/对客利率
     */
    public static Integer calIRR(BigDecimal
                                         feeRate1, BigDecimal feeRate2, Integer fee) {
        BigDecimal retFee = new BigDecimalBuilder(feeRate2).subtract(feeRate1).multiply(fee).divide(feeRate2).valueof();

        return formatBigDecimal2Tnt(retFee);
    }

    /**
     * 分成比例法
     */
    public static Integer calDR(BigDecimal
                                        divideRate, Integer bankFee) {
        BigDecimal additionalFee = new BigDecimal(bankFee).multiply(divideRate);
        return formatBigDecimal2Tnt(additionalFee);
    }


    /**
     * 零附加费法
     */
    public static Integer calZero() {
        return 0;
    }

    /**
     * @param val
     * @return
     */
    private static int formatBigDecimal2Tnt(BigDecimal val) {
        return val.setScale(INT_TYPE_BIGDECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * @param val
     * @return
     */
    private static BigDecimal formatInt2BigDecimal(int val) {
        return new BigDecimal(val).setScale(DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * 年利息转月利率
     *
     * @param yearFeeRate 年利率
     * @return 月利率
     */
    private static BigDecimal toMonthFeeRate(BigDecimal yearFeeRate) {
        return yearFeeRate.divide(BigDecimal.valueOf(MONTHS_OF_YEARS), DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * Excel IRR 算法Java实现
     * IRR=a+[NPVa/(NPVa-NPVb)]*(b-a)
     * https://zhidao.baidu.com/question/210045181.html
     *
     * @return IRR
     */

    public static BigDecimal calcIRR(Integer principal, List<Integer> stagePlanAmounts) {

        /**最大迭代次数*/
        int LOOPNUM = 1000;
        /**最小差异*/
        double MINDIF = 0.00000000001;

        List<Integer> inputList = getValues(principal, stagePlanAmounts);
        double flowOut = inputList.get(0);
        double minValue = 0d;
        double maxValue = 1d;
        double resultValue = 0d;
        while (LOOPNUM > 0) {
            resultValue = (minValue + maxValue) / 2;
            double npv = NPV(inputList, resultValue);
            if (Math.abs(flowOut + npv) < MINDIF) {
                break;
            } else if (Math.abs(flowOut) > npv) {
                maxValue = resultValue;
            } else {
                minValue = resultValue;
            }
            LOOPNUM--;
        }
        return new BigDecimal(resultValue).multiply(new BigDecimal(MONTHS_OF_YEARS)).setScale(DEFAULT_BIGDECIMAL_SCALE, DEFAULT_BIGDECIMAL_ROUNDINGMODE);
    }

    /**
     * 折现率
     * http://www.caohaifeng.com/view/311.html
     */
    private static double NPV(List<Integer> inputFlowList, double rate) {
        double npv = 0d;
        for (int i = 1; i < inputFlowList.size(); i++) {
            npv += inputFlowList.get(i) / Math.pow(1 + rate, i);
        }
        return npv;
    }

    private static List<Integer> getValues(Integer principal, List<Integer> stagePlanAmounts) {
        stagePlanAmounts.add(0, -principal);
        return stagePlanAmounts;
    }

}
