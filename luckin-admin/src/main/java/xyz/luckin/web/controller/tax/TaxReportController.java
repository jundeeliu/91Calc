package xyz.luckin.web.controller.tax;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.luckin.common.annotation.Log;
import xyz.luckin.common.core.controller.BaseController;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.enums.BusinessType;
import xyz.luckin.tax.domain.TaxReport;
import xyz.luckin.tax.service.ITaxReportService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 上报地税评估Controller
 * 
 * @author luckin
 * @date 2021-03-23
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/report" )
public class TaxReportController extends BaseController {

    private final ITaxReportService iTaxReportService;

    /**
     * 查询上报地税评估列表
     */
    @PreAuthorize("@ss.hasPermi('tax:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxReport taxReport) {
        startPage();
        List<TaxReport> list = iTaxReportService.queryList(taxReport);
        return getDataTable(list);
    }

    /**
     * 导出上报地税评估列表
     */
    @PreAuthorize("@ss.hasPermi('tax:report:export')" )
    @Log(title = "上报地税评估" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxReport taxReport) {
        List<TaxReport> list = iTaxReportService.queryList(taxReport);
        ExcelUtil<TaxReport> util = new ExcelUtil<TaxReport>(TaxReport.class);
        return util.exportExcel(list, "report" );
    }

    /**
     * 获取上报地税评估详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:report:query')" )
    @GetMapping(value = "/{reportId}" )
    public AjaxResult getInfo(@PathVariable("reportId" ) Long reportId) {
        return AjaxResult.success(iTaxReportService.getById(reportId));
    }

    /**
     * 新增上报地税评估
     */
    @PreAuthorize("@ss.hasPermi('tax:report:add')" )
    @Log(title = "上报地税评估" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxReport taxReport) {
        return toAjax(iTaxReportService.save(taxReport) ? 1 : 0);
    }

    /**
     * 修改上报地税评估
     */
    @PreAuthorize("@ss.hasPermi('tax:report:edit')" )
    @Log(title = "上报地税评估" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxReport taxReport) {
        return toAjax(iTaxReportService.updateById(taxReport) ? 1 : 0);
    }

    @PreAuthorize("@ss.hasPermi('tax:report:audit')" )
    @Log(title = "上报地税评估审核" , businessType = BusinessType.UPDATE)
    @PostMapping("/audit/{reportId}")
    public AjaxResult audit(@PathVariable("reportId") Long reportId) {
        return toAjax(iTaxReportService.audit(reportId) ? 1 : 0);
    }

    /**
     * 删除上报地税评估
     */
    @PreAuthorize("@ss.hasPermi('tax:report:remove')" )
    @Log(title = "上报地税评估" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{reportIds}" )
    public AjaxResult remove(@PathVariable Long[] reportIds) {
        return toAjax(iTaxReportService.removeByIds(Arrays.asList(reportIds)) ? 1 : 0);
    }
}