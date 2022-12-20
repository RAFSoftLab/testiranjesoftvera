package trgovina.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import trgovina.serviceimpl.Prodavnica;




@RestController
@RequestMapping(path="/prodavnica")
public class ProdavnicaController {
	
	@Autowired
	private Prodavnica prodavnica;
	

	@PostMapping(path="/kupi")
    public boolean kupiJedanProizvod(@RequestParam String nazivProizvoda,@RequestParam int kolicina, @RequestParam Long idKupca){  
		boolean uspesna = prodavnica.kupi(nazivProizvoda, kolicina);		
    	return uspesna;    	
    }

}
