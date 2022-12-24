package trgovina.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trgovina.main.KupovinaService;
import trgovina.model.Racun;


@RestController
@RequestMapping(path="/prodavnica")
public class ProdavnicaController {
	
	@Autowired
	private KupovinaService kupovinaService;
	
	
	@PostMapping(path="/otvoriracun/{kupacId}")
	public String otvoriRacin(@PathVariable int kupacId) {
		return kupovinaService.otvoriRacun(kupacId);		 
	}
	
	@PostMapping(path="/zatvoriracun/{racunId}")
	public String zatvoriRacin(@PathVariable String racunId) {
		return kupovinaService.zatvoriRacun(racunId); 
	}
	
	@GetMapping(path="/racun/{racunId}")
	public Racun vratiRacun(@PathVariable String racunId) {
		return kupovinaService.vratiRacunZaId(racunId);
	}
		
	@PostMapping(path="/kupi")
    public boolean kupiProizvod(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam Long idKupca){  
		boolean uspesna = kupovinaService.kupi(idKupca.intValue(),nazivProizvoda, kolicina);		
    	return uspesna;    	
    }
	
	
	@PostMapping(path="/dodajnaracun")
    public boolean dodajNaRacun(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam String idRacuna){  
		boolean uspesna = kupovinaService.dodajNaAktivanRacun(idRacuna, nazivProizvoda, kolicina);		
    	return uspesna;   	
    }

}
