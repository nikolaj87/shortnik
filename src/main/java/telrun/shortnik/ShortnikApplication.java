package telrun.shortnik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShortnikApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortnikApplication.class, args);
    }

}
