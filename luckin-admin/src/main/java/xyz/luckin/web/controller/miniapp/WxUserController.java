package xyz.luckin.web.controller.miniapp;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.luckin.common.config.LuckinCRMConfig;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.core.domain.model.LoginUser;
import xyz.luckin.common.exception.CustomException;
import xyz.luckin.common.utils.DateUtils;
import xyz.luckin.common.utils.ServletUtils;
import xyz.luckin.common.utils.file.FileUploadUtils;
import xyz.luckin.tax.converter.TaxHistoryConverter;
import xyz.luckin.tax.domain.TaxFeedback;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.domain.TaxReport;
import xyz.luckin.tax.form.ReportForm;
import xyz.luckin.tax.service.ITaxFeedbackService;
import xyz.luckin.tax.service.ITaxHistoryService;
import xyz.luckin.tax.service.ITaxMemberService;
import xyz.luckin.tax.service.ITaxReportService;
import xyz.luckin.tax.vo.TaxFeeResultVO;
import xyz.luckin.tax.vo.TaxHistoryVO;
import xyz.luckin.tax.vo.WxUserVO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 微信用户控制器
 */
@RestController
@RequestMapping("/wx/user")
@Validated
@Slf4j
public class WxUserController {
    @Autowired
    private ITaxMemberService memberService;

    @Autowired
    private ITaxHistoryService taxHistoryService;

    @Autowired
    private ITaxFeedbackService taxFeedbackService;

    @Autowired
    private ITaxReportService taxReportService;

    private TaxMember getLoginUser(HttpServletRequest request){
        TaxMember loginUser = memberService.getLoginUser(request);
        if (loginUser ==null) {
            throw new CustomException("用户未登录");
        }
        return loginUser;
    }

    @GetMapping("/info")
    public AjaxResult getUserInfo(HttpServletRequest request) {
        TaxMember loginUser = getLoginUser(request);
        WxUserVO userVO = new WxUserVO();
        userVO.setNickname(loginUser.getNickname());
        userVO.setAvatar(loginUser.getAvatar());
        userVO.setCreateTime(loginUser.getCreateTime());
        userVO.setPhone(loginUser.getPhone());
        return AjaxResult.success(userVO);
    }

    @GetMapping("/history")
    public AjaxResult getHistory(HttpServletRequest request){
        TaxMember taxMember=getLoginUser(request);
        List<TaxHistory> list = taxHistoryService.queryByOpenid(taxMember.getOpenid());
        List<TaxHistoryVO> historyVOList = TaxHistoryConverter.converterToTaxHistoryVO(list);
        return AjaxResult.success(historyVOList);
    }

    @GetMapping("/history/{historyId}")
    public AjaxResult getHistoryDetail(HttpServletRequest request,@PathVariable("historyId")Long historyId){
        TaxMember taxMember=getLoginUser(request);
        TaxHistory history = taxHistoryService.getById(historyId, taxMember.getOpenid());
        if(history!=null){
            TaxFeeResultVO taxFeeResultVO = JSONUtil.toBean(history.getComputeResult(), TaxFeeResultVO.class);
            return AjaxResult.success(taxFeeResultVO);
        }
        return AjaxResult.error();
    }

    /**
     * 问题反馈
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/feedback")
    public AjaxResult postFeedback(HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file) throws IOException {

        TaxFeedback taxFeedback = new TaxFeedback();

        try {
            //允许不登录的情况下提交问题
            TaxMember taxMember = getLoginUser(request);
            taxFeedback.setOpenid(taxMember.getOpenid());
            taxFeedback.setCreateTime(DateUtils.getNowDate());
            taxFeedback.setCreateBy(taxMember.getNickname());
        }catch (Exception ex){

        }

        String content = request.getParameter("content");
        taxFeedback.setContent(content);
        if (!file.isEmpty()) {
            String image = FileUploadUtils.upload(LuckinCRMConfig.getProfile() + "/feedback", file);
            taxFeedback.setImageFiles(image);
        }

        return taxFeedbackService.save(taxFeedback)?AjaxResult.success(): AjaxResult.error();
    }

    @PostMapping("/report")
    public AjaxResult postTaxReport(HttpServletRequest request, @RequestBody ReportForm reportForm) throws IOException {
        TaxMember taxMember = getLoginUser(request);

        TaxReport taxReport = new TaxReport();
        taxReport.setOpenid(taxMember.getOpenid());
        taxReport.setAddress(reportForm.getAddress());
        taxReport.setArea(reportForm.getArea());
        taxReport.setEvalPrice(reportForm.getEvalPrice());
        taxReport.setEvalAmount(reportForm.getEvalPrice());
        String images = JSONUtil.toJsonStr(reportForm.getImages());
        taxReport.setImageFiles(images);
        taxReport.setStatus("N");
        taxReport.setCreateTime(DateUtils.getNowDate());
        taxReport.setCreateBy(taxMember.getNickname());
        taxReportService.save(taxReport);
        return AjaxResult.success();
    }

    /**
     * 上报图片返回地址
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload-image")
    public AjaxResult uploadImage(HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String image = FileUploadUtils.upload(LuckinCRMConfig.getProfile(), file);
            AjaxResult ajaxResult=AjaxResult.success();
            ajaxResult.put(AjaxResult.DATA_TAG,image);
            return ajaxResult;
        }
        return AjaxResult.error("未选择图片");
    }
}
