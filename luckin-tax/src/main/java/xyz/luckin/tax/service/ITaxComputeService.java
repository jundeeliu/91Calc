package xyz.luckin.tax.service;


import xyz.luckin.tax.vo.TaxFeeResultVO;

import java.math.BigDecimal;
import java.util.Map;

public interface ITaxComputeService {
    /**
     * 计算税费
     * @param itemId 项目ID，用于查找项目的计算公式
     * @param fields 提交的计算字段信息
     * @return
     */
    TaxFeeResultVO taxCompute(Long itemId, Map<String, String> fields, String openid) throws Exception;

    /**
     * 保存历史记录
     * @param openid 用户openid
     * @param area 省市区
     * @param community 小区
     * @param taxFeeResultVO 计算结果
     * @return
     */
    boolean createHistory(String openid, String area, String community, TaxFeeResultVO taxFeeResultVO);
}
