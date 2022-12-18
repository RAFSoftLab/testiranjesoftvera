package inventar.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import inventar.dtos.ProizvodDTO;
import inventar.model.Proizvod;
import inventar.repostotries.ProizvodRepository;

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
		sviProizvodi.forEach(p -> retVal.add(new ProizvodDTO(p.getNaziv(), p.getKategorija(), p.getCena(), p.getStanje())));
		return retVal;
		
		
	}
	

}
