package inventar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"inventar"})
public class InventarApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(InventarApp.class);		
		app.run(args);	
	}

}



