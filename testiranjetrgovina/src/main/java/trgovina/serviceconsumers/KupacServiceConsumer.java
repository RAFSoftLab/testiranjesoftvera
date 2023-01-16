package trgovina.serviceconsumers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.KupovinaDTO;
import trgovina.dtos.ProizvodDTO;
import trgovina.dtos.UplataDTO;

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
	
	public List<KupovinaDTO> vratiKupovineZaIdKupca(Long id) {
		KupovinaDTO[] retVal = restTemplate.getForObject(createURL(String.valueOf(id)+"/kupovine"), KupovinaDTO[].class);
		return Arrays.asList(retVal);
	}
	
	
	public void obrisiKupovinu(Long idKupovine) {
		restTemplate.delete(createURL("kupovine"+"/"+String.valueOf(idKupovine)));
	}
	
	public String vratiTekuciRacunZaIdKupca(Long id) {
		String retVal = restTemplate.getForObject(createURL(String.valueOf(id)+"/tekuciracun"), String.class);
		return retVal;
	}
	
	public List<UplataDTO> vratiStrukturuUplate(Long idKupca, double iznosZaUplatu){
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("strukturauplate"));			
		builder.queryParam("idKupca", idKupca);
		builder.queryParam("iznosZaUplatu", iznosZaUplatu);
		ResponseEntity<UplataDTO[]> response = restTemplate.getForEntity(builder.toUriString(), UplataDTO[].class, HttpMethod.GET);
		if(response.getStatusCode()==HttpStatus.OK)
			if(response.getBody()==null)
				return null;
			else
		   	  return Arrays.asList(response.getBody());
		else return null;  	    		  
	}
	
	public void smanjiStanjeNaRacunu(String tekuciRacun, double umanjenje) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("smanjistanje"));			
		builder.queryParam("tekuciRacun", tekuciRacun);
		builder.queryParam("umanjenje", umanjenje);
		restTemplate.put(builder.toUriString(),null);		
	}
	
	public void povecajStanjeNaRacunu(String tekuciRacun, double uvecanje) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("povecajstanje"));			
		builder.queryParam("tekuciRacun", tekuciRacun);
		builder.queryParam("uvecanje", uvecanje);
		restTemplate.put(builder.toUriString(),null);		
	}
	
	public void dodajRezervisanoZaKupca(Long kupacId, double iznosZaRezervisanje) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("dodajrezervisano"));			
		builder.queryParam("kupacId", kupacId);
		builder.queryParam("iznosZaRezervisanje", iznosZaRezervisanje);
		restTemplate.put(builder.toUriString(),null);		
	}
	
	public void sacuvajKupovinu(KupovinaDTO kupovina) {
		restTemplate.postForEntity(createURL("sacuvajkupovinu"), kupovina, null);
	}
	
	public KupovinaDTO vratiKupovinuZaRacunId(String racunId) {
		ResponseEntity<KupovinaDTO> response = restTemplate.getForEntity(createURL("kupovina/"+racunId), KupovinaDTO.class, HttpMethod.GET);
		if(response.getStatusCode()==HttpStatus.OK)
		   	return response.getBody();
		else 
			return null;  	    		  
		
	}
	
	
	
	

}
