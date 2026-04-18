public enum Figura {
    REDONDA(4.0),
    BLANCA(2.0),
    NEGRA(1.0),
    CORCHEA(0.5);

    private final double duracion;

    Figura(double duracion) {
        this.duracion = duracion;
    }

    public double getDuracion() {
        return duracion;
    }

    public long getDuracionMilisegundos(int bpm) {
        double duracionRedonda = 60000.0 / bpm;
        return (long) (duracionRedonda * duracion);
    }
}