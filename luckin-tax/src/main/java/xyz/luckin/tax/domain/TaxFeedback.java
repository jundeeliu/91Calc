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

import java.util.HashMap;
import java.util.Map;

/**
 * 问题反馈对象 tax_feedback
 * 
 * @author luckin
 * @date 2021-03-23
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_feedback")
public class TaxFeedback extends BasePlusEntity {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "feedback_id")
    private Long feedbackId;

    /** 用户openid */
    @Excel(name = "用户openid")
    private String openid;

    /** 反馈内容 */
    @Excel(name = "反馈内容")
    private String content;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageFiles;

}