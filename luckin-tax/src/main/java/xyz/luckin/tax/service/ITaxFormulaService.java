package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxFormula;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 计算公式Service接口
 *
 * @author luckin
 * @date 2021-02-20
 */
public interface ITaxFormulaService extends IService<TaxFormula> {

    /**
     * 查询列表
     */
    List<TaxFormula> queryList(TaxFormula taxFormula);

    List<TaxFormula> queryByItemId(Long itemId);

    List<TaxFormula> queryByItemId(Long itemId, String status);
}
