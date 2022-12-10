package trgovina.services;

import trgovina.dtos.RacunDTO;
import trgovina.model.Kupac;

public interface RacunNotificationsService {
	
	public void setEmailService(EmailService emailService);
	
	public boolean prepareAndSendMessage(Kupac k, RacunDTO racun);
	
	

}
