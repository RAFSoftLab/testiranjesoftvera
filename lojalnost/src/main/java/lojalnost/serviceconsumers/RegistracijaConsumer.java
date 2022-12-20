package lojalnost.serviceconsumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Service
public class RegistracijaConsumer {
	
	
	@RabbitListener(queues = "registracija")
    public void receive(String email) {
        System.out.println("Registrovan kupac " + email);
    }
	
	
	

}
