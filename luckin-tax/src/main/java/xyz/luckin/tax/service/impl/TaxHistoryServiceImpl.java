package xyz.luckin.tax.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.luckin.common.utils.DateUtils;
import xyz.luckin.tax.domain.TaxFormula;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.dto.WxUserDTO;
import xyz.luckin.tax.mapper.TaxHistoryMapper;
import xyz.luckin.tax.mapper.TaxHistoryMapper;
import xyz.luckin.tax.service.ITaxHistoryService;
import xyz.luckin.tax.service.ITaxHistoryService;

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
public class TaxHistoryServiceImpl extends ServiceImpl<TaxHistoryMapper, TaxHistory> implements ITaxHistoryService {


    @Override
    public List<TaxHistory> queryList(TaxHistory taxHistory) {
        LambdaQueryWrapper<TaxHistory> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(taxHistory.getOpenid())){
            lqw.like(TaxHistory::getOpenid ,taxHistory.getOpenid());
        }
        lqw.orderByDesc(TaxHistory::getCreateTime);
        return this.list(lqw);
    }

    @Override
    public List<TaxHistory> queryByOpenid(String openid) {
        TaxHistory history=new TaxHistory();
        history.setOpenid(openid);

        List<TaxHistory> list = queryList(history);
        return list;
    }

    @Override
    public TaxHistory getById(Long historyId, String openid) {

        return getBaseMapper().getById(historyId, openid);
    }


}
