package lojalnost.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lojalnost.model.BrojKupovina;
import lojalnost.repositories.BrojKupovinaRepository;
import lojalnost.services.LojalnostService;

@RestController
@RequestMapping(path="/lojalnost")
public class LojalnostController {
	
	@Autowired
	private BrojKupovinaRepository brojKupovinaRepo;
	
	@Autowired
	private LojalnostService lojalnostService;
	
	@GetMapping(path="/all")
	public Iterable<BrojKupovina> getSveLojalnosti(){
		return brojKupovinaRepo.findAll();		
	}
	
	@GetMapping(path="/popust")
	public Integer getPopustZaEmailKupca(@RequestParam String email){
		return lojalnostService.izracunajPopistZaEmalKupca(email);	
	}

}
