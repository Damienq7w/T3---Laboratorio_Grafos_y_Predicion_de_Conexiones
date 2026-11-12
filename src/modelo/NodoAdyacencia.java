package modelo;

/**
 * Nodo de una lista de adyacencia: representa una colaboracion.
 * Apunta al vertice real del colaborador (no guarda una copia del nombre),
 * asi un cambio en el vertice se refleja en todas las listas.
 *
 */
public class NodoAdyacencia {
    private NodoVertice destino;       // estudiante con quien se colaboro
    private NodoAdyacencia siguiente;  // siguiente colaborador en la lista

    public NodoAdyacencia(NodoVertice destino) {
        this.destino = destino;
        this.siguiente = null;
    }

    public NodoVertice getDestino() {
        return destino;
    }

    public NodoAdyacencia getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoAdyacencia siguiente) {
        this.siguiente = siguiente;
    }
}
