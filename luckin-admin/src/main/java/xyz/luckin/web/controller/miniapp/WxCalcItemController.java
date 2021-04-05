package xyz.luckin.web.controller.miniapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.exception.CustomException;
import xyz.luckin.tax.domain.CalcItem;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.service.ICalcItemService;
import xyz.luckin.tax.service.ITaxComputeService;
import xyz.luckin.tax.service.ITaxItemFieldService;
import xyz.luckin.tax.service.ITaxMemberService;
import xyz.luckin.tax.vo.CalcItemGroupVO;
import xyz.luckin.tax.vo.CalcItemVO;
import xyz.luckin.tax.vo.HistoryForm;
import xyz.luckin.tax.vo.TaxFeeResultVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/calcitem")
@Validated
@Slf4j
public class WxCalcItemController {

    @Autowired
    private ICalcItemService calcItemService;

    @Autowired
    private ITaxItemFieldService taxItemFieldService;

    @Autowired
    private ITaxComputeService taxComputeService;

    @Autowired
    private ITaxMemberService memberService;
    
    @RequestMapping("/list")
    public AjaxResult listCalcItem(Long parentId){

        List<CalcItem> calcItems = calcItemService.queryListByParentId(parentId);
        List<CalcItemVO> voList = calcItems.stream().map(e -> {
            CalcItemVO vo = new CalcItemVO();
            vo.setId(e.getItemId());
            vo.setLabel(e.getItemName());
            vo.setParentId(e.getParentId());
            vo.setGroupName(e.getItemGroup());
            return vo;
        }).collect(Collectors.toList());
        Map<String, List<CalcItemVO>> map = voList.stream().collect(Collectors.groupingBy(CalcItemVO::getGroupName));
        CalcItemGroupVO groupVO=null;
        AjaxResult ajaxResult=new AjaxResult();
        if(map.size()==1) {
            groupVO=new CalcItemGroupVO();
            String s = map.keySet().stream().findFirst().get();
            List<CalcItemVO> calcItemVOS = map.get(map.keySet().toArray()[0]);
            groupVO.setGroupName(s);
            groupVO.setList(calcItemVOS);
        }
        ajaxResult.put(AjaxResult.DATA_TAG,groupVO );
        ajaxResult.put("itemFields",taxItemFieldService.queryByItemId(parentId));
        return ajaxResult;
    }

    /**
     * 计算
     * @return
     */
    @PostMapping("/tax-compute/{itemId}")
    public AjaxResult taxCompute(HttpServletRequest request,  @PathVariable("itemId")Long itemId, @RequestBody Map<String, String> formFields){
        log.info("itemId:{}", itemId);
        log.info("formFields:{}", formFields);
        String openid="";

        try {
            TaxMember taxMember =memberService.getLoginUser(request);
            if(taxMember!=null)
                openid=taxMember.getOpenid();
        }catch (Exception ex){}
        TaxFeeResultVO taxFeeResultVO = null;
//        try {
        try {
            taxFeeResultVO = taxComputeService.taxCompute(itemId, formFields, openid);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } catch (Exception e) {
//            throw new CustomException("计算失败："+e.getMessage());
//        }
        return AjaxResult.success(taxFeeResultVO);
    }

    @PostMapping("/tax-compute/history")
    public AjaxResult saveHistory(HttpServletRequest request, @RequestBody HistoryForm historyForm){
        String openid="";

        try {
            TaxMember taxMember =memberService.getLoginUser(request);
            if(taxMember!=null)
                openid=taxMember.getOpenid();
        }catch (Exception ex){}
        boolean history = taxComputeService.createHistory(openid, historyForm.getArea(), historyForm.getCommunity(), historyForm.getTaxFeeResult());
        return history? AjaxResult.success("历史保存成功") : AjaxResult.error();

    }
}
