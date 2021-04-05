package xyz.luckin.tax.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.luckin.common.core.domain.TreeSelect;
import xyz.luckin.common.core.domain.TreeSelectNode;
import xyz.luckin.tax.domain.CalcItemGroup;
import xyz.luckin.tax.mapper.CalcItemGroupMapper;
import xyz.luckin.tax.service.ICalcItemGroupService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 税费计算项目组Service业务层处理
 *
 * @author luckin
 * @date 2021-02-19
 */
@Service
public class CalcItemGroupServiceImpl extends ServiceImpl<CalcItemGroupMapper, CalcItemGroup> implements ICalcItemGroupService {
    

    @Override
    public List<CalcItemGroup> queryList(CalcItemGroup calcItemGroup) {
        LambdaQueryWrapper<CalcItemGroup> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(calcItemGroup.getGroupName())){
            lqw.like(CalcItemGroup::getGroupName ,calcItemGroup.getGroupName());
        }
        if (calcItemGroup.getParentId() != null){
            lqw.eq(CalcItemGroup::getParentId ,calcItemGroup.getParentId());
        }
        return this.list(lqw);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param groups 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildGroupTreeSelect(List<CalcItemGroup> groups)
    {
//        List<TreeSelectNode> groupTrees = buildGroupTree(groups);
//
//        return groupTrees.stream().map(TreeSelect::new).collect(Collectors.toList());

        //获取父节点
        List<TreeSelectNode> collect = groups.stream().filter(m -> m.getParentId() == 0).map(m -> {
                    TreeSelectNode selectNode=new TreeSelectNode();
                    selectNode.setId(m.getGroupId());
                    selectNode.setLabel(m.getGroupName());
                    selectNode.setChildren(getChildrens(m, groups));
                    return selectNode;
                }
        ).collect(Collectors.toList());

        return collect.stream().map(TreeSelect::new).collect(Collectors.toList());

    }


    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    private List<TreeSelectNode> getChildrens(CalcItemGroup root, List<CalcItemGroup> all) {
        List<TreeSelectNode> children = all.stream().filter(m -> {
            return Objects.equals(m.getParentId(), root.getGroupId());
        }).map(
                (m) -> {
                    TreeSelectNode selectNode=new TreeSelectNode();
                    selectNode.setId(m.getGroupId());
                    selectNode.setLabel(m.getGroupName());
                    selectNode.setChildren(getChildrens(m, all));
                    return selectNode;
                }
        ).collect(Collectors.toList());
        return children;
    }

}
