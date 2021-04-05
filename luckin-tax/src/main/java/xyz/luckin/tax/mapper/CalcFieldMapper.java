package xyz.luckin.tax.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.luckin.tax.domain.CalcField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 税费计算字段Mapper接口
 *
 * @author luckin
 * @date 2021-02-19`
 */
public interface CalcFieldMapper extends BaseMapper<CalcField> {

    @Select("select * from tax_calc_field where field_name=#{fieldName} limit 1")
    CalcField getByName(@Param("fieldName") String fieldName);
}
