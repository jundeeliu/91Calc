package xyz.luckin.tax.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReportForm {
    public String address;
    public BigDecimal area;
    public BigDecimal evalAmount;
    public BigDecimal evalPrice;
    public List<String> images=new ArrayList<>();
}
