package xyz.luckin.tax.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.tax.mapper.TaxFormulaMapper;
import xyz.luckin.tax.domain.TaxFormula;
import xyz.luckin.tax.service.ITaxFormulaService;

import java.util.List;
import java.util.Map;

/**
 * 计算公式Service业务层处理
 *
 * @author luckin
 * @date 2021-02-20
 */
@Service
public class TaxFormulaServiceImpl extends ServiceImpl<TaxFormulaMapper, TaxFormula> implements ITaxFormulaService {

    @Override
    public List<TaxFormula> queryList(TaxFormula taxFormula) {
        LambdaQueryWrapper<TaxFormula> lqw = Wrappers.lambdaQuery();
        if (taxFormula.getItemId() != null){
            lqw.eq(TaxFormula::getItemId ,taxFormula.getItemId());
        }
        if (StringUtils.isNotBlank(taxFormula.getLabel())){
            lqw.eq(TaxFormula::getLabel ,taxFormula.getLabel());
        }
        lqw.orderByAsc(TaxFormula::getPriority);
        return this.list(lqw);
    }

    @Override
    public List<TaxFormula> queryByItemId(Long itemId) {
        LambdaQueryWrapper<TaxFormula> lqw = Wrappers.lambdaQuery();
        if (itemId != null){
            lqw.eq(TaxFormula::getItemId ,itemId);
        }
        lqw.orderByAsc(TaxFormula::getPriority);
        return this.list(lqw);
    }

    @Override
    public List<TaxFormula> queryByItemId(Long itemId, String status) {
        LambdaQueryWrapper<TaxFormula> lqw = Wrappers.lambdaQuery();
        if (itemId != null){
            lqw.eq(TaxFormula::getItemId ,itemId);
        }
        if(StringUtils.isNotBlank(status)){
            lqw.eq(TaxFormula::getStatus, status);
        }
        lqw.orderByAsc(TaxFormula::getPriority);
        return this.list(lqw);
    }


}
