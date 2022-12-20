package lojalnost;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication(scanBasePackages = {"lojalnost"})
public class LojalnostApp {
	

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LojalnostApp.class);		
		app.run(args);	
	}
	
}
	

