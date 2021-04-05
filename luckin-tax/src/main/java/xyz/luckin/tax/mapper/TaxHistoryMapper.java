package xyz.luckin.tax.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxMember;

import java.util.List;

/**
 * 税费计算历史Mapper接口
 *
 * @author luckin
 * @date 2021-02-19
 */
public interface TaxHistoryMapper extends BaseMapper<TaxHistory> {

    @Select("select * from tax_history where history_id=#{historyId} and openid=#{openid}")
    TaxHistory getById(@Param("historyId") Long historyId,@Param("openid") String openid);
}
