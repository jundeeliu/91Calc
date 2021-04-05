package xyz.luckin.tax.dto;

import lombok.Data;

@Data
public class ItemFieldDTO {
    private Long itemId;
    private Long fieldId;
    private String isRequired;
    private String rowKey;
}
