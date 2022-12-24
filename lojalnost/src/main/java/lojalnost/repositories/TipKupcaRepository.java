package lojalnost.repositories;

import org.springframework.data.repository.CrudRepository;

import lojalnost.model.BrojKupovina;
import lojalnost.model.TipKupca;

public interface TipKupcaRepository extends CrudRepository<TipKupca, Long> {
	
	
}
