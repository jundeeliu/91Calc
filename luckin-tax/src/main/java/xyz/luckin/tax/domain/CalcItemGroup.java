package xyz.luckin.tax.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import xyz.luckin.common.annotation.Excel;
import xyz.luckin.common.core.domain.BasePlusEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 税费计算项目组对象 tax_calc_group
 * 
 * @author luckin
 * @date 2021-02-19
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_calc_item_group")
public class CalcItemGroup extends BasePlusEntity {

    /** $column.columnComment */
    @TableId(value = "group_id")
    private Long groupId;

    /** $column.columnComment */
    @Excel(name = "${comment}" , readConverterExp = "$column.readConverterExp()")
    private String groupName;

    /** 父ID */
    @Excel(name = "父ID")
    private Long parentId;

    @TableField(exist = false)
    private List<CalcItemGroup> children =new ArrayList<>();

}
