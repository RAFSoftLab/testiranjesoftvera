package lojalnost.serviceconsumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lojalnost.model.BrojKupovina;
import lojalnost.repositories.BrojKupovinaRepository;



@Service
public class RegistracijaConsumer {
	
	
	private BrojKupovinaRepository brojKupovinaRepo;
	
	
	@Autowired	
	public void setBrojKupovinaRepo(BrojKupovinaRepository brojKupovinaRepo) {
		this.brojKupovinaRepo = brojKupovinaRepo;
	}


	@RabbitListener(queues = "registracija")
    public void receive(String email) {
        System.out.println("Registrovan kupac " + email);
        BrojKupovina bk = new BrojKupovina(email);
        brojKupovinaRepo.save(bk);
        
    }
	
	
	

}
