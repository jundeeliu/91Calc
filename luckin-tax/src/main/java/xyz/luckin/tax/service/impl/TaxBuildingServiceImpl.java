package xyz.luckin.tax.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import xyz.luckin.tax.mapper.TaxBuildingMapper;
import xyz.luckin.tax.domain.TaxBuilding;
import xyz.luckin.tax.service.ITaxBuildingService;

import java.util.List;
import java.util.Map;

/**
 * 楼栋信息Service业务层处理
 *
 * @author luckin
 * @date 2021-03-30
 */
@Service
public class TaxBuildingServiceImpl extends ServiceImpl<TaxBuildingMapper, TaxBuilding> implements ITaxBuildingService {

    @Override
    public List<TaxBuilding> queryList(TaxBuilding taxBuilding) {
        LambdaQueryWrapper<TaxBuilding> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(taxBuilding.getProvince())){
            lqw.eq(TaxBuilding::getProvince ,taxBuilding.getProvince());
        }
        if (StringUtils.isNotBlank(taxBuilding.getCity())){
            lqw.eq(TaxBuilding::getCity ,taxBuilding.getCity());
        }
        if (StringUtils.isNotBlank(taxBuilding.getDistrict())){
            lqw.eq(TaxBuilding::getDistrict ,taxBuilding.getDistrict());
        }
        if (StringUtils.isNotBlank(taxBuilding.getAddress())){
            lqw.eq(TaxBuilding::getAddress ,taxBuilding.getAddress());
        }
        if (StringUtils.isNotBlank(taxBuilding.getCommunity())){
            lqw.eq(TaxBuilding::getCommunity ,taxBuilding.getCommunity());
        }
        if (StringUtils.isNotBlank(taxBuilding.getBuildingNo())){
            lqw.eq(TaxBuilding::getBuildingNo ,taxBuilding.getBuildingNo());
        }
        return this.list(lqw);
    }
}
