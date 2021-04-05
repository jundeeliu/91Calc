package xyz.luckin.tax.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.mapper.TaxItemFieldMapper;
import xyz.luckin.tax.service.ITaxItemFieldService;

import java.util.List;

/**
 * 项目字段信息Service业务层处理
 *
 * @author luckin
 * @date 2021-02-20
 */
@Service
public class TaxItemFieldServiceImpl extends ServiceImpl<TaxItemFieldMapper, TaxItemField> implements ITaxItemFieldService {

    public List<TaxItemField> queryByItemId(Long itemId) {
        return getBaseMapper().queryByItemId(itemId);
    }

    @Override
    public List<Long> selectByItemId(Long itemId){
        return getBaseMapper().selectByItemId(itemId);
    }
}