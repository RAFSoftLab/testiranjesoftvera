package trgovina.racun;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import trgovina.dtos.ProizvodDTO;
import trgovina.dtos.RacunDTO;
import trgovina.factories.KupacObjectMother;
import trgovina.model.Kupac;
import trgovina.serviceconsumers.InventarServiceConsumer;
import trgovina.serviceimpl.KupacServiceImpl;
import trgovina.serviceimpl.Prodavnica;
import trgovina.serviceimpl.ProdavnicaInventarServiceImpl;
import trgovina.services.EmailService;


class RacunTest {
	
	private Kupac kupac;
	private KupacServiceImpl kupacService;
	private Prodavnica prodavnica;
	
	private InventarServiceConsumer inventarServiceConsumer;
	private ProdavnicaInventarServiceImpl prodavnicaInventarService;
	
	
	@BeforeEach
	public void pripremiZaRacun() {		
		inventarServiceConsumer = mock(InventarServiceConsumer.class);
		List<ProizvodDTO> proizvodi = new ArrayList<>();
		proizvodi.add(new ProizvodDTO("sampon","kozmetika",100.0,90));
		proizvodi.add(new ProizvodDTO("sapun","kozetika",50.0,50));
		proizvodi.add(new ProizvodDTO("hleb","prehrambeni",40.0,80));
		proizvodi.add(new ProizvodDTO("mleko","prehrambeni",30.0,100));
		when(inventarServiceConsumer.vratiSveProizvode()).thenReturn(proizvodi);
		prodavnicaInventarService = new ProdavnicaInventarServiceImpl(inventarServiceConsumer);
		
		
		kupac = KupacObjectMother.createKupacBezTipa();
		prodavnica.setInventarService(prodavnicaInventarService);
		kupacService = new KupacServiceImpl(prodavnica);		
		kupacService.kupi(kupac,"hleb", 3);
		kupacService.kupi(kupac,"mleko", 2);
		kupacService.kupi(kupac,"bombone", 10);	
		
	}
	
	
	

	@Test
	void testRacunBezTipaKupca() {
	
		String racunId = kupacService.zatvoriRacun(kupac);
		
		
		RacunDTO racun = prodavnica.izdajRacun(kupac, racunId);
		
		assertEquals(1220, racun.getUkupnaCenaBezPdv());
		assertEquals(1464, racun.getUkupnaCenaSaPdv());
		assertEquals(1464, racun.getUkupnaCenaSaPopustom());		
	}
	
	// za vezbu uraditi sa nekim tipom kupca koji ima popust
	
	@Test
	void testSlanjeRacuna() {	
		EmailService emailService = mock(EmailService.class);
		kupacService.setEmailService(emailService);
		when(emailService.sendEmail(any(), any(), any())).thenReturn(true);
		
		String racunId = kupacService.zatvoriRacun(kupac);
		
		boolean ok = kupacService.posaljiRacun(kupac, racunId);
		
		assertTrue(ok);
		verify(emailService).sendEmail(eq(kupac.getEmail()), any(), any());  // proveravamo samo prvi argument, druga dva zanemarujemo		
	}
	
	// ovo mogu student sami da probaju
	// ako racun nije zatvoren ne moze da se kreira racun i posalje
	// proveriti da nije pozvano slanje
	@Test
	void testSlanjeRacunaNijeZatvorenRacun() {	
		EmailService emailService = mock(EmailService.class);
		kupacService.setEmailService(emailService);
		when(emailService.sendEmail(any(), any(), any())).thenReturn(true);
	
	
		boolean ok = kupacService.posaljiRacun(kupac, kupacService.getAktivniRacuni().get(kupac).getRacunId());
		
		assertFalse(ok);		
		verify(emailService, never()).sendEmail(eq(kupac.getEmail()), any(), any());  // proveravamo samo prvi argument, druga dva zanemarujemo		
	}
	
	
	// ilustracija za Argument capture
	
	@Test
	void testSlanjeRacunaArgumenti() {	
		EmailService emailService = mock(EmailService.class);
		kupacService.setEmailService(emailService);
		when(emailService.sendEmail(any(), any(), any())).thenReturn(true);
		String racunId = kupacService.zatvoriRacun(kupac);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);  
		
		
		kupacService.posaljiRacun(kupac, racunId);
		
		verify(emailService).sendEmail(eq(kupac.getEmail()), acString.capture(), any());  // hvatamo vrednost argumenta 
		assertEquals("Racun "+racunId,acString.getValue());
	}
	

}
