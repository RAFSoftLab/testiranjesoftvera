package trgovina.serviceimpl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.ProizvodDTO;
import trgovina.izuzeci.InventarExeception;
import trgovina.serviceconsumers.InventarServiceConsumer;
import trgovina.services.ProdavnicaInventarService;

/**
 * statless implementacija
 * 
 */

@Service
public class ProdavnicaInventarServiceImpl implements ProdavnicaInventarService{
	
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
	public int vratiStanjeZaProizvod(String naziv) {		
		ProizvodDTO p = inventarserviceConsumer.vratiProizvodePoNazivu(naziv);
		if(p==null) 
			return 0;
		return p.getStanje();
	}

	@Override
	public double vratiCenuZaProizvod(String naziv) {
		ProizvodDTO p = inventarserviceConsumer.vratiProizvodePoNazivu(naziv);		
		if(p==null) throw new InventarExeception("Proizvod pod nazivom "+naziv+" ne postoji");
		return p.getCena();
		
	}


	@Override
	public void umanjiStanjeProizvoda(String proizvod, int umanjenje) {		
		inventarserviceConsumer.azurirajStanjeInventaraAsinh(proizvod, umanjenje);		
	}
	
	
	
	
	
}
