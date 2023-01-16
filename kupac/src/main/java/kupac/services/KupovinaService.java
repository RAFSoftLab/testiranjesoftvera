package kupac.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kupac.dtos.KupovinaDTO;
import kupac.model.Kupovina;
import kupac.repositories.KupovinaRepository;

@Service
public class KupovinaService {
	
	private KupovinaRepository kupovinaRepo;
	
	@Autowired
	public KupovinaService(KupovinaRepository kupovinaRepo) {
		this.kupovinaRepo = kupovinaRepo;
	}
	
	public List<KupovinaDTO> vratiKupovineZaIdKupca(Long kupacId){
		List<Kupovina> kupovine = kupovinaRepo.vratiKupovineZaKupca(kupacId);
		List<KupovinaDTO> retVal = kupovine.stream()
				.map(k -> new KupovinaDTO(k.getDatum(), k.getRacunId(), k.isPlacen(), k.getUplacenIznos(), k.getKupac().getId(), k.getId())).toList();
		return retVal;
	}
	
	public KupovinaDTO vratiKupovinuZaRacunId(String racunId) {
		Kupovina k = kupovinaRepo.vratiKupovineZaRacunId(racunId);
		if(k==null) return null;
		return new KupovinaDTO(k.getDatum(), racunId, k.isPlacen(), k.getUplacenIznos(), k.getKupac().getId(), k.getId());
	}
	
	

}
