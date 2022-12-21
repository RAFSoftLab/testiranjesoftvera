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
	public int vratiStanjeZaProizvod(String naziv) {
		System.out.println("Vrati proizvod po nazivu-sinhrono");
		ProizvodDTO p = inventarserviceConsumer.vratiProizvodePoNazivu(naziv);
		System.out.println("Vracen proizvod-sinhrono");
		return p.getStanje();
	}

	@Override
	public double vratiCenuZaProizvod(String naziv) {
		ProizvodDTO p = inventarserviceConsumer.vratiProizvodePoNazivu(naziv);		
		return p.getCena();
		
	}


	@Override
	public void umanjiStanjeProizvoda(String proizvod, int umanjenje) {
		System.out.println("Azuriranje stanja-asinhrono");
		inventarserviceConsumer.azurirajStanjeInventaraAsinh(proizvod, umanjenje);
		System.out.println("Azuriranje stanja zavrseno-asinhrono");
		
	}
	
	
	
	
	
}
