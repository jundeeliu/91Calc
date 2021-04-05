package xyz.luckin.formula;


import com.googlecode.aviator.AviatorEvaluator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FormulaUtils {
    public static void main(String[] args) {
//        String yourName = "Michael";
//        Map<String, Object> env = new HashMap<String, Object>();
//        env.put("数量", 3);
//        env.put("单价", 10.2);
//        env.put("运费", 70);
//        Double result = (Double)AviatorEvaluator.execute("单价*数量+运费", env);
//        System.out.println(result);




        Map<String, Object> env2 = new HashMap<String, Object>();
        env2.put("面积", 3);
        env2.put("地税评估金额", "8");
        env2.put("购房发票金额", "32344");
        env2.put("购房发票日期", "48765");
        BigDecimal result2 = (BigDecimal)AviatorEvaluator.execute("面积*12/100", env2);
        System.out.println(result2);
    }
}
