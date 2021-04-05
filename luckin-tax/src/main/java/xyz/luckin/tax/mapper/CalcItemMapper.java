package xyz.luckin.tax.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.luckin.tax.domain.CalcItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 税费计算项目Mapper接口
 *
 * @author luckin
 * @date 2021-02-19
 */
public interface CalcItemMapper extends BaseMapper<CalcItem> {

    @Select("select item_group from tax_calc_item where parent_id=#{parentId} group by item_group")
    List<String> itemGroupByParentId(@Param("parentId") Long parentId);

}
