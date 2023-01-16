package kupac.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kupac.dtos.KupacDTO;
import kupac.dtos.KupovinaDTO;
import kupac.dtos.UplataDTO;
import kupac.model.Kupac;
import kupac.model.Kupovina;
import kupac.model.TekuciRacun;
import kupac.repositories.KupacRepository;
import kupac.repositories.KupovinaRepository;
import kupac.repositories.TekuciRacunRepository;
import kupac.servicecalls.QueueSender;

@Service
public class KupacService {
	
	private KupacRepository kupacRepo;
	private TekuciRacunRepository tekuciRacunRepo;	
	private KupovinaRepository kupovinaRepo;
	
	private QueueSender sender;	
	
	@Autowired
	public KupacService(KupacRepository kupacRepo, QueueSender sender, TekuciRacunRepository tekuciRacunRepo, KupovinaRepository kupovinaRepo) {
		this.kupacRepo = kupacRepo;
		this.sender = sender;
		this.tekuciRacunRepo = tekuciRacunRepo;
		this.kupovinaRepo = kupovinaRepo;
	}
	
	public Long registracija(KupacDTO kupac) {
		Kupac k = kupacRepo.save(new Kupac(kupac.getIme(), kupac.getPrezime(), kupac.getEmail()));
		sender.registrovanKupacEmitEvent(kupac.getEmail());
		return k.getId();
	}

	public List<KupacDTO> vratiSveKupce(){
		Iterable<Kupac> svi =  kupacRepo.findAll();
		List<KupacDTO> retVal = new ArrayList<>();
		for(Kupac k:svi) {
			retVal.add(new KupacDTO(k.getId(),k.getIme(),k.getPrezime(),k.getEmail(),k.getRezervisanaSredstva()));
		}
		return retVal;		
	}
	
	public KupacDTO vratiZaId(Long id){
		Optional<Kupac> kOpt =  kupacRepo.findById(id);
		if(kOpt.isEmpty())
			return null;
		else
			return kOpt.map(k -> new KupacDTO(k.getId(),k.getIme(),k.getPrezime(),k.getEmail(),k.getRezervisanaSredstva())).get();
	}
	
	
	public String vratiJedanBrojTekucegRacunaZaIdKupca(Long idKupca) {
		Optional<Kupac> kOpt = kupacRepo.findById(idKupca);
		if(kOpt.isEmpty()) return null;
		Kupac k = kOpt.get();
		if(k.getTekuciRacuni().isEmpty())
			return null;
		return k.getTekuciRacuni().get(0).getBrojRacuna();		
	}
	
	/**
	 * testirati 
	 * @param idKupca
	 * @param iznosZaUplatu
	 * @return
	 */
	
	public List<UplataDTO> vratiStrukturuUplate(Long idKupca, double iznosZaUplatu) {		
		Optional<Kupac> kOpt = kupacRepo.findById(idKupca);
		if(kOpt.isEmpty()) return null;
		Kupac k = kOpt.get();		
		if(k.getTekuciRacuni().isEmpty())
			return null;
		List<UplataDTO> retVal = new ArrayList<>();		
		if(k.getUkupnoStanje()<iznosZaUplatu)
			return null;
		double preostaloZaPlacanje = iznosZaUplatu;
		List<TekuciRacun> trList = k.getTekuciRacuni();
		int i = 0;
		while(preostaloZaPlacanje>0 && i<trList.size()) {			
			TekuciRacun tr = trList.get(i);
			if(preostaloZaPlacanje>=tr.getStanje()) {
				retVal.add(new UplataDTO(tr.getBrojRacuna(),tr.getStanje()));				
				preostaloZaPlacanje-=tr.getStanje();								
			}else {
				retVal.add(new UplataDTO(tr.getBrojRacuna(), preostaloZaPlacanje));				
				preostaloZaPlacanje=0;						
			}
			i++;			
		}
		return retVal;
	}
	
	public void umanjiStanjeNaRacunu(String tekuciRacun, double umanjenje) {
		TekuciRacun tr = tekuciRacunRepo.vratiTekuciRacunZaBroj(tekuciRacun);
		tr.setStanje(tr.getStanje()-umanjenje);
		tekuciRacunRepo.save(tr);
	}
	
	public void povecajStanjeNaRacunu(String tekuciRacun, double povecanje) {
		TekuciRacun tr = tekuciRacunRepo.vratiTekuciRacunZaBroj(tekuciRacun);
		tr.setStanje(tr.getStanje()+povecanje);
		tekuciRacunRepo.save(tr);
	}
	
	public void dodajRezervisano(Long kupacId, double iznos) {
		Optional<Kupac> kOpt = kupacRepo.findById(kupacId);
		if(!kOpt.isEmpty()) {
			Kupac k = kOpt.get();
			k.setRezervisanaSredstva(k.getRezervisanaSredstva()+iznos);
			kupacRepo.save(k);
		}
	}
	
	public void sacuvajKupovinu(KupovinaDTO k) {
		Kupovina kupovina = new Kupovina();
		Optional<Kupac> kOpt = kupacRepo.findById(k.getKupacId());
		if(!kOpt.isEmpty()) {
			kupovina.setKupac(kOpt.get());
		}
		kupovina.setDatum(k.getDatum());
		kupovina.setPlacen(k.isPlacen());
		kupovina.setRacunId(k.getRacunId());
		kupovina.setUplacenIznos(k.getUplacenIznos());
		kupovinaRepo.save(kupovina);
	}
	
	public double vratiStanjeNaTekucemRacunu(String brojRacuna) {
		TekuciRacun tr = tekuciRacunRepo.vratiTekuciRacunZaBroj(brojRacuna);
		if(tr!=null)
			return tr.getStanje();
		else
			return 0.0;
	}
	
	
	
		
}
