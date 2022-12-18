package trgovina;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TrgovinaServiceConfig {
	
	@Bean
    public RestTemplate getRestTemplate() {
       return new RestTemplate();
    }
    
	@Bean
    public String inventarServiceBaseUrl() {
       return "http://localhost:8081";
    }
	
	

}
