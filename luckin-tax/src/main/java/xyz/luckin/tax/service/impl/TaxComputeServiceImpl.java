package xyz.luckin.tax.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.luckin.common.exception.CustomException;
import xyz.luckin.common.utils.DateUtils;
import xyz.luckin.common.utils.StringUtils;
import xyz.luckin.tax.domain.TaxFormula;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.service.ITaxComputeService;
import xyz.luckin.tax.service.ITaxFormulaService;
import xyz.luckin.tax.service.ITaxHistoryService;
import xyz.luckin.tax.service.ITaxItemFieldService;
import xyz.luckin.tax.vo.TaxFeeItemResultVO;
import xyz.luckin.tax.vo.TaxFeeResultVO;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 税费计算服务接口
 */
@Slf4j
@Service
public class TaxComputeServiceImpl implements ITaxComputeService {

    @Autowired
    private ITaxFormulaService formulaService;


    @Autowired
    private ITaxItemFieldService taxItemFieldService;

    @Autowired
    private ITaxHistoryService taxHistoryService;


    /** 地税评估金额字段名 */
    private String EVAL_AMOUNT_FIELD_NAME ="地税评估金额";
    /** 地税评估金额 */
    private String EVAL_AMOUNT_TAG="#E{地税评估金额}";

    /** 税率标签 */
    private String TAX_RATE_TAG="#E{税率}";

    /** 计税金额标签 */
    private String TAX_AMOUNT_TAG="#E{计税金额}";

    /** 加计额标签 */
    private String INVOICE_ADD_AMOUNT="#E{加计额}";

    /**
     * 计算税费
     * @param itemId 项目ID，用于查找项目的计算公式
     * @param fields 提交的计算字段信息
     * @param openid 用于保存计算历史
     * @return
     */
    @Override
    @Transactional
    public TaxFeeResultVO taxCompute(Long itemId, Map<String, String> fields, String openid) throws Exception{
        //taxFormulas：[TaxFormula(formulaId=1, itemId=5, label=aaa, expression=【面积】*12%, delFlag=0)]
        //formFields:{面积=32, 地税评估金额=323, 购房发票金额=4324, 购房发票日期=2020-01-12}
        log.info("开始计算税费开始");
        List<TaxFormula> taxFormulas = formulaService.queryByItemId(itemId, "Y");


        List<TaxItemField> taxItemFields = taxItemFieldService.queryByItemId(itemId);

        //重新整理表单的数据类型
        Map<String, Object> convertMap=new LinkedHashMap<>();
        Iterator<Map.Entry<String, String>> iterator = fields.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                if(taxItemFields.stream().filter(e -> e.getFieldName().equalsIgnoreCase(next.getKey())).count()>0) {
                    TaxItemField itemField = taxItemFields.stream().filter(e -> e.getFieldName().equalsIgnoreCase(next.getKey())).findFirst().get();
                    if (itemField != null && itemField.getFieldType().equalsIgnoreCase("N") && StringUtils.isNotBlank(next.getValue())) {
                        convertMap.put(next.getKey(), new Double(next.getValue()));
                    }
                }
            }
        }catch (Exception ex){
            log.error("数据转换失败,{}", ex);
            throw new CustomException("数据转换失败，请确认数据是否正确");
        }

        //获取表单的评估金额（固定格式），否则评估金额为0
        BigDecimal evalAmount=BigDecimal.ZERO;
        if(fields.containsKey(EVAL_AMOUNT_FIELD_NAME)) {
            evalAmount=new BigDecimal(fields.get(EVAL_AMOUNT_FIELD_NAME));
        }


        List<TaxFeeItemResultVO> taxFeeItemResultList = new ArrayList<>();
        BigDecimal totalTaxFee=BigDecimal.ZERO;
        for (TaxFormula formula :  taxFormulas) {

            TaxFeeItemResultVO taxFeeItemResultVO=null;
            //判断是否土地增值税
            if (StringUtils.isNotBlank(formula.getZzsPlan())) {
                taxFeeItemResultVO=calculateZzs(formula, fields, convertMap,taxFeeItemResultList);
                taxFeeItemResultList.add(taxFeeItemResultVO);
            }else {


                //税额表达式
                String expression = formula.getExpression();


                //处理税率标签 $E{税率}
                BigDecimal taxRate = formula.getTaxRate().divide(new BigDecimal(100));
                expression = expression.replace(TAX_RATE_TAG, taxRate.toString());

                //计税金额表达式
                String taxAmountExpression = formula.getTaxAmountExpression();
                BigDecimal taxAmountResult = BigDecimal.ZERO;
                if (StringUtils.isNotBlank(taxAmountExpression)) {
                    //处理计税金额标签 $E{计税金额}
                    taxAmountExpression = taxAmountExpression.replace(TAX_AMOUNT_TAG, taxRate.toString());

                    //处理税额
                    List<String> taxAmountExpressionParams = getExpressionParams(taxAmountExpression);
                    for (String exp : taxAmountExpressionParams) {
                        //查询已经计算出来的结果值，因为是按优先级排序的，所以使用时必须前面已经计算出来
                        TaxFeeItemResultVO taxFeeItemVO = taxFeeItemResultList.stream().filter(e -> e.name.equalsIgnoreCase(exp)).findFirst().get();
                        //替换表达式【其它计算结果值】
                        taxAmountExpression = taxAmountExpression.replace("#{" + exp + "}", taxFeeItemVO.getAmount().toString());
                    }
                    Double _taxAmount = (Double) AviatorEvaluator.execute(taxAmountExpression, convertMap);

                    taxAmountResult = BigDecimal.valueOf(_taxAmount).setScale(2, BigDecimal.ROUND_HALF_UP);


                }
                expression = expression.replace(TAX_AMOUNT_TAG, taxAmountResult.toString());

                //处理结果表达式
                List<String> expressionParams = getExpressionParams(expression);
                for (String exp : expressionParams) {
                    //查询已经计算出来的结果值，因为是按优先级排序的，所以使用时必须前面已经计算出来
                    TaxFeeItemResultVO taxFeeItemVO = taxFeeItemResultList.stream().filter(e -> e.name.equalsIgnoreCase(exp)).findFirst().get();
                    //替换表达式【其它计算结果值】
                    expression = expression.replace("#{" + exp + "}", taxFeeItemVO.getAmount().toString());
                }
                Double result = (Double) AviatorEvaluator.execute(expression, convertMap);

                taxFeeItemResultVO = new TaxFeeItemResultVO();
                taxFeeItemResultVO.setName(formula.getLabel());
                taxFeeItemResultVO.setTaxAmount(taxAmountResult);
                taxFeeItemResultVO.setTaxRate(taxRate.multiply(new BigDecimal(100)));
                taxFeeItemResultVO.setAmount(BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP));
                taxFeeItemResultList.add(taxFeeItemResultVO);


            }
            //累计
            totalTaxFee = totalTaxFee.add(taxFeeItemResultVO.getAmount());

        }
        TaxFeeResultVO taxFeeResultVO=new TaxFeeResultVO();
        taxFeeResultVO.setEvalAmount(evalAmount);
        taxFeeResultVO.setTotal(totalTaxFee);
        taxFeeResultVO.setItems(taxFeeItemResultList);

        //写入用户历史
