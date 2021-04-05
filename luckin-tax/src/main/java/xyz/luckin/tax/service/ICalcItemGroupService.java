package xyz.luckin.tax.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.luckin.common.core.domain.TreeSelect;
import xyz.luckin.tax.domain.CalcItemGroup;

import java.util.List;

/**
 * 税费计算项目组Service接口
 *
 * @author luckin
 * @date 2021-02-19
 */
public interface ICalcItemGroupService extends IService<CalcItemGroup> {

    /**
     * 查询列表
     */
    List<CalcItemGroup> queryList(CalcItemGroup calcItemGroup);

    List<TreeSelect> buildGroupTreeSelect(List<CalcItemGroup> groups);
}
