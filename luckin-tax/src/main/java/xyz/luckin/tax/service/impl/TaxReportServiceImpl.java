package xyz.luckin.tax.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.tax.mapper.TaxReportMapper;
import xyz.luckin.tax.domain.TaxReport;
import xyz.luckin.tax.service.ITaxReportService;

import java.util.List;
import java.util.Map;

/**
 * 上报地税评估Service业务层处理
 *
 * @author luckin
 * @date 2021-03-23
 */
@Service
public class TaxReportServiceImpl extends ServiceImpl<TaxReportMapper, TaxReport> implements ITaxReportService {

    @Override
    public List<TaxReport> queryList(TaxReport taxReport) {
        LambdaQueryWrapper<TaxReport> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(taxReport.getOpenid())){
            lqw.eq(TaxReport::getOpenid ,taxReport.getOpenid());
        }
        if (StringUtils.isNotBlank(taxReport.getAddress())){
            lqw.eq(TaxReport::getAddress ,taxReport.getAddress());
        }
        if (taxReport.getArea() != null){
            lqw.eq(TaxReport::getArea ,taxReport.getArea());
        }
        if (taxReport.getEvalPrice() != null){
            lqw.eq(TaxReport::getEvalPrice ,taxReport.getEvalPrice());
        }
        if (taxReport.getEvalAmount() != null){
            lqw.eq(TaxReport::getEvalAmount ,taxReport.getEvalAmount());
        }
        if (StringUtils.isNotBlank(taxReport.getImageFiles())){
            lqw.eq(TaxReport::getImageFiles ,taxReport.getImageFiles());
        }
        Map<String, Object> params = taxReport.getParams();
        if (params.get("beginCreateTime") != null && params.get("endCreateTime") != null) {
            lqw.between(TaxReport::getCreateTime ,params.get("beginCreateTime"),params.get("endCreateTime"));
        }
        return this.list(lqw);
    }

    @Override
    public boolean audit(Long reportId) {
        TaxReport taxReport = getById(reportId);
        if(taxReport!=null && taxReport.getStatus().equalsIgnoreCase("N")){
            taxReport.setStatus("Y");
            return updateById(taxReport);
        }
        return false;
    }
}