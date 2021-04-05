package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxReport;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 上报地税评估Service接口
 *
 * @author luckin
 * @date 2021-03-23
 */
public interface ITaxReportService extends IService<TaxReport> {

    /**
     * 查询列表
     */
    List<TaxReport> queryList(TaxReport taxReport);


    /**
     * 审核
     * @param reportId
     * @return
     */
    boolean audit(Long reportId);
}