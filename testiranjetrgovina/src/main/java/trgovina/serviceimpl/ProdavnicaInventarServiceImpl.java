package trgovina.serviceimpl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.ProizvodDTO;
import trgovina.serviceconsumers.InventarServiceConsumer;
import trgovina.services.InventarService;

/**
 * statless implementacija
 * 
 */

@Service
public class ProdavnicaInventarServiceImpl implements InventarService{
	
	private InventarServiceConsumer inventarserviceConsumer;
	
	
	public ProdavnicaInventarServiceImpl(InventarServiceConsumer inventarServiceConsumer) {
		this.inventarserviceConsumer = inventarServiceConsumer;
	}
	
	
	public void setInventarserviceConsumer(InventarServiceConsumer inventarserviceConsumer) {
		this.inventarserviceConsumer = inventarserviceConsumer;
	}

	@Override
	public int vratiBrojRazlicitihProizvoda() {
		List<ProizvodDTO> retVal = inventarserviceConsumer.vratiSveProizvode();
		if(retVal==null) return 0;
		return retVal.size();
	}

	@Override
	public List<String> vratiSveNaziveProizvoda(){
		List<ProizvodDTO> retVal = inventarserviceConsumer.vratiSveProizvode();
		List<String> nazivi = retVal.stream().map(p -> p.getNaziv()).collect(Collectors.toList());
		return nazivi;
	}

	@Override
	public int vratiStanjeZaProizvod(String proizvod) {
		List<ProizvodDTO> retVal = inventarserviceConsumer.vratiSveProizvode();		
		return retVal.stream()
				.filter(p->p.getNaziv().equals(proizvod))
				.map(p -> p.getStanje()).collect(Collectors.toList()).get(0);
	}

	@Override
	public double vratiCenuZaProizvod(String proizvod) {
		List<ProizvodDTO> retVal = inventarserviceConsumer.vratiSveProizvode();		
		return retVal.stream()
				.filter(p->p.getNaziv().equals(proizvod))
				.map(p -> p.getCena()).collect(Collectors.toList()).get(0);
		
	}
	
	
	
	
	
}
