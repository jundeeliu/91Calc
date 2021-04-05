package xyz.luckin.tax.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.tax.mapper.CalcFieldMapper;
import xyz.luckin.tax.domain.CalcField;
import xyz.luckin.tax.service.ICalcFieldService;

import java.util.List;
import java.util.Map;

/**
 * 税费计算字段Service业务层处理
 *
 * @author luckin
 * @date 2021-02-19
 */
@Service
public class CalcFieldServiceImpl extends ServiceImpl<CalcFieldMapper, CalcField> implements ICalcFieldService {

    @Override
    public List<CalcField> queryList(CalcField calcField) {
        LambdaQueryWrapper<CalcField> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(calcField.getFieldLabel())){
            lqw.eq(CalcField::getFieldLabel ,calcField.getFieldLabel());
        }
        return this.list(lqw);
    }

    @Override
    public CalcField getByName(String fieldName) {
        CalcField byName = getBaseMapper().getByName(fieldName);
        return byName;
    }
}
