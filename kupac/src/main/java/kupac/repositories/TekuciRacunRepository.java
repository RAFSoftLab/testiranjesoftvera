package kupac.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import kupac.model.TekuciRacun;

import java.util.List;

public interface TekuciRacunRepository extends CrudRepository<TekuciRacun, Long> {
	
	@Query("select t from TekuciRacun t where t.brojRacuna like :brojRacuna")
	TekuciRacun vratiTekuciRacunZaBroj(String brojRacuna);

	@Query("select t from TekuciRacun t where t.kupac.id = :idKupca and t.stanje>= :iznos")
	List<TekuciRacun> vratiTekuciRacunKupcaIStanjeVeceOdIznosa(Long idKupca, double iznos);

}
