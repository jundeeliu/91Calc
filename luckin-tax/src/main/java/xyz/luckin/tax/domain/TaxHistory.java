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
 * 计算历史对象 tax_history
 * 
 * @author luckin
 * @date 2021-02-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_history")
public class TaxHistory extends BasePlusEntity {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "history_id")
    private Long historyId;

    @Excel(name = "OPENID")
    private String openid;

    /** 省市区 */
    @Excel(name = "省市区")
    private String area;

    @Excel(name = "地址")
    private String address;

    /** 楼盘 */
    @Excel(name = "楼盘")
    private String community;

    /** 栋号 */
    @Excel(name = "栋号")
    private String buildingNo;

    /** 评估价格 */
    @Excel(name = "评估价格")
    private BigDecimal evalAmount;


    @Excel(name="计算结果")
    private String computeResult;
}
