package xyz.luckin.tax.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 税费计算结果VO
 */
@Data
public class TaxFeeItemResultVO implements Serializable {
    /**
     * 税种
     */
    public String name;
    /**
     * 计税金额
     */
    public BigDecimal taxAmount = BigDecimal.ZERO;
    /**
     * 税率
     */
    public BigDecimal taxRate = BigDecimal.ZERO;
    /**
     * 税额
     */
    public BigDecimal amount;
}
