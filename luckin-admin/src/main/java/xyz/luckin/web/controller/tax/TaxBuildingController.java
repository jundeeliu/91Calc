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
import xyz.luckin.tax.domain.TaxBuilding;
import xyz.luckin.tax.service.ITaxBuildingService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 楼栋信息Controller
 * 
 * @author luckin
 * @date 2021-03-30
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/building" )
public class TaxBuildingController extends BaseController {

    private final ITaxBuildingService iTaxBuildingService;

    /**
     * 查询楼栋信息列表
     */
    @PreAuthorize("@ss.hasPermi('tax:building:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxBuilding taxBuilding) {
        startPage();
        List<TaxBuilding> list = iTaxBuildingService.queryList(taxBuilding);
        return getDataTable(list);
    }

    /**
     * 导出楼栋信息列表
     */
    @PreAuthorize("@ss.hasPermi('tax:building:export')" )
    @Log(title = "楼栋信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxBuilding taxBuilding) {
        List<TaxBuilding> list = iTaxBuildingService.queryList(taxBuilding);
        ExcelUtil<TaxBuilding> util = new ExcelUtil<TaxBuilding>(TaxBuilding.class);
        return util.exportExcel(list, "building" );
    }

    /**
     * 获取楼栋信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:building:query')" )
    @GetMapping(value = "/{buildingId}" )
    public AjaxResult getInfo(@PathVariable("buildingId" ) Long buildingId) {
        return AjaxResult.success(iTaxBuildingService.getById(buildingId));
    }

    /**
     * 新增楼栋信息
     */
    @PreAuthorize("@ss.hasPermi('tax:building:add')" )
    @Log(title = "楼栋信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxBuilding taxBuilding) {
        return toAjax(iTaxBuildingService.save(taxBuilding) ? 1 : 0);
    }

    /**
     * 修改楼栋信息
     */
    @PreAuthorize("@ss.hasPermi('tax:building:edit')" )
    @Log(title = "楼栋信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxBuilding taxBuilding) {
        return toAjax(iTaxBuildingService.updateById(taxBuilding) ? 1 : 0);
    }

    /**
     * 删除楼栋信息
     */
    @PreAuthorize("@ss.hasPermi('tax:building:remove')" )
    @Log(title = "楼栋信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{buildingIds}" )
    public AjaxResult remove(@PathVariable Long[] buildingIds) {
        return toAjax(iTaxBuildingService.removeByIds(Arrays.asList(buildingIds)) ? 1 : 0);
    }
}
