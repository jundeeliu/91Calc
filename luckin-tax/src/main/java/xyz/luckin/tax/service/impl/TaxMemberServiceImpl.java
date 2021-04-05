package xyz.luckin.tax.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.common.constant.Constants;
import xyz.luckin.common.utils.DateUtils;
import xyz.luckin.tax.dto.WxUserDTO;
import xyz.luckin.tax.mapper.TaxMemberMapper;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.service.ITaxMemberService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员信息Service业务层处理
 *
 * @author luckin
 * @date 2021-02-20
 */
@Service
public class TaxMemberServiceImpl extends ServiceImpl<TaxMemberMapper, TaxMember> implements ITaxMemberService {

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    public static final String WX_LOGIN_USER_KEY = "wx_user_login_key";

    @Override
    public List<TaxMember> queryList(TaxMember taxMember) {
        LambdaQueryWrapper<TaxMember> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(taxMember.getNickname())){
            lqw.like(TaxMember::getNickname ,taxMember.getNickname());
        }
        return this.list(lqw);
    }

    @Override
    public TaxMember wxLogin(WxUserDTO wxUserDTO) {
        TaxMember taxMember = getByOpenid(wxUserDTO.getOpenid());
        if(taxMember==null){
            taxMember=new TaxMember();
            taxMember.setOpenid(wxUserDTO.getOpenid());
            taxMember.setNickname(wxUserDTO.getNickname());
            taxMember.setAvatar(wxUserDTO.getAvatar());
            taxMember.setCreateTime(DateUtils.getNowDate());
            getBaseMapper().insert(taxMember);
        }else{
            taxMember.setNickname(wxUserDTO.getNickname());
            taxMember.setAvatar(wxUserDTO.getAvatar());
            taxMember.setUpdateTime(DateUtils.getNowDate());
            getBaseMapper().updateById(taxMember);
        }
        return taxMember;
    }


    @Override
    public TaxMember getByOpenid(String openid){
        if (StringUtils.isBlank(openid)) return null;
        LambdaQueryWrapper<TaxMember> lqw = Wrappers.lambdaQuery();
        lqw.like(TaxMember::getOpenid ,openid);
        TaxMember taxMember = this.getOne(lqw);
        return taxMember;
    }

    @Override
    public String createToken(String openid, String sessionKey)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(WX_LOGIN_USER_KEY, openid);
        claims.put("session_key", sessionKey);


        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,secret).compact();
        return token;
    }

    public Claims parseToken(String token)
    {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    @Override
    public TaxMember getLoginUser(HttpServletRequest request)
    {
        String token = getToken(request);
        if(StringUtils.isNotBlank(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String openid = (String) claims.get(WX_LOGIN_USER_KEY);
            TaxMember taxMember = getByOpenid(openid);
            return taxMember;
        }
        return null;
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader("token");
        return token;
    }
}
