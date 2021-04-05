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
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.service.ITaxHistoryService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 用户计算历史Controller
 * 
 * @author luckin
 * @date 2021-03-30
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/history" )
public class TaxHistoryController extends BaseController {

    private final ITaxHistoryService iTaxHistoryService;

    /**
     * 查询用户计算历史列表
     */
    @PreAuthorize("@ss.hasPermi('tax:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxHistory taxHistory) {
        startPage();
        List<TaxHistory> list = iTaxHistoryService.queryList(taxHistory);
        return getDataTable(list);
    }

    /**
     * 导出用户计算历史列表
     */
    @PreAuthorize("@ss.hasPermi('tax:history:export')" )
    @Log(title = "用户计算历史" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxHistory taxHistory) {
        List<TaxHistory> list = iTaxHistoryService.queryList(taxHistory);
        ExcelUtil<TaxHistory> util = new ExcelUtil<TaxHistory>(TaxHistory.class);
        return util.exportExcel(list, "history" );
    }

    /**
     * 获取用户计算历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:history:query')" )
    @GetMapping(value = "/{historyId}" )
    public AjaxResult getInfo(@PathVariable("historyId" ) Long historyId) {
        return AjaxResult.success(iTaxHistoryService.getById(historyId));
    }

    /**
     * 新增用户计算历史
     */
    @PreAuthorize("@ss.hasPermi('tax:history:add')" )
    @Log(title = "用户计算历史" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxHistory taxHistory) {
        return toAjax(iTaxHistoryService.save(taxHistory) ? 1 : 0);
    }

    /**
     * 修改用户计算历史
     */
    @PreAuthorize("@ss.hasPermi('tax:history:edit')" )
    @Log(title = "用户计算历史" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxHistory taxHistory) {
        return toAjax(iTaxHistoryService.updateById(taxHistory) ? 1 : 0);
    }

    /**
     * 删除用户计算历史
     */
    @PreAuthorize("@ss.hasPermi('tax:history:remove')" )
    @Log(title = "用户计算历史" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{historyIds}" )
    public AjaxResult remove(@PathVariable Long[] historyIds) {
        return toAjax(iTaxHistoryService.removeByIds(Arrays.asList(historyIds)) ? 1 : 0);
    }
}
