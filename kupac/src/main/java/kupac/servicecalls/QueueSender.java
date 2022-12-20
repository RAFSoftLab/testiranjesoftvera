package kupac.servicecalls;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kupac.dtos.KupacDTO;

@Component
public class QueueSender {
	
	@Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void posaljiEmailKupca(String email) {
        rabbitTemplate.convertAndSend(this.queue.getName(), email);
    }

}
