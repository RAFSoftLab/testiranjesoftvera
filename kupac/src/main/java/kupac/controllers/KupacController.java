package kupac.controllers;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import kupac.dtos.KupacDTO;
import kupac.services.KupacService;

@RestController
@RequestMapping(path="/kupac")
public class KupacController {

	
	private KupacService kupacService;
	
	public KupacController(KupacService kupacService) {
		this.kupacService = kupacService;
	}
	
	@PostMapping("/registracija")
	public Long registracijaKupca(@RequestBody KupacDTO kupac) {		
		return kupacService.registracija(kupac);		
	}
	
	
	@GetMapping("/all")
	public List<KupacDTO> vratiSveKupce() {
		return kupacService.vratiSveKupce();		
	}
	
	@GetMapping("/{id}")
	public KupacDTO vratiZaId(@PathVariable Long id) {
		return kupacService.vratiZaId(id);		
	}
	
	
	
	
}
