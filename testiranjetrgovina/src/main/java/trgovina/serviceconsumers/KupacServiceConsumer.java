package trgovina.serviceconsumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import trgovina.dtos.KupacDTO;

@RestController
public class KupacServiceConsumer {
	
	private RestTemplate restTemplate;	
	private String kupacServiceBaseUrl;
	
	@Autowired
	public KupacServiceConsumer(RestTemplate restTemplate, String kupacServiceBaseUrl) {		
		this.restTemplate = restTemplate;
		this.kupacServiceBaseUrl = kupacServiceBaseUrl;
	}
	
	private final String KUPAC_URL_PATH = "/kupac"; 
	
	
	private String createURL(String pathEnd) {
		return kupacServiceBaseUrl+KUPAC_URL_PATH+"/"+pathEnd;
	}
	
	public KupacDTO vratiKupcaZaId(Long id) {
		KupacDTO kupac = restTemplate.getForObject(createURL(String.valueOf(id)), KupacDTO.class);
		return kupac;
	}
	
	
	

}
