package xyz.luckin.web.controller.tax;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.luckin.common.annotation.Log;
import xyz.luckin.common.core.controller.BaseController;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.enums.BusinessType;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.tax.converter.ItemFieldConverter;
import xyz.luckin.tax.domain.CalcItem;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.dto.ItemFieldDTO;
import xyz.luckin.tax.service.ICalcItemService;
import xyz.luckin.tax.service.ITaxItemFieldService;

import java.util.Arrays;
import java.util.List;

/**
 * 税费计算项目Controller
 *
 * @author luckin
 * @date 2021-02-19
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/calcitem" )
public class CalcItemController extends BaseController {

    private final ICalcItemService iCalcItemService;

    private final ITaxItemFieldService iTaxItemFieldService;


    /**
     * 查询税费计算项目列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:list')")
    @GetMapping("/list")
    public AjaxResult list(CalcItem calcItem) {
        List<CalcItem> list = iCalcItemService.queryList(calcItem);
        return AjaxResult.success(list);
    }

    /**
     * 导出税费计算项目列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:export')" )
    @Log(title = "税费计算项目" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CalcItem calcItem) {
        List<CalcItem> list = iCalcItemService.queryList(calcItem);
        ExcelUtil<CalcItem> util = new ExcelUtil<CalcItem>(CalcItem.class);
        return util.exportExcel(list, "caclitem" );
    }

    /**
     * 获取税费计算项目详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:query')" )
    @GetMapping(value = "/{itemId}" )
    public AjaxResult getInfo(@PathVariable("itemId" ) Long itemId) {
        CalcItem calcItem = iCalcItemService.getById(itemId);
        AjaxResult ajaxResult=new AjaxResult();
        ajaxResult.put(AjaxResult.DATA_TAG, calcItem);
        ajaxResult.put("itemFields",iTaxItemFieldService.queryByItemId(itemId));
        return ajaxResult;
    }

    /**
     * 新增税费计算项目
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:add')" )
    @Log(title = "税费计算项目" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CalcItem calcItem) {
        return toAjax(iCalcItemService.save(calcItem) ? 1 : 0);
    }

    /**
     * 修改税费计算项目
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:edit')" )
    @Log(title = "税费计算项目" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CalcItem calcItem) {
        return toAjax(iCalcItemService.updateById(calcItem) ? 1 : 0);
    }

    /**
     * 设置项目字段
     * @return
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:edit')" )
    @Log(title = "设置项目字段" , businessType = BusinessType.UPDATE)
    @PostMapping("/fields/{itemId}")
    public AjaxResult setItemField(@PathVariable("itemId") Long itemId, @RequestBody List<ItemFieldDTO> itemFieldDTOS) {
        List<TaxItemField> taxItemFieldList = ItemFieldConverter.converterToItemFieldDTO(itemFieldDTOS);
        return toAjax(iCalcItemService.setItemField(itemId, taxItemFieldList));
    }

    /**
     * 删除税费计算项目
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitem:remove')" )
    @Log(title = "税费计算项目" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemIds}" )
    public AjaxResult remove(@PathVariable Long[] itemIds) {
        return toAjax(iCalcItemService.removeByIds(Arrays.asList(itemIds)) ? 1 : 0);
    }
}
