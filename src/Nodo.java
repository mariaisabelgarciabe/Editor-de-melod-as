public class Nodo {
    private Nota dato;
    private Nodo siguiente;

    public Nodo(Nota dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public Nota getDato() {
        return dato;
    }

    public void setDato(Nota dato) {
        this.dato = dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}