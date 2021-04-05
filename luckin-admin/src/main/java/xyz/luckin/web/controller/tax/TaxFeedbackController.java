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
import xyz.luckin.tax.domain.TaxFeedback;
import xyz.luckin.tax.service.ITaxFeedbackService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 问题反馈Controller
 * 
 * @author luckin
 * @date 2021-03-23
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/feedback" )
public class TaxFeedbackController extends BaseController {

    private final ITaxFeedbackService iTaxFeedbackService;

    /**
     * 查询问题反馈列表
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxFeedback taxFeedback) {
        startPage();
        List<TaxFeedback> list = iTaxFeedbackService.queryList(taxFeedback);
        return getDataTable(list);
    }

    /**
     * 导出问题反馈列表
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:export')" )
    @Log(title = "问题反馈" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxFeedback taxFeedback) {
        List<TaxFeedback> list = iTaxFeedbackService.queryList(taxFeedback);
        ExcelUtil<TaxFeedback> util = new ExcelUtil<TaxFeedback>(TaxFeedback.class);
        return util.exportExcel(list, "feedback" );
    }

    /**
     * 获取问题反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:query')" )
    @GetMapping(value = "/{feedbackId}" )
    public AjaxResult getInfo(@PathVariable("feedbackId" ) Long feedbackId) {
        return AjaxResult.success(iTaxFeedbackService.getById(feedbackId));
    }

    /**
     * 新增问题反馈
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:add')" )
    @Log(title = "问题反馈" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxFeedback taxFeedback) {
        return toAjax(iTaxFeedbackService.save(taxFeedback) ? 1 : 0);
    }

    /**
     * 修改问题反馈
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:edit')" )
    @Log(title = "问题反馈" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxFeedback taxFeedback) {
        return toAjax(iTaxFeedbackService.updateById(taxFeedback) ? 1 : 0);
    }

    /**
     * 删除问题反馈
     */
    @PreAuthorize("@ss.hasPermi('tax:feedback:remove')" )
    @Log(title = "问题反馈" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}" )
    public AjaxResult remove(@PathVariable Long[] feedbackIds) {
        return toAjax(iTaxFeedbackService.removeByIds(Arrays.asList(feedbackIds)) ? 1 : 0);
    }
}