package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxFeedback;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 问题反馈Service接口
 *
 * @author luckin
 * @date 2021-03-23
 */
public interface ITaxFeedbackService extends IService<TaxFeedback> {

    /**
     * 查询列表
     */
    List<TaxFeedback> queryList(TaxFeedback taxFeedback);
}