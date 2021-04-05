package xyz.luckin.web.controller.tax;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.luckin.common.annotation.Log;
import xyz.luckin.common.core.controller.BaseController;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.core.domain.entity.SysDept;
import xyz.luckin.common.enums.BusinessType;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.tax.domain.CalcItemGroup;
import xyz.luckin.tax.service.ICalcItemGroupService;

import java.util.Arrays;
import java.util.List;

/**
 * 税费计算项目组Controller
 * 
 * @author luckin
 * @date 2021-02-19
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/calcitemgroup" )
public class CalcItemGroupController extends BaseController {

    private final ICalcItemGroupService iCalcItemGroupService;

    /**
     * 查询税费计算项目组列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:list')")
    @GetMapping("/list")
    public AjaxResult list(CalcItemGroup calcItem) {
        List<CalcItemGroup> list = iCalcItemGroupService.queryList(calcItem);
        return AjaxResult.success(list);
    }

    /**
     * 导出税费计算项目组列表
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:export')" )
    @Log(title = "税费计算项目组" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CalcItemGroup calcItem) {
        List<CalcItemGroup> list = iCalcItemGroupService.queryList(calcItem);
        ExcelUtil<CalcItemGroup> util = new ExcelUtil<CalcItemGroup>(CalcItemGroup.class);
        return util.exportExcel(list, "caclitem" );
    }

    /**
     * 获取税费计算项目组详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:query')" )
    @GetMapping(value = "/{itemId}" )
    public AjaxResult getInfo(@PathVariable("itemId" ) Long itemId) {
        CalcItemGroup calcItem = iCalcItemGroupService.getById(itemId);
        AjaxResult ajaxResult=new AjaxResult();
        ajaxResult.put(AjaxResult.DATA_TAG, calcItem);
        return ajaxResult;
    }

    /**
     * 新增税费计算项目组
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:add')" )
    @Log(title = "税费计算项目组" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CalcItemGroup calcItem) {
        return toAjax(iCalcItemGroupService.save(calcItem) ? 1 : 0);
    }

    /**
     * 修改税费计算项目组
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:edit')" )
    @Log(title = "税费计算项目组" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CalcItemGroup calcItem) {
        return toAjax(iCalcItemGroupService.updateById(calcItem) ? 1 : 0);
    }

    /**
     * 删除税费计算项目组
     */
    @PreAuthorize("@ss.hasPermi('tax:calcitemgroup:remove')" )
    @Log(title = "税费计算项目组" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemIds}" )
    public AjaxResult remove(@PathVariable Long[] itemIds) {
        return toAjax(iCalcItemGroupService.removeByIds(Arrays.asList(itemIds)) ? 1 : 0);
    }

    /**
     * 获取项目组下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(CalcItemGroup group)
    {
        List<CalcItemGroup> groups = iCalcItemGroupService.queryList(group);
        return AjaxResult.success(iCalcItemGroupService.buildGroupTreeSelect(groups));
    }

}
