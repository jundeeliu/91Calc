package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxMember;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.luckin.tax.dto.WxUserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 会员信息Service接口
 *
 * @author luckin
 * @date 2021-02-20
 */
public interface ITaxMemberService extends IService<TaxMember> {

    /**
     * 查询列表
     */
    List<TaxMember> queryList(TaxMember taxMember);

    TaxMember wxLogin(WxUserDTO wxUserDTO);

    /**
     * 根据openid获取会员信息
     * @param openid
     * @return
     */
    TaxMember getByOpenid(String openid);

    String createToken(String openid, String sessionKey);

    TaxMember getLoginUser(HttpServletRequest request);
}
