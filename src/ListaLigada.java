public class ListaLigada {
    private Nodo cabeza;
    private int tamaño;

    public ListaLigada() {
        this.cabeza = null;
        this.tamaño = 0;
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
        tamaño++;
    }

    public Nota seleccionar(int index) {
        if (index < 0 || index >= tamaño) {
            return null; // índice fuera de rango
        }
        Nodo nodo = obtenerNodo(index);
        return nodo != null ? nodo.getDato() : null;
    }

    /**
     * Edita (modifica) una nota existente en la posición indicada.
     * @param index posición de la nota a modificar (0-based)
     * @param nuevaNota objeto Nota con los nuevos valores
     * @return true si se modificó correctamente, false si el índice es inválido
     */
    public boolean editar(int index, Nota nuevaNota) {
        if (index < 0 || index >= tamaño || nuevaNota == null) {
            return false;
        }
        Nodo nodo = obtenerNodo(index);
        if (nodo == null) {
            return false;
        }
        nodo.setDato(nuevaNota); // modificamos el dato del nodo existente
        return true;
    }

    /**
     * Elimina un nodo por su posición.
     * @param index posición del nodo a eliminar (0-based)
     * @return true si se eliminó correctamente, false si el índice es inválido
     */
    public boolean eliminar(int index) {
        if (index < 0 || index >= tamaño) {
            return false; // índice fuera de rango
        }

        if (index == 0) {
            // eliminar el primer nodo (cabeza)
            cabeza = cabeza.getSiguiente();
        } else {
            // buscar el nodo anterior al que se eliminará
            Nodo anterior = obtenerNodo(index - 1);
            if (anterior == null) {
                return false; // no debería pasar si índice válido y >0
            }
            Nodo actual = anterior.getSiguiente(); // nodo a eliminar
            anterior.setSiguiente(actual.getSiguiente()); // bypass del nodo
        }
        tamaño--;
        return true;
    }

    /** Imprime todas las notas de la lista en consola */
    public void imprimirLista() {
        if (cabeza == null) {
            System.out.println("Lista vacía");
            return;
        }
        Nodo actual = cabeza;
        int indice = 0;
        while (actual != null) {
            System.out.println(indice + ": " + actual.getDato());
            actual = actual.getSiguiente();
            indice++;
        }
    }

    /** Obtiene el nodo en una posición específica (método privado auxiliar) */
    private Nodo obtenerNodo(int index) {
        if (index < 0 || index >= tamaño) {
            return null;
        }
        Nodo actual = cabeza;
        for (int i = 0; i < index; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    /** Getter del tamaño */
    public int getTamaño() {
        return tamaño;
    }

    /** Método main de demostración */
    public static void main(String[] args) {
        ListaLigada lista = new ListaLigada();

        // Agregar varias notas
        lista.agregar(new Nota("DO", Figura.NEGRA, 4));
        lista.agregar(new Nota("RE", Figura.BLANCA, 4));
        lista.agregar(new Nota("MI", Figura.NEGRA, 5));
        lista.agregar(new Nota("FA", Figura.NEGRA, 4));
        lista.agregar(new Nota("SOL", Figura.NEGRA, 4));

        System.out.println("Lista inicial:");
        lista.imprimirLista();
        System.out.println();

        // Seleccionar una nota (índice 2)
        Nota seleccionada = lista.seleccionar(2);
        System.out.println("Nota seleccionada en índice 2: " + seleccionada);
        System.out.println();

        // Editar la nota en índice 0 (cambiar a SI, NEGRA, 3)
        Nota nuevaNota = new Nota("SI", Figura.NEGRA, 3);
        boolean editado = lista.editar(0, nuevaNota);
        System.out.println("Edición en índice 0 exitosa? " + editado);
        System.out.println();

        // Eliminar la nota en índice 3 (originalmente FA, después de edición sigue siendo FA)
        boolean eliminado = lista.eliminar(3);
        System.out.println("Eliminación en índice 3 exitosa? " + eliminado);
        System.out.println();

        System.out.println("Lista final después de operaciones:");
        lista.imprimirLista();
    }

    public boolean isEmpty() {
        return cabeza == null;
    }

    /**
     * Obtiene la nota en la posición especificada.
     * @param index posición de la nota a obtener (0-based)
     * @return la nota en la posición especificada, o null si el índice es inválido
     */
    public Nota obtener(int index) {
        return seleccionar(index);
    }

    /**
     * Modifica la nota en la posición especificada.
     * @param index posición de la nota a modificar (0-based)
     * @param nuevaNota objeto Nota con los nuevos valores
     * @return true si se modificó correctamente, false si el índice es inválido o la nota es null
     */
    public boolean modificar(int index, Nota nota) {
        return editar(index, nota);
    }

    /**
     * Obtiene una representación en cadena de todas las notas en la lista.
     * @return arreglo de cadenas donde cada elemento representa una nota en formato "índice: nombre - figura - octava"
     */
    public String[] mostrar() {
        if (cabeza == null) {
            return new String[0];
        }
        
        String[] resultado = new String[tamaño];
        Nodo actual = cabeza;
        int indice = 0;
        
        while (actual != null) {
            resultado[indice] = indice + ": " + actual.getDato().toString();
            actual = actual.getSiguiente();
            indice++;
        }
        
        return resultado;
    }
}