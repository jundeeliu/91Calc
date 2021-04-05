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

/**
 * 会员信息对象 tax_member
 * 
 * @author luckin
 * @date 2021-02-20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_member")
public class TaxMember extends BasePlusEntity {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "member_id")
    private Long memberId;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickname;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 手机 */
    @Excel(name="手机")
    private String phone;

    /** OPENID */
    @Excel(name = "OPENID")
    private String openid;

    /** 删除标志 */
    private String delFlag;


}
