package algoritmos;

import estructuras.Cola;
import estructuras.Grafo;
import estructuras.Pila;
import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * RF06 y RF07. Recorridos en anchura (BFS) y en profundidad (DFS).
 * Los visitados se marcan en un arreglo boolean[] usando el indice de cada vertice,
 * asi cada consulta "ya fue visitado?" es O(1).
 * Complejidad de ambos recorridos: O(V + E).
 */
public class Recorridos {

    /**
     * BFS: visita por niveles usando una Cola.
     * Primero el inicio, luego todos sus colaboradores, luego los colaboradores de estos, etc.
     * Lista vacia si el inicio no existe.
     */
    public static ArrayList<String> bfs(Grafo grafo, String inicio) {
        ArrayList<String> orden = new ArrayList<>();
        NodoVertice origen = grafo.buscarVertice(inicio);
        if (origen == null) {
            return orden;
        }
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        Cola cola = new Cola();
        visitado[origen.getIndice()] = true;   // se marca al ENCOLAR para no encolarlo dos veces
        cola.encolar(origen);

        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();
            orden.add(actual.getNombre());
            NodoAdyacencia ady = actual.getPrimerAdyacente();
            while (ady != null) {
                NodoVertice vecino = ady.getDestino();
                if (!visitado[vecino.getIndice()]) {
                    visitado[vecino.getIndice()] = true;
                    cola.encolar(vecino);
                }
                ady = ady.getSiguiente();
            }
        }
        return orden;
    }

    /**
     * DFS iterativo: avanza lo mas profundo posible usando una Pila.
     * Los vecinos se apilan en orden inverso para que salga primero el
     * alfabeticamente menor (mismo orden que la version recursiva).
     * Lista vacia si el inicio no existe.
     */
    public static ArrayList<String> dfs(Grafo grafo, String inicio) {
        ArrayList<String> orden = new ArrayList<>();
        NodoVertice origen = grafo.buscarVertice(inicio);
        if (origen == null) {
            return orden;
        }
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        Pila pila = new Pila();
        pila.apilar(origen);

        while (!pila.estaVacia()) {
            NodoVertice actual = pila.desapilar();
            if (visitado[actual.getIndice()]) {
                continue;                        // ya se visito por otro camino
            }
            visitado[actual.getIndice()] = true; // se marca al DESAPILAR
            orden.add(actual.getNombre());

            // Copiar los vecinos a un arreglo para recorrerlos al reves
            NodoVertice[] vecinos = new NodoVertice[contarVecinos(actual)];
            NodoAdyacencia ady = actual.getPrimerAdyacente();
            int i = 0;
            while (ady != null) {
                vecinos[i] = ady.getDestino();
                i++;
                ady = ady.getSiguiente();
            }
            for (int k = vecinos.length - 1; k >= 0; k--) {
                if (!visitado[vecinos[k].getIndice()]) {
                    pila.apilar(vecinos[k]);
                }
            }
        }
        return orden;
    }

    private static int contarVecinos(NodoVertice v) {
        int contador = 0;
        NodoAdyacencia ady = v.getPrimerAdyacente();
        while (ady != null) {
            contador++;
            ady = ady.getSiguiente();
        }
        return contador;
    }
}
