package kupac;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication(scanBasePackages = {"kupac"})
public class KupacApp {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KupacApp.class);		
		app.setDefaultProperties(Collections.singletonMap("server.port", "8082"));
		app.run(args);	
	}
	
	

}
