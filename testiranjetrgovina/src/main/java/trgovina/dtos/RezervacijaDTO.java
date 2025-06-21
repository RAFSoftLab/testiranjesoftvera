package trgovina.dtos;

public class RezervacijaDTO {



    private Long kupacId;

    private String nazivProizvoda;

    private int kolicina;

    private Long rezervacijaId;


    public RezervacijaDTO() {
    }

    public RezervacijaDTO(Long rezervacijaId, Long kupacId, String nazivProizvoda, int kolicina) {
        this.kupacId = kupacId;
        this.nazivProizvoda = nazivProizvoda;
        this.kolicina = kolicina;
        this.rezervacijaId = rezervacijaId;

    }

    public Long getKupacId() {
        return kupacId;
    }

    public void setKupac(Long kupacId) {
        this.kupacId = kupacId;
    }

    public String getNazivProizvoda() {
        return nazivProizvoda;
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda = nazivProizvoda;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public Long getRezervacijaId() {
        return rezervacijaId;
    }

    public void setRezervacijaId(Long rezervacijaId) {
        this.rezervacijaId = rezervacijaId;
    }

    @Override
    public String toString() {
        return "RezervacijaDTO{" +
                "kupacId=" + kupacId +
                ", nazivProizvoda='" + nazivProizvoda + '\'' +
                ", kolicina=" + kolicina +
                ", rezervacijaId=" + rezervacijaId +
                '}';
    }
}
