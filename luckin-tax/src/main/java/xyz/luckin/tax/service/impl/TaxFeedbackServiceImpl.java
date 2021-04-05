package xyz.luckin.tax.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.tax.mapper.TaxFeedbackMapper;
import xyz.luckin.tax.domain.TaxFeedback;
import xyz.luckin.tax.service.ITaxFeedbackService;

import java.util.List;
import java.util.Map;

/**
 * 问题反馈Service业务层处理
 *
 * @author luckin
 * @date 2021-03-23
 */
@Service
public class TaxFeedbackServiceImpl extends ServiceImpl<TaxFeedbackMapper, TaxFeedback> implements ITaxFeedbackService {

    @Override
    public List<TaxFeedback> queryList(TaxFeedback taxFeedback) {
        LambdaQueryWrapper<TaxFeedback> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(taxFeedback.getOpenid())){
            lqw.eq(TaxFeedback::getOpenid ,taxFeedback.getOpenid());
        }
        if (StringUtils.isNotBlank(taxFeedback.getContent())){
            lqw.eq(TaxFeedback::getContent ,taxFeedback.getContent());
        }
        if (StringUtils.isNotBlank(taxFeedback.getImageFiles())){
            lqw.eq(TaxFeedback::getImageFiles ,taxFeedback.getImageFiles());
        }
        Map<String, Object> params = taxFeedback.getParams();
        if (params.get("beginCreateTime") != null && params.get("endCreateTime") != null) {
            lqw.between(TaxFeedback::getCreateTime ,params.get("beginCreateTime"),params.get("endCreateTime"));
        }
        return this.list(lqw);
    }
}