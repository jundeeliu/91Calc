package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxItemField;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 项目字段信息Service接口
 *
 * @author luckin
 * @date 2021-02-20
 */
public interface ITaxItemFieldService extends IService<TaxItemField> {

    List<TaxItemField> queryByItemId(Long itemId);
    /**
     * 查询列表
     */
    List<Long> selectByItemId(Long itemId);


}