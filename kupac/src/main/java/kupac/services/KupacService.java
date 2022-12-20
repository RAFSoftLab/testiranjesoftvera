package kupac.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kupac.dtos.KupacDTO;
import kupac.model.Kupac;
import kupac.repositories.KupacRepository;
import kupac.servicecalls.QueueSender;

@Service
public class KupacService {
	
	private KupacRepository kupacRepo;
	private QueueSender sender;	
	@Autowired
	public KupacService(KupacRepository kupacRepo, QueueSender sender) {
		this.kupacRepo = kupacRepo;
		this.sender = sender;
	}
	
	public Long registracija(KupacDTO kupac) {
		Kupac k = kupacRepo.save(new Kupac(kupac.getIme(), kupac.getPrezime(), kupac.getEmail()));
		sender.posaljiEmailKupca(kupac.getEmail());
		return k.getId();
	}

	public List<KupacDTO> vratiSveKupce(){
		Iterable<Kupac> svi =  kupacRepo.findAll();
		List<KupacDTO> retVal = new ArrayList<>();
		for(Kupac k:svi) {
			retVal.add(new KupacDTO(k.getIme(),k.getPrezime(),k.getEmail()));
		}
		return retVal;		
	}
	
	public KupacDTO vratiZaId(Long id){
		Optional<Kupac> kOpt =  kupacRepo.findById(id);
		if(kOpt.isEmpty())
			return null;
		else
			return kOpt.map(k -> new KupacDTO(k.getIme(),k.getPrezime(),k.getEmail())).get();
	}
		
}
