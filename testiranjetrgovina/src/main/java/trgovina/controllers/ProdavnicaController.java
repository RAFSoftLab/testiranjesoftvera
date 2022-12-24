package trgovina.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.InventarException;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
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
		try {
			return kupovinaService.zatvoriRacun(racunId);
		} catch (NedozvoljenaOperacijaNadRacunomException e) {			
			e.printStackTrace();
			// TODO log
			return null;
		} 
	}
	
	@GetMapping(path="/racun/{racunId}")
	public Racun vratiRacun(@PathVariable String racunId) {
		return kupovinaService.vratiRacunZaId(racunId);
	}
		
	@PostMapping(path="/kupi")
    public boolean kupiProizvod(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam Long idKupca){		
		try {
			return kupovinaService.kupi(idKupca.intValue(),nazivProizvoda, kolicina);
		} catch (InventarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
    	  	
    }
	
	
	@PostMapping(path="/dodajnaracun")
    public boolean dodajNaRacun(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam String idRacuna){		
		try {
			return kupovinaService.dodajNaAktivanRacun(idRacuna, nazivProizvoda, kolicina);
		} catch (InventarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
    	 	
    }
	
	
	/**
	 * izdaje racun za gotovu kupovinu, sa cenama
	 * @param racunId
	 * @return
	 */
	@GetMapping(path="/izdajracun/{racunId}")
	public RacunDTO izdajRacun(@PathVariable String racunId) {
		try {
			return kupovinaService.izdajRacun(racunId);
		} catch (InventarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
