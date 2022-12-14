package trgovina.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.serviceconsumers.KupacServiceConsumer;
import trgovina.services.ProdavnicaKupacService;

@Service
public class ProdavnicaKupacServiceImpl implements ProdavnicaKupacService {
	
	KupacServiceConsumer kupacServiceConsumer;
	
	@Autowired
	public ProdavnicaKupacServiceImpl(KupacServiceConsumer kupacServiceConsumer) {
		this.kupacServiceConsumer = kupacServiceConsumer;
	}

	@Override
	public KupacDTO kupacZaId(int id) {		
		return kupacServiceConsumer.vratiKupcaZaId(Long.valueOf(id));
	}
	
	

}
