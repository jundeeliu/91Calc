package xyz.luckin.tax.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 税费计算项目对象 tax_calc_item
 * 
 * @author luckin
 * @date 2021-02-19
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_calc_item")
public class CalcItem extends BasePlusEntity {

    /** $column.columnComment */
    @TableId(value = "item_id")
    private Long itemId;

    /** $column.columnComment */
    @Excel(name = "${comment}" , readConverterExp = "$column.readConverterExp()")
    private String itemName;

    /** 父ID */
    @Excel(name = "父ID")
    private Long parentId;

    @TableField(exist = false)
    private Long groupId;

    @Excel(name="归属组")
    private String itemGroup;

}
