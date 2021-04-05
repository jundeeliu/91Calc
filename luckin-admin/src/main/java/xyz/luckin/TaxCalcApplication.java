package xyz.luckin;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动程序
 * 
 * @author luckin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
        PageHelperAutoConfiguration.class},
        scanBasePackages = {"xyz.luckin"})
public class TaxCalcApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        System.setProperty("spring.devtools.restart.enabled", "true");
        SpringApplication.run(TaxCalcApplication.class, args);
        System.out.println("房税计算器--启动成功--");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 设置启动类,用于独立tomcat运行的入口
        return builder.sources(TaxCalcApplication.class);
    }
}
