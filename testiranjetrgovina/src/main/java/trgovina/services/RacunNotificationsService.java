package trgovina.services;

import trgovina.dtos.RacunDTO;


public interface RacunNotificationsService {
	
	public void setEmailService(EmailService emailService);
	
	public boolean prepareAndSendMessage(String email, RacunDTO racun);
	
	

}
