package diplom.problembook;

import diplom.problembook.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProblembookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProblembookApplication.class, args);
	}
}
