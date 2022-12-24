# Mikroservisne aplikacije i testiranje softvera

Primeri za sledeće predmete na Računarskom fakultetu: 

- Testiranje softvera (RN - 5. semestar)
- Softverske komponente (RN - 5. semestar)
- Mikroservisne aplikacije (RI - 7. semestar)


## Projekat se sastoji od 4 mikroservisa

- testiranjetrgovina - centralni mikroservis koji obezbeđuje sve osnovne informacije u prodavnici, ne koristi bazu, sve podatke čuva u operativnoj memoriji u poljima klasa Prodavnica i KupovinaService,
- inventar - odgovoran za podatke o stanju (dostupnoj količini) artikala u prodavnici, kao i cene proizvoda, koristi in-memory bazu koja se inicijalizuje  podacima o 4 proizvoda,
- kupac - odgovoran za podatke o kupcima i njihovim virtuelnim tekucim racunima, koristi in-memory bazu koja se inicijalizu podacima o 3 kupca i njihovim računima,
- lojalnost - programi popusta za kupce sa određenim brojem kupovina, koristi in-memory bazu koja se inicijalizuje sa dva programa lojalnosti i za 3 kupca postavlja vrednosti broja kupovina. 
