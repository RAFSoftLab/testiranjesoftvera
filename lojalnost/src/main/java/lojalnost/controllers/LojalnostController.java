package lojalnost.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lojalnost.model.BrojKupovina;
import lojalnost.repositories.BrojKupovinaRepository;

@RestController
@RequestMapping(path="/lojalnost")
public class LojalnostController {
	
	@Autowired
	private BrojKupovinaRepository brojKupovinaRepo;
	
	@GetMapping(path="/all")
	public Iterable<BrojKupovina> getSveLojalnosti(){
		return brojKupovinaRepo.findAll();
		
	}

}
