package xyz.luckin.tax.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.luckin.tax.domain.TaxItemField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 项目字段信息Mapper接口
 *
 * @author luckin
 * @date 2021-02-20
 */
public interface TaxItemFieldMapper extends BaseMapper<TaxItemField> {

    @Select("select field_id from tax_item_field where item_id=#{itemId}")
    List<Long> selectByItemId(@Param("itemId") Long itemId);



    @Select("select t.item_id, t.is_required, t.default_value, f.field_id, f.field_type, f.field_label,f.field_name, f.field_unit from tax_item_field t\n" +
            "left join tax_calc_field f on t.field_id = f.field_id\n" +
            "where t.item_id=#{itemId} ")
    List<TaxItemField> queryByItemId(@Param("itemId") Long itemId);

}