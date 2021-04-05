package xyz.luckin.tax.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaxHistoryVO {
    private Long historyId;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    private TaxFeeResultVO taxFeeResultVO;
}
