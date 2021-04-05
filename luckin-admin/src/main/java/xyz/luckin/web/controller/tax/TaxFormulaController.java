package xyz.luckin.web.controller.tax;

import java.util.Comparator;
import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.luckin.common.annotation.Log;
import xyz.luckin.common.core.controller.BaseController;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.enums.BusinessType;
import xyz.luckin.tax.domain.TaxFormula;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.service.ITaxFormulaService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;
import xyz.luckin.tax.service.ITaxItemFieldService;

/**
 * 计算公式Controller
 * 
 * @author luckin
 * @date 2021-02-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/formula" )
public class TaxFormulaController extends BaseController {

    private final ITaxFormulaService iTaxFormulaService;

    @Autowired
    private ITaxItemFieldService taxItemFieldService;

    /**
     * 查询计算公式列表
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxFormula taxFormula) {
        startPage();
        List<TaxFormula> list = iTaxFormulaService.queryList(taxFormula);
        return getDataTable(list);
    }

    /**
     * 导出计算公式列表
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:export')" )
    @Log(title = "计算公式" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxFormula taxFormula) {
        List<TaxFormula> list = iTaxFormulaService.queryList(taxFormula);
        ExcelUtil<TaxFormula> util = new ExcelUtil<TaxFormula>(TaxFormula.class);
        return util.exportExcel(list, "formula" );
    }

    /**
     * 获取计算公式详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:query')" )
    @GetMapping(value = "/{formulaId}" )
    public AjaxResult getInfo(@PathVariable("formulaId" ) Long formulaId) {
        return AjaxResult.success(iTaxFormulaService.getById(formulaId));
    }

    /**
     * 新增计算公式
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:add')" )
    @Log(title = "计算公式" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxFormula taxFormula) {
        return toAjax(iTaxFormulaService.save(taxFormula) ? 1 : 0);
    }

    /**
     * 修改计算公式
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:edit')" )
    @Log(title = "计算公式" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxFormula taxFormula) {
        return toAjax(iTaxFormulaService.updateById(taxFormula) ? 1 : 0);
    }

    /**
     * 删除计算公式
     */
    @PreAuthorize("@ss.hasPermi('tax:formula:remove')" )
    @Log(title = "计算公式" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{formulaIds}" )
    public AjaxResult remove(@PathVariable Long[] formulaIds) {
        return toAjax(iTaxFormulaService.removeByIds(Arrays.asList(formulaIds)) ? 1 : 0);
    }

    @GetMapping("/itemfields/{itemId}")
    public AjaxResult itemFields(@PathVariable Long itemId){
        List<TaxItemField> taxItemFields = taxItemFieldService.queryByItemId(itemId);
        return AjaxResult.success(taxItemFields);
    }
}
