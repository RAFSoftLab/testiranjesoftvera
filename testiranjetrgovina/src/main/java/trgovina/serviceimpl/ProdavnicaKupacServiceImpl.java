package trgovina.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.KupovinaDTO;
import trgovina.dtos.UplataDTO;
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

	@Override
	public List<KupovinaDTO> kupovineZaKupca(int id) {		
		return kupacServiceConsumer.vratiKupovineZaIdKupca(Long.valueOf(id));
	}

	@Override
	public void obrisiKupovinu(Long idKupovine) {
		kupacServiceConsumer.obrisiKupovinu(idKupovine);		
	}

	@Override
	public String vratiTekuciRacunZaIdKupca(int kupacId) {
		return kupacServiceConsumer.vratiTekuciRacunZaIdKupca(Long.valueOf(kupacId));
	}

	@Override
	public List<UplataDTO> vratiStrukturuUplate(int kupacId, double iznosZaUplatu) {
		return kupacServiceConsumer.vratiStrukturuUplate(Long.valueOf(kupacId), iznosZaUplatu);
	}

	@Override
	public void smanjiStanjeNaRacunu(String tekuciRacun, double umanjenje) {
		kupacServiceConsumer.smanjiStanjeNaRacunu(tekuciRacun, umanjenje);
		
	}

	@Override
	public void dodajRezervisanoZaKupca(int kupacId, double iznosZaRezervisanje) {
		kupacServiceConsumer.dodajRezervisanoZaKupca(Long.valueOf(kupacId), iznosZaRezervisanje);
		
	}

	@Override
	public void sacuvajKupovinu(KupovinaDTO kupovina) {
		kupacServiceConsumer.sacuvajKupovinu(kupovina);
		
	}

	@Override
	public KupovinaDTO vratiKupovinuZaRacunId(String racunId) {		
		return kupacServiceConsumer.vratiKupovinuZaRacunId(racunId);
	}

	@Override
	public void povecajStanjeNaRacunu(String tekuciRacun, double povecanje) {
		kupacServiceConsumer.povecajStanjeNaRacunu(tekuciRacun, povecanje);
		
	}
	
	
	
	
	
	

}
