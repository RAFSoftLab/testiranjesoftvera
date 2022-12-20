package trgovina.serviceconsumers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
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
	
	public ProizvodDTO vratiProizvodePoNazivu(String naziv) { 	 
	    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("proizvod"));			
		builder.queryParam("naziv", naziv);		
		ResponseEntity<ProizvodDTO> response = restTemplate.getForEntity(builder.toUriString(), ProizvodDTO.class, HttpMethod.GET);
	    if(response.getStatusCode()==HttpStatus.OK)
	    	  return response.getBody();
	    else return null;  	    		  
		
	}
	
	public void azurirajStanjeInventaraAsinh(String nazivProizvda, int umanjenje) {  	      	
	      UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("umanji"));			
		  builder.queryParam("naziv", nazivProizvda);
		  builder.queryParam("umanjenje", umanjenje);		  
		  Flux<Boolean> fluxUmanjiInventar =  WebClient.create()
				  .put()
				  .uri(builder.toUriString())
				  .retrieve()
				  .bodyToFlux(Boolean.class);		  
		  fluxUmanjiInventar.subscribe(uspesno->System.out.println("Umanjenje uspesno: "+uspesno));	        	    		  
	}
	
	
	
	

}
