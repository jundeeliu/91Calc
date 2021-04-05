package xyz.luckin.tax.form;

import lombok.Data;

/**
 * 前端提交过来的计算字段表单
 */
@Data
public class ItemFieldForm {
    /**
     * 项目ID
     */
    public Long itemId;
    /**
     * 字段ID
     */
    private Long fieldId;
    /**
     * 前端录入的值
     */
    public String fieldValue;
}
