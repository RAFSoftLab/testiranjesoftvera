package kupac.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
	
	
}
