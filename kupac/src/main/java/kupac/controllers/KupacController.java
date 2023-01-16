package kupac.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kupac.dtos.KupacDTO;
import kupac.dtos.KupovinaDTO;
import kupac.dtos.UplataDTO;
import kupac.repositories.KupovinaRepository;
import kupac.services.KupacService;
import kupac.services.KupovinaService;

@RestController
@RequestMapping(path="/kupac")
public class KupacController {

	
	private KupacService kupacService;	
	private KupovinaRepository kupovinaRepo;
	private KupovinaService kupovinaService;
	
	@Autowired
	public KupacController(KupacService kupacService, KupovinaService kupovinaService, KupovinaRepository kupovinaRepo) {
		this.kupacService = kupacService;
		this.kupovinaService = kupovinaService;
		this.kupovinaRepo = kupovinaRepo;		
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
	
	@GetMapping("/{id}/kupovine")
	public List<KupovinaDTO> vratiRacuneZaId(@PathVariable Long id) {
		return kupovinaService.vratiKupovineZaIdKupca(id);	
	}
	
	@GetMapping("/kupovina/{racunId}")
	public KupovinaDTO vratiKupovinuZaRacunId(@PathVariable String racunId) {
		return kupovinaService.vratiKupovinuZaRacunId(racunId);
	}
	
	
	@DeleteMapping("/kupovine/{id}")
	public void obrisiKupovinu(@PathVariable Long kupovinaId) {	
		kupovinaRepo.deleteById(kupovinaId);		
	}
	
	@GetMapping("/{id}/tekuciracun")
	public String vratiTekuciRacun(@PathVariable Long id) {
		return kupacService.vratiJedanBrojTekucegRacunaZaIdKupca(id);
	}
	
	@GetMapping("/tekuciracunstanje/{brojRacuna}")
	public Double vratiTekuciRacun(@PathVariable String brojRacuna) {
		return kupacService.vratiStanjeNaTekucemRacunu(brojRacuna);
	}
	
	@GetMapping("/strukturauplate")
	public List<UplataDTO> uplataRacuna(@RequestParam Long idKupca, @RequestParam double iznosZaUplatu) {		
		return kupacService.vratiStrukturuUplate(idKupca, iznosZaUplatu);	
	}
	
	@PutMapping("/smanjistanje")
	public void smanjiStanjeNaRacunu(@RequestParam String tekuciRacun, @RequestParam double umanjenje) {
		kupacService.umanjiStanjeNaRacunu(tekuciRacun, umanjenje);
	}
	
	@PutMapping("/povecajstanje")
	public void povecajStanjeNaRacunu(@RequestParam String tekuciRacun, @RequestParam double uvecanje) {
		kupacService.povecajStanjeNaRacunu(tekuciRacun, uvecanje);
	}
	
	@PutMapping("/dodajrezervisano")
	public void dodajRezervisanoZaKupca(@RequestParam Long kupacId, @RequestParam double iznosZaRezervisanje) {
		kupacService.dodajRezervisano(kupacId, iznosZaRezervisanje);
	}
	
	@PostMapping("/sacuvajkupovinu")
	public void sacuvajKupovinu(@RequestBody KupovinaDTO kupovina) {
		kupacService.sacuvajKupovinu(kupovina);
	}
	
	
}
