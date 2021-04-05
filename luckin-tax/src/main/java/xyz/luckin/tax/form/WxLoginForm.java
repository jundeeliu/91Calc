package xyz.luckin.tax.form;

import lombok.Data;

/**
 * 微信登录表单
 */
@Data
public class WxLoginForm {
    /**
     * code: "033GNq0w3owo1W2Tpf1w3bqbC62GNq0d"
     * encrypteData: "L211RzMs/Vm3jE4lxHFaAfhikVEq7NETW8tJlPfWmm0qrpBuZPQybeE3LAXYYHCVeUNubJHgo0xjm+Lsl7ZG2AlBAG13jm5A8PYtAphebZ0KmxKPT88JeZx8eX/GABtN2x7dKo2AHva9DBD9Cl03mP8dUpHRQLQofOSNav0CejPIZFzm694kLXN2dkA0kNRfLbBSvmN+3VIahlakTvll54wmryquyRyYQbuotyb6CBPwWQSwE1LaKtUcUh+8RKmP7snOqMCNHjqRs5pobzOrkGX809H/FuXp6l4Mf2KbdC/lSzoLupFowhQGGZP4JdM9cwdwy9Np2RWK8mGYzrC3Smg6GoJkoJCb2QERVgVvhHYlvFOPtuwkMq5lXpEbrC/XvZ5KQwTyQBLhC89I16mzsvm40s/NdNTZHSPv4YMTj4sIYfcJvIwQsiVsrjXNDbDvncDnYctboUmCDrR/B5HmnZXucGKdvATL0EgPUhkkm2OoseDLnRp0R3rywaFoIyh2"
     * iv: "UlTtefb3wf0vy9aoBKcMjg=="
     * rawData: "{"nickName":"大尾巴狼","gender":1,"language":"zh_CN","city":"","province":"Dubai","country":"United Arab Emirates","avatarUrl":"https://thirdwx.qlogo.cn/mmopen/vi_32/x7dO3qq2JzU5mmsSjFkMZwRgHyKxk6XHWkrqsicQkmJKZSDdZia5qV4Pftgj3qTdPfDZ4TRFeiaHjbvqGMhicLnicUg/132"}"
     * signature: "93f1de0815fa49fa9b7e04cbebc5b09979cdd4e4"
     */
    public String code;
    public String encrypteData;
    public String iv;
    public String rawData;
    public String signature;
}
