package xyz.luckin.tax.service;

import xyz.luckin.tax.domain.TaxBuilding;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 楼栋信息Service接口
 *
 * @author luckin
 * @date 2021-03-30
 */
public interface ITaxBuildingService extends IService<TaxBuilding> {

    /**
     * 查询列表
     */
    List<TaxBuilding> queryList(TaxBuilding taxBuilding);
}
