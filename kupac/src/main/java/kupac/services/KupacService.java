package kupac.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kupac.dtos.KupacDTO;
import kupac.model.Kupac;
import kupac.repositories.KupacRepository;

@Service
public class KupacService {
	
	private KupacRepository kupacRepo;
	
	@Autowired
	public KupacService(KupacRepository kupacRepo) {
		this.kupacRepo = kupacRepo;
	}
	
	public Long registracija(KupacDTO kupac) {
		Kupac k = kupacRepo.save(new Kupac(kupac.getIme(), kupac.getPrezime(), kupac.getEmail()));
		return k.getId();
	}

}
