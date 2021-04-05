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
import xyz.luckin.tax.domain.CalcField;
import xyz.luckin.tax.service.ICalcFieldService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 税费计算字段Controller
 * 
 * @author luckin
 * @date 2021-02-19
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/calcfield" )
public class CalcFieldController extends BaseController {

    private final ICalcFieldService iCalcFieldService;

    /**
     * 查询税费计算字段列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:list')")
    @GetMapping("/list")
    public TableDataInfo list(CalcField calcField) {
        startPage();
        List<CalcField> list = iCalcFieldService.queryList(calcField);
        return getDataTable(list);
    }

    /**
     * 导出税费计算字段列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:export')" )
    @Log(title = "税费计算字段" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CalcField calcField) {
        List<CalcField> list = iCalcFieldService.queryList(calcField);
        ExcelUtil<CalcField> util = new ExcelUtil<CalcField>(CalcField.class);
        return util.exportExcel(list, "calcfield" );
    }

    /**
     * 获取税费计算字段详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:query')" )
    @GetMapping(value = "/{fieldId}" )
    public AjaxResult getInfo(@PathVariable("fieldId" ) Long fieldId) {
        return AjaxResult.success(iCalcFieldService.getById(fieldId));
    }

    /**
     * 新增税费计算字段
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:add')" )
    @Log(title = "税费计算字段" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CalcField calcField) {
        return toAjax(iCalcFieldService.save(calcField) ? 1 : 0);
    }

    /**
     * 修改税费计算字段
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:edit')" )
    @Log(title = "税费计算字段" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CalcField calcField) {
        return toAjax(iCalcFieldService.updateById(calcField) ? 1 : 0);
    }

    /**
     * 删除税费计算字段
     */
    @PreAuthorize("@ss.hasPermi('tax:calcfield:remove')" )
    @Log(title = "税费计算字段" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{fieldIds}" )
    public AjaxResult remove(@PathVariable Long[] fieldIds) {
        return toAjax(iCalcFieldService.removeByIds(Arrays.asList(fieldIds)) ? 1 : 0);
    }
}
