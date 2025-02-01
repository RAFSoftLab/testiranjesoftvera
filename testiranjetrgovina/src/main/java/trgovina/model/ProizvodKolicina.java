package trgovina.model;

public class ProizvodKolicina {

    private String nazivProizvoda;
    private int kolicina;

    public ProizvodKolicina(String nazivProizvoda, int kolicina) {
        this.nazivProizvoda = nazivProizvoda;
        this.kolicina = kolicina;
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
}
