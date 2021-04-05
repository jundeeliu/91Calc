package xyz.luckin.tax.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import xyz.luckin.common.annotation.Excel;
import xyz.luckin.common.core.domain.BasePlusEntity;

import java.math.BigDecimal;

/**
 * 计算公式对象 tax_formula
 * 
 * @author luckin
 * @date 2021-02-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_formula")
public class TaxFormula extends BasePlusEntity {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "formula_id")
    private Long formulaId;

    /** 项目ID */
    private Long itemId;

    @Excel(name = "优先级")
    private Integer priority;

    /** 公式标签 */
    @Excel(name = "公式标签")
    private String label;

    /** 表达式 */
    @Excel(name = "表达式")
    private String expression;

    /** 计税金额公式 */
    @Excel(name = "计税金额公式")
    private String taxAmountExpression;

    /** 税率 */
    @Excel(name = "税率")
    private BigDecimal taxRate;

    /** 土地增值税方案 */
    @Excel(name = "土地增值税方案")
    private String zzsPlan;

    @Excel(name = "是否有发票")
    private String zzsHasInvoice;

    @Excel(name = "增值税表达式")
    private String zzsAmountExpression;

    /** 评估金额字段 */
    @Excel(name = "评估金额字段")
    private String zzsEvalAmountFieldName;

    /** 发票金额字段 */
    @Excel(name = "发票金额字段")
    private String zzsInvoiceAmountFieldName;

    /** 发票日期字段名 */
    @Excel(name = "发票日期字段名")
    private String zzsInvoiceDateFieldName;

    /** 加计率 */
    @Excel(name = "加计率")
    private BigDecimal zzsAddChargeRate;

    /** 删除标志 */
    private String delFlag;

    @Excel(name = "是否启用",readConverterExp = "N=停用,Y=启用")
    private String status;
}
