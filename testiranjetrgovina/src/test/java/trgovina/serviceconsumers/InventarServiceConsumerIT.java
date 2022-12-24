package trgovina.serviceconsumers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.dtos.ProizvodDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InventarServiceConsumerIT {
	
	@Autowired
	InventarServiceConsumer inventarServiceConsumer;

	@Test
	void testVratiSveProizvode() {
		List<ProizvodDTO> inventar = inventarServiceConsumer.vratiSveProizvode();
		
		assertEquals(4, inventar.size());
	}
	
	@Test
	void testProizvodPoNazivu() {
		ProizvodDTO proizvod = inventarServiceConsumer.vratiProizvodePoNazivu("sampon");
		
		assertEquals("sampon",proizvod.getNaziv());
	}


}
