package xyz.luckin.tax.vo;

import lombok.Data;

@Data
public class HistoryForm {
    public String area;
    public String community;
    public TaxFeeResultVO taxFeeResult;
}
