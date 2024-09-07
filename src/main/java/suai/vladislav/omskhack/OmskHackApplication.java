package suai.vladislav.omskhack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OmskHackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmskHackApplication.class, args);
    }
}