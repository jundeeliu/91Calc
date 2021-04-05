package xyz.luckin.tax.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 微信用户VO
 */
@Data
public class WxUserVO {
    public String nickname;
    public String avatar;
    public String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date createTime;
}
