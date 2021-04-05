package xyz.luckin.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeSelectNode {
    public Long id;
    public String label;
    public List<TreeSelectNode> children=new ArrayList<>();
}
