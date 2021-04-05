package xyz.luckin.tax.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcItemFieldVO {
    public Long itemId;
    public Long fieldId;
    public String fieldType;
    public String fieldLabel;
}
