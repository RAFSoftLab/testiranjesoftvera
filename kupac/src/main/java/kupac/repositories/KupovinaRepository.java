package kupac.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import kupac.model.Kupovina;

public interface KupovinaRepository extends CrudRepository<Kupovina, Long> {
	
	@Query("select k from Kupovina k where k.kupac.id = :kupacId")
	List<Kupovina> vratiKupovineZaKupca(Long kupacId);
	
	@Query("select k from Kupovina k where k.racunId= :racunId")
	Kupovina vratiKupovineZaRacunId(String racunId);


}
