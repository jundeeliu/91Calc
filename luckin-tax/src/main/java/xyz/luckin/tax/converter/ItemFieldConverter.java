package xyz.luckin.tax.converter;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import xyz.luckin.tax.domain.TaxItemField;
import xyz.luckin.tax.dto.ItemFieldDTO;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemFieldConverter {
    public static List<TaxItemField> converterToItemFieldDTO(List<ItemFieldDTO> list ) {
        List<TaxItemField> results = list
                .stream()
                .map(
                        item -> {
                            TaxItemField itemField = new TaxItemField();
                            BeanUtils.copyProperties(item, itemField);
                            return itemField;
                        }
                )
                .collect(Collectors.toList());

        return results;
    }

    public static List<ItemFieldDTO> converter(List<TaxItemField> list ) {
        List<ItemFieldDTO> results = list
                .stream()
                .map(
                        item -> {
                            ItemFieldDTO dto = new ItemFieldDTO();
                            BeanUtils.copyProperties(item, dto);
                            dto.setRowKey(item.getItemId()+"_"+item.getFieldId());
                            return dto;
                        }
                )
                .collect(Collectors.toList());

        return results;
    }
}
