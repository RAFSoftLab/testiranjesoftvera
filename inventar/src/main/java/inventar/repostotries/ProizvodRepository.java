package inventar.repostotries;

import org.springframework.data.repository.CrudRepository;

import inventar.model.Proizvod;

public interface ProizvodRepository extends CrudRepository<Proizvod, Long> {

}
