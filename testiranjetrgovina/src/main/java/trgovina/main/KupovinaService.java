package trgovina.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.InventarException;
import trgovina.izuzeci.RacunException;
import trgovina.model.Racun;
import trgovina.services.ProdavnicaKupacService;

@Service
public class KupovinaService{
	
	/**
	 * svi trenutno otvoreni racuni, na kojima jos nije zavrsena kupovina
	 * id racuna je kljuc
	 * 
	 */
	
	private Map<String, Racun> aktivniRacuni;
	private List<Racun> zatvoreniRacuni;
	
	private Prodavnica prodavnica;
	
	@Autowired
	private ProdavnicaKupacService kupacService;
	
	public KupovinaService(Prodavnica prodavnica) {
		this.prodavnica = prodavnica;		
		aktivniRacuni = new HashMap<>();
		zatvoreniRacuni = new ArrayList<>();
	}
	
	
	@Autowired
	public void setKupacService(ProdavnicaKupacService kupacService) {
		this.kupacService = kupacService;
	}

	/**
	 * jedan kupac ne moze imati vise aktivnih racuna
	 */

	
	public String otvoriRacun(int kupacId) {
		if(getAktivanRacunZaKupca(kupacId)!=null) 
			return getAktivanRacunZaKupca(kupacId);  // ako vec postoji aktivan racun za tog kupca, vracamo njegov broj
		KupacDTO k = kupacService.kupacZaId(kupacId);
		if(k==null) return null;
		String racunId = prodavnica.noviBrojRacuna(k.getIme(),k.getPrezime());
		Racun racun = new Racun(racunId, LocalDate.now());
		racun.setKupacId(kupacId);		
		aktivniRacuni.put(racunId, racun);
		return racunId;
	}
	
	public Collection<Racun> getAktivniRacuni(){
		return aktivniRacuni.values();
	}
	
	
	// zatvara racun i vraca njegov id
	
	
	public String zatvoriRacun(String idRacuna) throws RacunException {
		Racun racunKupca =  aktivniRacuni.get(idRacuna);
		if(racunKupca==null) 
			throw new RacunException("Racun nije otvoren, ne moze se zatvoriti");		
		// TODO za kupca sacuvati racun
		racunKupca.zatvori();
		zatvoreniRacuni.add(racunKupca);
		aktivniRacuni.remove(idRacuna);
		return racunKupca.getRacunId();
		
	}
	
	/**
	 * Vraca idRacuna aktivnog za kupca sa prosledjenim id-jem ili null ako 
	 * ne postoji aktivan racun za kupca 
	 * @param kupacId
	 * @return
	 */
	
	public String getAktivanRacunZaKupca(int kupacId) {
		for(String racunId:aktivniRacuni.keySet()) {
			if(aktivniRacuni.get(racunId).getKupacId() == kupacId)
				return racunId;			
		}		
		return null;		
	}
	
	/**
	 * prvo gleda u aktivnim racunima, onda u zatvorenim
	 */
	
	
	public Racun vratiRacunZaId(String idRacuna) {
		if(aktivniRacuni.containsKey(idRacuna)) {
			return aktivniRacuni.get(idRacuna);			
		}else {
			return getZatvorenRacunPoId(idRacuna);
		}
		
	}
	
	
	/**
	 * samo dodaje proizvod sa zadatom kolicinom na aktivan racun kupca
	 * ovde se jos ne smanjuje kolicina u inventaru
	 * @throws InventarException 
	 */
	
	
	public boolean kupi(int kupacId, String proizvod, int kolicina) throws InventarException {
		String racunId = getAktivanRacunZaKupca(kupacId);
		if(racunId == null) {  // nije otvoren racun, prvo cemo ga otvoriti
			racunId = otvoriRacun(kupacId);
		}
		return dodajNaAktivanRacun(racunId, proizvod, kolicina);
		
	}
	
	public boolean dodajNaAktivanRacun(String racunId, String proizvod, int kolicina) throws InventarException {
		if(isAktivanRacun(racunId)) {
			if(prodavnica.mozeDaKupi(proizvod, kolicina)) {
				Racun racun = aktivniRacuni.get(racunId);
				racun.dodajArtikal(proizvod, kolicina);			
				return true;
			}else {
				throw new InventarException("Nema dovoljno proizvoda na stanju");
			}		
		}
		return false;			
	}
	
	private boolean isAktivanRacun(String idRacuna) {
		return aktivniRacuni.containsKey(idRacuna);
	}
	
	
	public List<Racun> getZatvoreniRacuni() {
		return zatvoreniRacuni;
	}
	
	private Racun getZatvorenRacunPoId(String racunId) {
		for(Racun r:zatvoreniRacuni) {
			if(r.getRacunId().equals(racunId))
				return r;
		}
		return null;
	}

	public void setZatvoreniRacuni(List<Racun> zatvoreniRacuni) {
		this.zatvoreniRacuni = zatvoreniRacuni;
	}
	
	public RacunDTO izdajRacun(String racunId) throws InventarException{
		Racun r = getZatvorenRacunPoId(racunId);
		if(r==null) return null;
		return prodavnica.izdajRacun(r);
	}

	
	
	


	


	
	
	
}
