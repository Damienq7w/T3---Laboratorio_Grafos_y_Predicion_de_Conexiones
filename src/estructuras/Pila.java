package estructuras;

import modelo.NodoVertice;

/**
 * Pila LIFO con nodos propios (sin Stack ni ArrayDeque). La usa DFS.
 * apilar y desapilar son O(1) porque se trabaja siempre sobre la cima.
 *
 */
public class Pila {

    private static class NodoPila {
        private NodoVertice dato;
        private NodoPila debajo;

        NodoPila(NodoVertice dato, NodoPila debajo) {
            this.dato = dato;
            this.debajo = debajo;
        }
    }

    private NodoPila cima;

    public void apilar(NodoVertice v) {
        cima = new NodoPila(v, cima);
    }

    /** Saca el ultimo que entro. null si esta vacia. */
    public NodoVertice desapilar() {
        if (cima == null) {
            return null;
        }
        NodoVertice dato = cima.dato;
        cima = cima.debajo;
        return dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }
}
