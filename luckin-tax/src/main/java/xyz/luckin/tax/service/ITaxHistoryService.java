package xyz.luckin.tax.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxMember;
import xyz.luckin.tax.dto.WxUserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 计算历史Service接口
 *
 * @author luckin
 * @date 2021-02-20
 */
public interface ITaxHistoryService extends IService<TaxHistory> {

    /**
     * 查询列表
     */
    List<TaxHistory> queryList(TaxHistory taxHistory);

    List<TaxHistory> queryByOpenid(String openid);

    TaxHistory getById(Long historyId, String openid);

}
