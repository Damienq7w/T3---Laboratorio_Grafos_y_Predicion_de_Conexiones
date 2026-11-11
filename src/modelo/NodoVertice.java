package modelo;

/**
 * Nodo de la lista principal del grafo: representa a un estudiante.
 * Cada vertice guarda el inicio de su propia lista de colaboradores (lista de adyacencia).
 *
 */
public class NodoVertice {
    private String nombre;
    private int indice;                      // posicion 0..n-1 en la lista principal (la mantiene Grafo)
    private NodoAdyacencia primerAdyacente;  // inicio de su lista de colaboradores
    private NodoVertice siguiente;           // siguiente estudiante en la lista principal

    public NodoVertice(String nombre) {
        this.nombre = nombre;
        this.indice = -1;
        this.primerAdyacente = null;
        this.siguiente = null;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public NodoAdyacencia getPrimerAdyacente() {
        return primerAdyacente;
    }

    public void setPrimerAdyacente(NodoAdyacencia primerAdyacente) {
        this.primerAdyacente = primerAdyacente;
    }

    public NodoVertice getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoVertice siguiente) {
        this.siguiente = siguiente;
    }
}
