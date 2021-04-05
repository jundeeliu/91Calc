package xyz.luckin.tax.converter;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import xyz.luckin.tax.domain.TaxHistory;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.dto.ItemFieldDTO;
import xyz.luckin.tax.vo.TaxFeeResultVO;
import xyz.luckin.tax.vo.TaxHistoryVO;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaxHistoryConverter {
    public static List<TaxHistoryVO> converterToTaxHistoryVO(List<TaxHistory> list ) {
        List<TaxHistoryVO> results = list
                .stream()
                .map(
                        item -> {
                            TaxHistoryVO taxHistoryVO = new TaxHistoryVO();
                            BeanUtils.copyProperties(item, taxHistoryVO);
                            TaxFeeResultVO taxFeeResultVO = JSONUtil.toBean(item.getComputeResult(), TaxFeeResultVO.class);
                            taxHistoryVO.setAddress(item.getArea()+" "+item.getCommunity());
                            taxHistoryVO.setTaxFeeResultVO(taxFeeResultVO);
                            return taxHistoryVO;
                        }
                )
                .collect(Collectors.toList());

        return results;
    }

}
