package kupac.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import kupac.model.TekuciRacun;

public interface TekuciRacunRepository extends CrudRepository<TekuciRacun, Long> {
	
	@Query("select t from TekuciRacun t where t.brojRacuna like :brojRacuna")
	TekuciRacun vratiTekuciRacunZaBroj(String brojRacuna);

}
