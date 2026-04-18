public class Nota {
    private String nombreNota;
    private Figura figura;
    private int octava;

    public Nota(String nombreNota, Figura figura, int octava) {
        this.nombreNota = nombreNota;
        this.figura = figura;
        this.octava = octava;
    }

    public String getNombreNota() {
        return nombreNota;
    }

    public void setNombreNota(String nombreNota) {
        this.nombreNota = nombreNota;
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public int getOctava() {
        return octava;
    }

    public void setOctava(int octava) {
        this.octava = octava;
    }

    @Override
    public String toString() {
        return nombreNota + " - " + figura.name() + " - Octava " + octava;
    }
}