package xyz.luckin.tax.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import xyz.luckin.common.exception.CustomException;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.mapper.CalcItemMapper;
import xyz.luckin.tax.domain.CalcItem;
import xyz.luckin.tax.service.ICalcItemService;
import xyz.luckin.tax.service.ITaxItemFieldService;

import java.util.List;
import java.util.Map;

/**
 * 税费计算项目Service业务层处理
 *
 * @author luckin
 * @date 2021-02-19
 */
@Service
public class CalcItemServiceImpl extends ServiceImpl<CalcItemMapper, CalcItem> implements ICalcItemService {

    @Autowired
    private ITaxItemFieldService itemFieldService;

    @Override
    public boolean save(CalcItem calcItem){
        //检查该记录的itemGroup，如果大于1个，说明同级的存在不一样的组
        isSameItemGroup(calcItem);
        return super.save(calcItem);
    }

    @Override
    public boolean updateById(CalcItem calcItem){
        //检查该记录的itemGroup，如果大于1个，说明同级的存在不一样的组
        isSameItemGroup(calcItem);
        return super.updateById(calcItem);
    }

    private void isSameItemGroup(CalcItem calcItem){
        List<String> list = getBaseMapper().itemGroupByParentId(calcItem.getParentId());
        String itemGroup="";
        if(list.size()==0) return;
        if(list.size()==1){
            itemGroup=list.get(0);
        }
//        if(list.size()>1 || !list.contains(calcItem.getItemGroup())){
//            throw new CustomException("同级项目不允许多个项目组，请保持同级项目的组名一致，存在的组名是："+itemGroup);
//        }
    }

    @Override
    public List<CalcItem> queryList(CalcItem calcItem) {
        LambdaQueryWrapper<CalcItem> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(calcItem.getItemName())){
            lqw.like(CalcItem::getItemName ,calcItem.getItemName());
        }
        if (calcItem.getParentId() != null){
            lqw.eq(CalcItem::getParentId ,calcItem.getParentId());
        }
        if (calcItem.getGroupId() != null){
            lqw.eq(CalcItem::getGroupId ,calcItem.getGroupId());
        }
        return this.list(lqw);
    }

    @Override
    @Transactional
    public int setItemField(Long itemId, List<TaxItemField> itemFields) {
        LambdaQueryWrapper<TaxItemField> lqw = Wrappers.lambdaQuery();
        itemFieldService.remove(lqw.eq(TaxItemField::getItemId, itemId));
        return itemFieldService.saveBatch(itemFields)==true?1:0;
    }

    @Override
    public List<CalcItem> queryListByParentId(Long parentId) {
        LambdaQueryWrapper<CalcItem> lqw = Wrappers.lambdaQuery();
        if (parentId != null){
            lqw.eq(CalcItem::getParentId ,parentId);
        }
        return this.list(lqw);
    }
}
