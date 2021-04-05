package xyz.luckin.tax.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import xyz.luckin.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import xyz.luckin.common.core.domain.BasePlusEntity;

import java.math.BigDecimal;

/**
 * 上报地税评估对象 tax_report
 * 
 * @author luckin
 * @date 2021-03-23
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_report")
public class TaxReport extends BasePlusEntity {

    private static final long serialVersionUID = 1L;


    /**
     * $column.columnComment
     */
    @TableId(value = "report_id")
    private Long reportId;

    /**
     * 用户openid
     */
    @Excel(name = "用户openid")
    private String openid;

    /**
     * 地址
     */
    @Excel(name = "地址")
    private String address;

    /**
     * 面积
     */
    @Excel(name = "面积")
    private BigDecimal area;

    /**
     * 评估单价
     */
    @Excel(name = "评估单价")
    private BigDecimal evalPrice;

    /**
     * 评估金额
     */
    @Excel(name = "评估金额")
    private BigDecimal evalAmount;

    /**
     * 图片地址
     */
    @Excel(name = "图片地址")
    private String imageFiles;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态", readConverterExp = "N=待审,Y=已审")
    private String status;
}