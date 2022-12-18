package inventar;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"inventar"})
public class InventarApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(InventarApp.class);		
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);	
	}

}



