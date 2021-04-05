package xyz.luckin.tax.vo;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcItemVO {
    public Long id;
    public Long parentId;
    public String label;
    public String groupName;
}
