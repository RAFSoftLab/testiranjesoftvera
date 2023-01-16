INSERT INTO `kupac` (`ime`,`prezime`,`email`,`rezervisana_sredstva`) VALUES ('Petar','Petrovic','ppetrovic@gmail.com',0);
INSERT INTO `kupac` (`ime`,`prezime`,`email`,`rezervisana_sredstva`) VALUES ('Marko','Markovic','mmarkovic@gmail.com',0);
INSERT INTO `kupac` (`ime`,`prezime`,`email`,`rezervisana_sredstva`) VALUES ('Stefan','Stefanovic','sstefanovic@gmail.com',0);


INSERT INTO `tekuci_racun` (`broj_racuna`,`stanje`,`kupac_id`) VALUES ('212345678',3000.0,1);
INSERT INTO `tekuci_racun` (`broj_racuna`,`stanje`,`kupac_id`) VALUES ('444345678',15000.0,1);
INSERT INTO `tekuci_racun` (`broj_racuna`,`stanje`,`kupac_id`) VALUES ('555345678',2100.0,2);
INSERT INTO `tekuci_racun` (`broj_racuna`,`stanje`,`kupac_id`) VALUES ('666345678',130000.0,2);

INSERT INTO `kupovina` (`datum`,`racun_id`,`placen`,`uplacen_iznos`,`kupac_id`) VALUES ('2023-01-16','PPBIG1075387',true,2000.0,1)