//        String area=fields.get("area");
//        createHistory(openid, area, taxFeeResultVO);

        return taxFeeResultVO;
    }

    /**
     * 计算土地增值税
     * <pre>
     *      差额A=评估价-增值税-项目成本(购房发票金额+#E{加计额}+契税金额+重置成本+本次城建费+附加A+附加B+印花税卖方)
     *      差额B=评估价-项目成本(购地发票金额+购地契税金额)
     *      级距=差额/项目成本（级距<=50% 按30%税率; >50%级距<=100% 按40%税率；>100%级距<=200% 按50%税率; 级距>200% 按60%税率)
     *      结果=差额*税率-(成本*系数)
     * </pre>
     * @param formula 公式
     * @param fields 表单数据
     * @param numericMap 已转换的数字型数据
     * @return
     */
    private TaxFeeItemResultVO calculateZzs(TaxFormula formula, Map<String, String> fields, Map<String ,Object> numericMap, List<TaxFeeItemResultVO> taxFeeItemResultList) throws Exception {
        String evalAmountField=formula.getZzsEvalAmountFieldName();
        String invoiceAmountField=formula.getZzsInvoiceAmountFieldName();
        String invoiceDateField=formula.getZzsInvoiceDateFieldName();

        //评估价
        BigDecimal evalAmount = new BigDecimal(fields.get(evalAmountField));

        //增值税 金额
        BigDecimal zzsAmount=BigDecimal.ZERO;

        //项目成本表达式
        String costExpression=formula.getExpression();

        //如果有发票的才需要计算加计额
        if(StringUtils.isNotBlank(formula.getZzsHasInvoice()) && formula.getZzsHasInvoice().equalsIgnoreCase("Y")) {
            //发票金额
            BigDecimal invoiceAmount = new BigDecimal(fields.get(invoiceAmountField));
            //发票日期
            Date invoiceDate = DateUtil.parse(fields.get(invoiceDateField));

            //计算加计额
            BigDecimal invoiceAddAmount = calculateInvoiceAddAmount(invoiceAmount, invoiceDate, formula.getZzsAddChargeRate().divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));

            //处理表达式中的 #E{加计额}
            costExpression=costExpression.replace(INVOICE_ADD_AMOUNT, invoiceAddAmount.toString());


            String zzsAmountExpression=formula.getZzsAmountExpression();
            //处理增值税金额
            List<String> expressionParams = getExpressionParams(zzsAmountExpression);
            for (String exp : expressionParams) {
                //查询已经计算出来的结果值，因为是按优先级排序的，所以使用时必须前面已经计算出来
                TaxFeeItemResultVO taxFeeItemResultVO = taxFeeItemResultList.stream().filter(e -> e.name.equalsIgnoreCase(exp)).findFirst().get();
                //替换表达式【其它计算结果值】
                zzsAmountExpression = zzsAmountExpression.replace("#{" + exp + "}", taxFeeItemResultVO.getAmount().toString());
            }
            //增值税金额结果
            Double zzsAmountResult  = (Double) AviatorEvaluator.execute(zzsAmountExpression, numericMap);
            zzsAmount=BigDecimal.valueOf(zzsAmountResult).setScale(2, BigDecimal.ROUND_HALF_UP);
        }


        //List<TaxFeeItemResultVO> taxFeeItemResultList = new ArrayList<>();
        //处理项目成本表达式
        List<String> expressionParams = getExpressionParams(costExpression);
        for (String exp : expressionParams) {
            //查询已经计算出来的结果值，因为是按优先级排序的，所以使用时必须前面已经计算出来
            TaxFeeItemResultVO taxFeeItemResultVO = taxFeeItemResultList.stream().filter(e -> e.name.equalsIgnoreCase(exp)).findFirst().get();
            //替换表达式【其它计算结果值】
            costExpression = costExpression.replace("#{" + exp + "}", taxFeeItemResultVO.getAmount().toString());
        }
        //成本金额结果
        Double result  = (Double) AviatorEvaluator.execute(costExpression, numericMap);

        BigDecimal costResult = BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP);

        //差额=评估金额-项目成本
        BigDecimal balance = evalAmount.subtract(zzsAmount).subtract(costResult).setScale(2, BigDecimal.ROUND_HALF_UP);

        //计算税率
        TaxFeeItemResultVO taxFeeItemResultVO =calculateZzsResult(formula.getZzsPlan(),balance, costResult);
        taxFeeItemResultVO.setName(formula.getLabel());


        return taxFeeItemResultVO;
    }

    /** 计算增值税结果
     * <pre>
     *     级距=差额/项目成本（级距<=50% 按30%税率; 50%<级距<=100% 按40%税率；100%<级距<=200% 按50%税率; 级距>200% 按60%税率)
     *     结果=差额*税率-(成本*系数) 【系数：税率30% -> 0%; 税率40% -> 5%; 税率50% -> 15%; 税率60% -> 35%; 】
     * </pre>
     * @param plan 方案
     * @param balance 差额
     * @param costAmount 项目成本
     * @return
     */
    private TaxFeeItemResultVO calculateZzsResult(String plan,  BigDecimal balance, BigDecimal costAmount) {
        //级距
        BigDecimal interval = balance.divide(costAmount,2 ,BigDecimal.ROUND_HALF_UP);
        //系数
        BigDecimal factor=BigDecimal.ZERO;

        BigDecimal rate=BigDecimal.ZERO;

        switch (plan) {
            case "A":
                if (interval.compareTo(new BigDecimal(2)) ==1 ) { //级距>200%
                    rate = new BigDecimal(0.6);
                    factor=new BigDecimal(0.35);
                }else if (interval.compareTo(new BigDecimal(2)) < 1 && interval.compareTo(new BigDecimal(1)) ==1 ) {  //100%<级距<=200%
                    rate=new BigDecimal(0.5);
                    factor=new BigDecimal(0.15);
                }else if (interval.compareTo(new BigDecimal(1)) < 1 && interval.compareTo(new BigDecimal(0.5)) ==1 ) {  //50%<级距<=100%
                    rate=new BigDecimal(0.4);
                    factor=new BigDecimal(0.05);
                }else if (interval.compareTo(new BigDecimal(0.5)) < 1) {  //级距<=50%
                    rate=new BigDecimal(0.3);
                    factor=new BigDecimal(0.00);
                }
                break;
            default:
                throw new CustomException("没有符合的土地增值税方案");
        }

        TaxFeeItemResultVO taxFeeItemResultVO=new TaxFeeItemResultVO();
        taxFeeItemResultVO.setTaxAmount(balance);
        taxFeeItemResultVO.setTaxRate(rate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
        //结果=差额*税率-(成本*系数)
        BigDecimal result=(balance.multiply(rate)).subtract((costAmount.multiply(factor)));
        taxFeeItemResultVO.setAmount(result.setScale(2, BigDecimal.ROUND_HALF_UP));

        log.info("差额：{}，级距：{}，税率：{}，系数：{}，结果：{}",
                balance,
                interval,
                rate.setScale(2, BigDecimal.ROUND_HALF_UP),
                factor.setScale(2, BigDecimal.ROUND_HALF_UP),
                result.setScale(2, BigDecimal.ROUND_HALF_UP));
        return taxFeeItemResultVO;
    }

    /**
     * 计算发票加计额
     * <pre>发票日期满半年按一年计，不满半年不计
     * 例：2017-03-27
     * </pre>
     * @param invoiceAmount 发票金额
     * @param invoiceDate 发票日期
     * @param rate 加计税率
     * @return
     */
    public BigDecimal calculateInvoiceAddAmount(BigDecimal invoiceAmount , Date invoiceDate, BigDecimal rate) throws Exception{
        //2017-03-17

        LocalDateTime localDateTime = LocalDateTimeUtil.of(invoiceDate);
        LocalDateTime now = LocalDateTimeUtil.now();

        boolean halfYear=false;

        int firstYear=localDateTime.getYear();
        int nowYear=now.getYear();
        int count=0;

        //判断是否今年的
        if(firstYear==nowYear){
            Duration between = LocalDateTimeUtil.between(localDateTime, now);
            if(between.toDays()>180){
                count++;
            }
        }else {
            for (int i = firstYear; i < nowYear; i++) {
                if(localDateTime.getYear()>firstYear && localDateTime.getYear()<nowYear){
                    count++;
                }else if(localDateTime.getYear()==firstYear && localDateTime.getDayOfYear() < 180) {
                    count++;
                }
                localDateTime = localDateTime.plusYears(1);
                if(localDateTime.getYear()==nowYear) {
                    Duration between = LocalDateTimeUtil.between(localDateTime, now);
                    if (between.toDays() > 180) {
                        count++;
                    }
                }
            }
        }

        BigDecimal addAmount=invoiceAmount.multiply(rate.multiply(new BigDecimal(count)).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);

        log.info("发票日期：{}，满 {} 年，加计额 {} 元", invoiceDate.toString(), count, addAmount);

        return addAmount;
    }

    /**
     * 保存历史记录
     * @param openid 用户openid
     * @param area 省市区
     * @param community 小区
     * @param evalAmount 评估金额
     * @param taxFeeResultVO 计算结果
     * @return
     */
    @Override
    public boolean createHistory(String openid, String area, String community, TaxFeeResultVO taxFeeResultVO){
        if(StringUtils.isNotBlank(openid)){
            log.info("写入用户计算历史");
            TaxHistory taxHistory = new TaxHistory();
            taxHistory.setOpenid(openid);
            taxHistory.setArea(area);
            taxHistory.setCommunity(community);
            taxHistory.setEvalAmount(taxFeeResultVO.getEvalAmount());

            String jsonStr = JSONUtil.toJsonStr(taxFeeResultVO);
            taxHistory.setComputeResult(jsonStr);
            taxHistory.setCreateTime(DateUtils.getNowDate());
            boolean save = taxHistoryService.save(taxHistory);
            return save;
        }
        return false;
    }

    /**
     * 匹配 #{关键字}
     * @param expression
     * @return
     */
    private List<String> getExpressionParams(String expression){
        Pattern pattern = Pattern.compile("#\\{[^\\}]+\\}");
        Matcher matcher = pattern.matcher(expression);
        List<String> result=new ArrayList<>();
        //循环，字符串中有多少个符合的，就循环多少次
        while(matcher.find()){
            //每一个符合正则的字符串
            String e = matcher.group();
            //截取出括号中的内容
            String substring = e.substring(2, e.length()-1);
            result.add(substring);
        }
        return result;
    }
}
