package trgovina.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.serviceconsumers.LojalnostServiceConsumer;
import trgovina.services.ProdavnicaLojalnostService;

@Service
public class ProdavnicaLojalnostServiceImpl implements ProdavnicaLojalnostService {
	
	private LojalnostServiceConsumer lojalnostServiceConsumer;
	
	
	
	@Autowired
	public void setLojalnostServiceConsumer(LojalnostServiceConsumer lojalnostServiceConsumer) {
		this.lojalnostServiceConsumer = lojalnostServiceConsumer;
	}

	@Override
	public int vratiPopustZaKupca(String email) {		
		return lojalnostServiceConsumer.vratiPopustZaEmailKupca(email);
	}

}
