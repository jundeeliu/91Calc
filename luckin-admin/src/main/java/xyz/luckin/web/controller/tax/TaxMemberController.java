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
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.service.ITaxMemberService;
import xyz.luckin.common.utils.poi.ExcelUtil;
import xyz.luckin.common.core.page.TableDataInfo;

/**
 * 会员信息Controller
 * 
 * @author luckin
 * @date 2021-02-20
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tax/member" )
public class TaxMemberController extends BaseController {

    private final ITaxMemberService iTaxMemberService;

    /**
     * 查询会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('tax:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(TaxMember taxMember) {
        startPage();
        List<TaxMember> list = iTaxMemberService.queryList(taxMember);
        return getDataTable(list);
    }

    /**
     * 导出会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('tax:member:export')" )
    @Log(title = "会员信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TaxMember taxMember) {
        List<TaxMember> list = iTaxMemberService.queryList(taxMember);
        ExcelUtil<TaxMember> util = new ExcelUtil<TaxMember>(TaxMember.class);
        return util.exportExcel(list, "member" );
    }

    /**
     * 获取会员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('tax:member:query')" )
    @GetMapping(value = "/{memberId}" )
    public AjaxResult getInfo(@PathVariable("memberId" ) Long memberId) {
        return AjaxResult.success(iTaxMemberService.getById(memberId));
    }

    /**
     * 新增会员信息
     */
    @PreAuthorize("@ss.hasPermi('tax:member:add')" )
    @Log(title = "会员信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TaxMember taxMember) {
        return toAjax(iTaxMemberService.save(taxMember) ? 1 : 0);
    }

    /**
     * 修改会员信息
     */
    @PreAuthorize("@ss.hasPermi('tax:member:edit')" )
    @Log(title = "会员信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TaxMember taxMember) {
        return toAjax(iTaxMemberService.updateById(taxMember) ? 1 : 0);
    }

    /**
     * 删除会员信息
     */
    @PreAuthorize("@ss.hasPermi('tax:member:remove')" )
    @Log(title = "会员信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}" )
    public AjaxResult remove(@PathVariable Long[] memberIds) {
        return toAjax(iTaxMemberService.removeByIds(Arrays.asList(memberIds)) ? 1 : 0);
    }
}
