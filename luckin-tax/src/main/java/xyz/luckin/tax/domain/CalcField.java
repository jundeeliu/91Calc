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

/**
 * 税费计算字段对象 tax_calc_field
 * 
 * @author luckin
 * @date 2021-02-19
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_calc_field")
public class CalcField extends BasePlusEntity {
    /** $column.columnComment */
    @TableId(value = "field_id")
    private Long fieldId;

    /** 字段名称(唯一) */
    @Excel(name = "字段标签")
    private String fieldName;

    /** 字段标签 */
    @Excel(name = "字段标签")
    private String fieldLabel;

    /** 字段类型(S字符 D日期 N数字) */
    @Excel(name = "字段类型(S字符 D日期 N数字)")
    private String fieldType;

    @Excel(name = "字段单位")
    private String fieldUnit;

}
