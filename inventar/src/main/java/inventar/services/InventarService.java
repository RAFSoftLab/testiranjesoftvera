package inventar.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inventar.dtos.ProizvodDTO;
import inventar.model.Proizvod;
import inventar.repositories.ProizvodRepository;

@Service
public class InventarService {
	
	private ProizvodRepository proizvodRepo;

	@Autowired	
	public InventarService(ProizvodRepository proizvodRepo) {		
		this.proizvodRepo = proizvodRepo;
	}
	
	public List<ProizvodDTO> getInventar(){
		Iterable<Proizvod> sviProizvodi = proizvodRepo.findAll();
		List<ProizvodDTO> retVal = new ArrayList<>();
		sviProizvodi.forEach(p 
				-> retVal.add(new ProizvodDTO(p.getNaziv(), p.getKategorija(), p.getCena(), p.getStanje())));
		return retVal;		
	}
	
	/*
	 * ocekujemo samo jedan proizvod za prosledjeni naziv
	 */
	
	public ProizvodDTO getProizvodZaNaziv(String naziv){
		List<Proizvod> proizvodi = proizvodRepo.vratiProizvodZaNaziv(naziv);
		Proizvod p = proizvodi.get(0);		
		return new ProizvodDTO(p.getNaziv(), p.getKategorija(), p.getCena(), p.getStanje());		
	}
	
	public boolean smanjiKolicinuNaStanju(String naziv, int umanjenje){
		List<Proizvod> proizvodi = proizvodRepo.vratiProizvodZaNaziv(naziv);
		Proizvod p = proizvodi.get(0);
		if(p.getStanje()>=umanjenje) {
			p.setStanje(p.getStanje()-umanjenje);
			proizvodRepo.save(p);
			return true;
		}
		return false;				
	}
	

}
