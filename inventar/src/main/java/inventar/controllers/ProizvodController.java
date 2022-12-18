package inventar.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
