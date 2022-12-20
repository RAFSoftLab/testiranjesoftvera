package lojalnost;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.AMQP.Queue;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

@Configuration
public class LojalnostConfiguration {
	
	@Value("${queue.name}")
	private String queueName;

	
	
	


}
