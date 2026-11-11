package algoritmos;

import estructuras.Grafo;
import java.util.ArrayList;
import modelo.Candidato;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * RF08, RF09 y reto del Grupo 2: recomendar companeros de proyecto
 * segun los colaboradores que comparten, excluyendo relaciones directas.
 *
 * Responsable: Monica Tacuri
 */
public class Recomendador {

    /**
     * RF08. Colaboradores que a y b tienen en comun, en orden alfabetico.
     * El puntaje es el tamano de la lista. Lista vacia si alguno no existe o si son el mismo.
     */
    public static ArrayList<String> vecinosComunes(Grafo grafo, String a, String b) {
        NodoVertice va = grafo.buscarVertice(a);
        NodoVertice vb = grafo.buscarVertice(b);
        if (va == null || vb == null || va == vb) {
            return new ArrayList<>();
        }
        return interseccion(va, vb);
    }

    /**
     * RF09 + reto. Hasta 'limite' candidatos ordenados por puntaje (y alfabeticamente si empatan).
     * Excluye: al mismo estudiante, a sus colaboradores directos y a quien tenga puntaje 0.
     * Cada estudiante se revisa una sola vez, por eso no hay candidatos duplicados.
     */
    public static ArrayList<Candidato> recomendar(Grafo grafo, String nombre, int limite) {
        ArrayList<Candidato> resultado = new ArrayList<>();
        NodoVertice origen = grafo.buscarVertice(nombre);
        if (origen == null || limite <= 0) {
            return resultado;
        }

        // 1. Marcar colaboradores directos en un arreglo por indice (consulta O(1))
        boolean[] esDirecto = new boolean[grafo.getCantidadVertices()];
        NodoAdyacencia ady = origen.getPrimerAdyacente();
        while (ady != null) {
            esDirecto[ady.getDestino().getIndice()] = true;
            ady = ady.getSiguiente();
        }

        // 2. Revisar cada estudiante y calcular su puntaje
        ArrayList<Candidato> ordenados = new ArrayList<>();
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            boolean esElMismo = actual == origen;
            if (!esElMismo && !esDirecto[actual.getIndice()]) {
                ArrayList<String> comunes = interseccion(origen, actual);
                if (!comunes.isEmpty()) {
                    insertarOrdenado(ordenados, new Candidato(actual.getNombre(), comunes));
                }
            }
            actual = actual.getSiguiente();
        }

        // 3. Tomar los mejores
        for (int i = 0; i < ordenados.size() && i < limite; i++) {
            resultado.add(ordenados.get(i));
        }
        return resultado;
    }

    /**
     * Interseccion de dos listas de adyacencia ORDENADAS, avanzando en ambas a la vez
     * (como el "merge" de MergeSort). Complejidad O(ga + gb).
     */
    private static ArrayList<String> interseccion(NodoVertice va, NodoVertice vb) {
        ArrayList<String> comunes = new ArrayList<>();
        NodoAdyacencia x = va.getPrimerAdyacente();
        NodoAdyacencia y = vb.getPrimerAdyacente();
        while (x != null && y != null) {
            int comparacion = x.getDestino().getNombre().compareToIgnoreCase(y.getDestino().getNombre());
            if (comparacion == 0) {
                comunes.add(x.getDestino().getNombre());
                x = x.getSiguiente();
                y = y.getSiguiente();
            } else if (comparacion < 0) {
                x = x.getSiguiente();
            } else {
                y = y.getSiguiente();
            }
        }
        return comunes;
    }

    /** Ordenamiento por insercion propio: coloca al candidato en su posicion. */
    private static void insertarOrdenado(ArrayList<Candidato> lista, Candidato nuevo) {
        int posicion = 0;
        while (posicion < lista.size() && lista.get(posicion).compararCon(nuevo) <= 0) {
            posicion++;
        }
        lista.add(posicion, nuevo);
    }
}
