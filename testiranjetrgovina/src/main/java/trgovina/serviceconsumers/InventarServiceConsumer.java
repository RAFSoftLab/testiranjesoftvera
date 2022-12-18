package trgovina.serviceconsumers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import trgovina.dtos.ProizvodDTO;

@RestController
public class InventarServiceConsumer {
	
	private RestTemplate restTemplate;	
	private String inventarServiceBaseUrl;
	
	@Autowired
	public InventarServiceConsumer(RestTemplate restTemplate, String inventarServiceBaseUrl) {
		super();
		this.restTemplate = restTemplate;
		this.inventarServiceBaseUrl = inventarServiceBaseUrl;
	}
	
	private final String INVENTAR_URL_PATH = "/inventar"; 
	
	
	private String createURL(String pathEnd) {
		return inventarServiceBaseUrl+INVENTAR_URL_PATH+"/"+pathEnd;
	}
	
	public List<ProizvodDTO> vratiSveProizvode() {  	      	
		ProizvodDTO[] retVal = restTemplate.getForObject(createURL("all"), ProizvodDTO[].class);		
		return Arrays.asList(retVal);
	}
	
	
	
	

}
