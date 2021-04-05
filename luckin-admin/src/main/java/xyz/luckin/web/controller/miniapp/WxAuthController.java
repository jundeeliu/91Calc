package xyz.luckin.web.controller.miniapp;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.luckin.common.constant.Constants;
import xyz.luckin.common.core.domain.AjaxResult;
import xyz.luckin.common.utils.StringUtils;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.dto.WxUserDTO;
import xyz.luckin.tax.form.WxLoginForm;
import xyz.luckin.tax.service.ITaxMemberService;
import xyz.luckin.tax.vo.WxUserVO;


/**
 * 鉴权服务
 */
@RestController
@RequestMapping("/wx/auth")
@Validated
@Slf4j
public class WxAuthController extends  BaseWxController {

    @Autowired
    private WxMaService wxService;

    @Autowired
    private ITaxMemberService taxMemberService;



    /**
     * 登陆接口
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody WxLoginForm wxLoginForm) {
        log.info("微信登录表单：{}", wxLoginForm);
        if (StringUtils.isBlank(wxLoginForm.getCode())) {
            return AjaxResult.error("empty jscode");
        }

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(wxLoginForm.getCode());

            log.info(session.getSessionKey());
            log.info(session.getOpenid());

            WxUserDTO userDTO=new WxUserDTO();
            userDTO.setOpenid(session.getOpenid());

            getWxMaUserInfo(userDTO,session.getSessionKey(), wxLoginForm);

            TaxMember taxMember = taxMemberService.wxLogin(userDTO);
            if(taxMember==null){
                return AjaxResult.error("登录失败");
            }else{
                String token = taxMemberService.createToken( session.getOpenid(), session.getSessionKey());
                WxUserVO wxUserVO=new WxUserVO();
                wxUserVO.setNickname(taxMember.getNickname());
                wxUserVO.setPhone(taxMember.getPhone());
                wxUserVO.setAvatar(taxMember.getAvatar());
                wxUserVO.setCreateTime(taxMember.getCreateTime());
                BeanUtils.copyProperties(userDTO, wxUserVO);
                AjaxResult ajaxResult = AjaxResult.success();
                ajaxResult.put(Constants.TOKEN, token);
                ajaxResult.put("userInfo", wxUserVO);
                return ajaxResult;
            }
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return AjaxResult.error(e.toString());
        }
    }

    private void getWxMaUserInfo(WxUserDTO wxUserDTO,String sessionKey, WxLoginForm wxLoginForm){
        if (wxService.getUserService().checkUserInfo(sessionKey, wxLoginForm.getRawData(), wxLoginForm.getSignature())) {
            // 解密用户信息
            WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, wxLoginForm.getEncrypteData(), wxLoginForm.getIv());
            wxUserDTO.setAvatar(userInfo.getAvatarUrl());
            wxUserDTO.setNickname(userInfo.getNickName());
        }
    }


    @GetMapping("/info")
    public AjaxResult info(String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return AjaxResult.error("user check failed");
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return AjaxResult.success(userInfo);
    }
}