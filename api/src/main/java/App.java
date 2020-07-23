import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = { "com.pbkj.qinmin" })
@Slf4j
@MapperScan(basePackages = {"com.pbkj.qinmin.dao"})
@EnableAsync //开启异步
public class App 
{
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        ConfigurableEnvironment env = context.getEnvironment();
        System.out.println("hello world");
    }
}
