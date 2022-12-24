package lojalnost.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lojalnost.model.BrojKupovina;
import lojalnost.model.TipKupca;
import lojalnost.repositories.BrojKupovinaRepository;
import lojalnost.repositories.TipKupcaRepository;

@Service
public class LojalnostService {
	
	@Autowired
	BrojKupovinaRepository brKupovinaRepo;
	
	@Autowired
	TipKupcaRepository tipKupcaRepo;
	
	public int izracunajPopistZaEmalKupca(String email) {
		BrojKupovina brK = brKupovinaRepo.getBrojKupovinaZaEmail(email);
		if(brK==null)
			return 0;
		Iterable<TipKupca> tipovi = tipKupcaRepo.findAll();
		TipKupca tip = null;
		for(TipKupca tk:tipovi) {
			if(brK.getBrojKupovina()>=tk.getMinBrojKupovina())
				tip = tk;
			else
				break;
		}
		if(tip==null) return 0;
		return tip.getPopust();
	}

}
