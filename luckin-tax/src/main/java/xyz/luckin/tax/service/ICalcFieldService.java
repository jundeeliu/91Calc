package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.CalcField;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.luckin.tax.service.impl.CalcFieldServiceImpl;

import java.util.List;

/**
 * 税费计算字段Service接口
 *
 * @author luckin
 * @date 2021-02-19
 */
public interface ICalcFieldService extends IService<CalcField> {

    /**
     * 查询列表
     */
    List<CalcField> queryList(CalcField calcField);

    /**
     * 根据name找字段
     * @param fieldName 字段名称
     * @return
     */
    CalcField getByName(String fieldName);

}
