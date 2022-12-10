package trgovina.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import trgovina.model.Kupac;




@RestController
@RequestMapping(path="/prodavnica")
public class ProdavnicaController {
	

	@GetMapping(path="/kupac")
    public Kupac getStudentPoImenu(){  
		Kupac kupac = new Kupac("Petar","Petrovic");		
    	return kupac;    	
    }

}
