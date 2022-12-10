package trgovina;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"trgovina"})
public class TrgovinaApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TrgovinaApp.class);		
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
		app.run(args);	
	}

}



