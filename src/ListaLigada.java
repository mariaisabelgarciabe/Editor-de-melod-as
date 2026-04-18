public class ListaLigada {
    private Nodo cabeza;
    private int tamanho;

    public ListaLigada() {
        this.cabeza = null;
        this.tamanho = 0;
    }

    public void agregar(Nota nota) {
        Nodo nuevoNodo = new Nodo(nota);
        
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamanho++;
    }

    public boolean eliminar(int posicion) {
        if (posicion < 0 || posicion >= tamanho) {
            return false;
        }

        if (posicion == 0) {
            cabeza = cabeza.getSiguiente();
        } else {
            Nodo anterior = obtenerNodo(posicion - 1);
            Nodo actual = anterior.getSiguiente();
            anterior.setSiguiente(actual.getSiguiente());
        }
        tamanho--;
        return true;
    }

    public boolean modificar(int posicion, Nota nuevaNota) {
        Nodo nodo = obtenerNodo(posicion);
        if (nodo != null) {
            nodo.setDato(nuevaNota);
            return true;
        }
        return false;
    }

    public Nota obtener(int posicion) {
        Nodo nodo = obtenerNodo(posicion);
        return nodo != null ? nodo.getDato() : null;
    }

    private Nodo obtenerNodo(int posicion) {
        if (posicion < 0 || posicion >= tamanho) {
            return null;
        }
        
        Nodo actual = cabeza;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }

    public String[] mostrar() {
        String[] notasArray = new String[tamanho];
        Nodo actual = cabeza;
        int indice = 0;
        
        while (actual != null) {
            notasArray[indice] = (indice + 1) + ". " + actual.getDato().toString();
            actual = actual.getSiguiente();
            indice++;
        }
        
        return notasArray;
    }
}