package trgovina.prodavnicaimpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import trgovina.dtos.RacunDTO;
import trgovina.factories.KupacObjectMother;
import trgovina.factories.ProdavnicaObjectMother;
import trgovina.kupac.impl.KupacServiceImpl;
import trgovina.model.Kupac;
import trgovina.services.EmailService;
import trgovina.services.ProdavnicaService;

class RacunTest {
	
	private Kupac kupac;
	private KupacServiceImpl kupacService;
	private ProdavnicaService prodavnica;
	
	
	@BeforeEach
	public void pripremiZaRacun() {
		kupac = KupacObjectMother.createKupacBezTipa();   
		kupacService = new KupacServiceImpl();
		prodavnica = ProdavnicaObjectMother.createProdavnicaSaNazivomSaNProizvodaICenomB
				(List.of("sampon","sapun","hleb","mleko","bombone"), List.of(20,30,40,50,60), List.of(120.0, 80.0,40.0,50.0,100.0));
		kupacService.kupi(kupac,prodavnica,"hleb", 3);
		kupacService.kupi(kupac,prodavnica,"mleko", 2);
		kupacService.kupi(kupac,prodavnica, "bombone", 10);	
		
	}
	
	
	

	@Test
	void testRacunBezTipaKupca() {
	
		String racunId = kupacService.zatvoriRacun(kupac, prodavnica);
		
		
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
		
		String racunId = kupacService.zatvoriRacun(kupac, prodavnica);
		
		boolean ok = kupacService.posaljiRacun(kupac, prodavnica, racunId);
		
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
	
	
		boolean ok = kupacService.posaljiRacun(kupac, prodavnica, kupacService.getAktivniRacuni().get(kupac).getRacunId());
		
		assertFalse(ok);		
		verify(emailService, never()).sendEmail(eq(kupac.getEmail()), any(), any());  // proveravamo samo prvi argument, druga dva zanemarujemo		
	}
	
	
	// ilustracija za Argument capture
	
	@Test
	void testSlanjeRacunaArgumenti() {	
		EmailService emailService = mock(EmailService.class);
		kupacService.setEmailService(emailService);
		when(emailService.sendEmail(any(), any(), any())).thenReturn(true);
		String racunId = kupacService.zatvoriRacun(kupac, prodavnica);
		ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);  
		
		
		kupacService.posaljiRacun(kupac, prodavnica, racunId);
		
		verify(emailService).sendEmail(eq(kupac.getEmail()), acString.capture(), any());  // hvatamo vrednost argumenta 
		assertEquals("Racun "+racunId,acString.getValue());
	}
	

}
