package inventar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import inventar.dtos.ProizvodDTO;
import inventar.services.InventarService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path="/inventar")
public class ProizvodController {
	
	private InventarService inventarService;
	
	@Autowired
	public ProizvodController(InventarService inventarService) {		
		this.inventarService = inventarService;
	}
	
	

	@Operation(summary = "Vraca ceo inventar", description = "Vraca listu proizvoda sa cenom i stanjem")
	@GetMapping(path="/all")
	public List<ProizvodDTO> getInventar(){
		return inventarService.getInventar();
	}
	
	@Operation(summary = "Proizvod po nazivu", description = "Vraca sve podatke o proizvodu sa prosledjenim nazivom")
	@GetMapping(path="/proizvod")
	public ProizvodDTO getProizvodPoNazivu(@RequestParam String naziv){	
		return inventarService.getProizvodZaNaziv(naziv);
	}
	
	@Operation(summary = "Smanjuje kolicinu proizvoda na stanju", 
			   description = "Smanjuje broj proizvada u inventaru za vrednost prosledjenog parametra umenjenje, ako ne moze da se smanji jer nema dovoljno na stanju, vraca false, inace true")
	@PutMapping("/umanji")
	public boolean smanjiKolicinu(@RequestParam String naziv, @RequestParam int umanjenje){		
		return inventarService.smanjiKolicinuNaStanju(naziv, umanjenje);
	}
	
	@PutMapping("/uvecaj")
	public boolean uvecajKolicinu(@RequestParam String naziv, @RequestParam int uvecanje){		
		return inventarService.uvecajKolicinuNaStanju(naziv, uvecanje);
	}
	
	
}
