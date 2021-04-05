package xyz.luckin.tax.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import xyz.luckin.common.annotation.Excel;

import java.io.Serializable;

/**
 * 项目字段信息对象 tax_item_field
 * 
 * @author luckin
 * @date 2021-02-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_item_field")
public class TaxItemField implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 项目ID */
    private Long itemId;

    /** 字段ID */
    private Long fieldId;

    /** 是否必填(N否 Y是) */
    @Excel(name = "是否必填(N否 Y是)")
    private String isRequired;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    @TableField(exist = false)
    private String fieldType;

    @TableField(exist = false)
    private String fieldName;

    @TableField(exist = false)
    private String fieldLabel;

    @TableField(exist = false)
    private String fieldUnit;

}