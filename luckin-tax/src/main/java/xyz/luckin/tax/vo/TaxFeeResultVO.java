package xyz.luckin.tax.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 税费计算结果VO
 */
@Data
public class TaxFeeResultVO implements Serializable {
    /** 评估金额 */
    public BigDecimal evalAmount;
    /** 合计税额 */
    public BigDecimal total;
    /** 明细 */
    public List<TaxFeeItemResultVO> items=new ArrayList<>();
}
