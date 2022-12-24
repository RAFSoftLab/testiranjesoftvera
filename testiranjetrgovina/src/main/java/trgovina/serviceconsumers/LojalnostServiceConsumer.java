package trgovina.serviceconsumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class LojalnostServiceConsumer {
	
	private RestTemplate restTemplate;	
	private String lojalnostServiceBaseUrl;
	
	@Autowired
	public LojalnostServiceConsumer(RestTemplate restTemplate, String lojalnostServiceBaseUrl) {		
		this.restTemplate = restTemplate;
		this.lojalnostServiceBaseUrl = lojalnostServiceBaseUrl;
	}
	
	private final String LOJALNOST_URL_PATH = "/lojalnost"; 
	
	
	private String createURL(String pathEnd) {
		return lojalnostServiceBaseUrl+LOJALNOST_URL_PATH+"/"+pathEnd;
	}
	
	public int vratiPopustZaEmailKupca(String email) {
		 UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("popust"));			
			builder.queryParam("email", email);		
			ResponseEntity<Integer> response = restTemplate.getForEntity(builder.toUriString(), Integer.class, HttpMethod.GET);
		    if(response.getStatusCode()==HttpStatus.OK)
		    	  return response.getBody();
		    else return 0;  	    		  
	}

}
