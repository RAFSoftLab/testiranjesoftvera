package trgovina.serviceimpl;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.dtos.KupacDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.services.KupacService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KupovinaServiceImplTest {

	@Test
	void testkupovinaServiceOtvaranjeRacuna() {
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaServiceImpl kupovinaService = new KupovinaServiceImpl(prodavnica);	
		KupacService kupacService = mock(KupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		
		String brojRacuna = kupovinaService.otvoriRacun(1);
		
		assertTrue(brojRacuna.startsWith("MM"));
		assertNotNull(kupovinaService.aktivanRacunZaKupca(1));
		
		
	}
	
	
	@Test
	void testkupovinaServiceOtvaranjeRacunaMockGenerator() {
		// generator broja racuna ima nedeterministicko ponasanje, mozemo taj deo da laziramo		
		Prodavnica prodavnica = mock(Prodavnica.class);
		when(prodavnica.noviBrojRacuna("Marko","Markovic")).thenReturn("MM12345");
		KupovinaServiceImpl kupovinaService = new KupovinaServiceImpl(prodavnica);	
		KupacService kupacService = mock(KupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		
		String brojRacuna = kupovinaService.otvoriRacun(1);		
		
		assertEquals("MM12345",kupovinaService.aktivanRacunZaKupca(1));			
	}
	
	@Test
	void testkupovinaServiceZatvaranjeRacuna() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaServiceImpl kupovinaService = new KupovinaServiceImpl(prodavnica);
		KupacService kupacService = mock(KupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		String brojRacuna = kupovinaService.otvoriRacun(1);
		
		kupovinaService.zatvoriRacun(brojRacuna);
		
		assertNull(kupovinaService.aktivanRacunZaKupca(1));
		assertEquals(1,kupovinaService.getZatvoreniRacuni().size());
		assertEquals(brojRacuna,kupovinaService.getZatvoreniRacuni().get(0).getRacunId());			
	}
	
	// za vezbu - da studenti sami rade
	
	@Test
	void testkupovinaServiceZatvaranjeRacunaNeispravno() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaServiceImpl kupovinaService = new KupovinaServiceImpl(prodavnica);		
		
		assertThrows(NedozvoljenaOperacijaNadRacunomException.class,() -> kupovinaService.zatvoriRacun("MM1234"));
		
					
	}
	
	
	


}
