package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.CalcItem;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.luckin.tax.domain.TaxItemField;

import java.util.List;

/**
 * 税费计算项目Service接口
 *
 * @author luckin
 * @date 2021-02-19
 */
public interface ICalcItemService extends IService<CalcItem> {

    /**
     * 查询列表
     */
    List<CalcItem> queryList(CalcItem calcItem);

    int setItemField(Long itemId, List<TaxItemField> itemFields);

    List<CalcItem> queryListByParentId(Long parentId);
}
