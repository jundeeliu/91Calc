package xyz.luckin.tax.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcItemGroupVO {
    public String groupName;
    public List<CalcItemVO> list=new ArrayList<>();
}
