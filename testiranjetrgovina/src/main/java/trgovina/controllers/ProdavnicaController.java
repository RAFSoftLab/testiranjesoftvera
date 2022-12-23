package trgovina.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trgovina.serviceimpl.Prodavnica;
import trgovina.services.KupovinaService;




@RestController
@RequestMapping(path="/prodavnica")
public class ProdavnicaController {
	
	@Autowired
	private KupovinaService kupovina;
	
	
	@PostMapping(path="/otvoriracun")
	public boolean otvoriRacin(Long kupacId) {
		return true; 
	}
	
	@PostMapping(path="/zatvoriracun")
	public boolean zatvoriRacin(Long kupacId) {
		return true; 
	}

	@PostMapping(path="/kupi")
    public boolean kupiJedanProizvod(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam Long idKupca){  
		boolean uspesna = kupovina.kupi(idKupca.intValue(),nazivProizvoda, kolicina);		
    	return uspesna;    	
    }

}
