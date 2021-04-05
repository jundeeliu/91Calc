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
 * 楼栋信息对象 tax_building
 * 
 * @author luckin
 * @date 2021-03-30
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tax_building")
public class TaxBuilding extends BasePlusEntity {

private static final long serialVersionUID=1L;


    /** 楼栋ID */
    @TableId(value = "building_id")
    private Long buildingId;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String district;

    /** 省市区 */
    @Excel(name = "省市区")
    private String area;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 小区 */
    @Excel(name = "小区")
    private String community;

    /** 栋号 */
    @Excel(name = "栋号")
    private String buildingNo;

}
