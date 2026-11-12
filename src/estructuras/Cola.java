package estructuras;

import modelo.NodoVertice;

/**
 * Cola FIFO con nodos propios (sin LinkedList ni ArrayDeque). La usa BFS.
 * encolar y desencolar son O(1) porque se guardan frente y fin.
 *
 */
public class Cola {

    private static class NodoCola {
        private NodoVertice dato;
        private NodoCola siguiente;

        NodoCola(NodoVertice dato) {
            this.dato = dato;
        }
    }

    private NodoCola frente;
    private NodoCola fin;

    public void encolar(NodoVertice v) {
        NodoCola nuevo = new NodoCola(v);
        if (fin == null) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
    }

    /** Saca el primero que entro. null si esta vacia. */
    public NodoVertice desencolar() {
        if (frente == null) {
            return null;
        }
        NodoVertice dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }
}
