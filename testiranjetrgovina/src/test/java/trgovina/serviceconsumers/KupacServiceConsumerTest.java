package trgovina.serviceconsumers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.dtos.KupacDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KupacServiceConsumerTest {
	
	@Autowired
	KupacServiceConsumer kupacServiceConsumer;
	
	@Test
	void testVratiZaId() {
		KupacDTO kupac = kupacServiceConsumer.vratiKupcaZaId(Long.valueOf(1));		
		assertEquals("ppetrovc@gmail.com", kupac.getEmail());
	}
	
	@Test
	void testVratiZaIdNePostoji() {
		KupacDTO kupac = kupacServiceConsumer.vratiKupcaZaId(Long.valueOf(7));		
		assertNull(kupac);
	}

}
