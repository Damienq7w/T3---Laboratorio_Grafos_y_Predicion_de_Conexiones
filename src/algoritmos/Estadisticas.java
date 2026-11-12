package algoritmos;

import estructuras.Cola;
import estructuras.Grafo;
import java.util.ArrayList;
import java.util.Locale;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Opcion 10 del menu: estadisticas de la red de colaboracion.
 *
 */
public class Estadisticas {

    /** Grado promedio = 2E / V (cada colaboracion suma 1 a dos estudiantes). */
    public static double gradoPromedio(Grafo grafo) {
        if (grafo.getCantidadVertices() == 0) {
            return 0;
        }
        return (2.0 * grafo.getCantidadAristas()) / grafo.getCantidadVertices();
    }

    /** Densidad = E / (V(V-1)/2): que tan cerca esta de que todos colaboren con todos. */
    public static double densidad(Grafo grafo) {
        int v = grafo.getCantidadVertices();
        if (v < 2) {
            return 0;
        }
        return grafo.getCantidadAristas() / (v * (v - 1) / 2.0);
    }

    /** Estudiantes con mas colaboraciones (puede haber empate). */
    public static ArrayList<String> mayorGrado(Grafo grafo) {
        ArrayList<String> resultado = new ArrayList<>();
        int maximo = -1;
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            int g = contarVecinos(actual);
            if (g > maximo) {
                maximo = g;
                resultado.clear();
                resultado.add(actual.getNombre());
            } else if (g == maximo) {
                resultado.add(actual.getNombre());
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    /** Estudiantes sin ninguna colaboracion. */
    public static ArrayList<String> aislados(Grafo grafo) {
        ArrayList<String> resultado = new ArrayList<>();
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            if (actual.getPrimerAdyacente() == null) {
                resultado.add(actual.getNombre());
            }
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    /**
     * Cantidad de componentes conectados (grupos de estudiantes sin relacion entre si).
     * Por cada vertice no visitado se lanza un BFS que marca todo su componente. O(V + E)
     */
    public static int componentesConectados(Grafo grafo) {
        boolean[] visitado = new boolean[grafo.getCantidadVertices()];
        int componentes = 0;
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            if (!visitado[actual.getIndice()]) {
                componentes++;
                marcarComponente(actual, visitado);
            }
            actual = actual.getSiguiente();
        }
        return componentes;
    }

    /** Texto con todas las estadisticas para mostrar en el menu. */
    public static String resumen(Grafo grafo) {
        StringBuilder texto = new StringBuilder();
        texto.append("Total de estudiantes (vertices): ").append(grafo.getCantidadVertices()).append("\n");
        texto.append("Total de colaboraciones (aristas): ").append(grafo.getCantidadAristas()).append("\n");
        texto.append("Grado promedio: ").append(String.format(Locale.ROOT, "%.2f", gradoPromedio(grafo))).append("\n");
        texto.append("Densidad: ").append(String.format(Locale.ROOT, "%.2f", densidad(grafo))).append("\n");

        ArrayList<String> mayores = mayorGrado(grafo);
        if (mayores.isEmpty()) {
            texto.append("Mayor numero de colaboraciones: -\n");
        } else {
            int g = grafo.grado(mayores.get(0));
            texto.append("Mayor numero de colaboraciones (").append(g).append("): ")
                    .append(unir(mayores)).append("\n");
        }
        ArrayList<String> sinColaboraciones = aislados(grafo);
        texto.append("Estudiantes aislados: ")
                .append(sinColaboraciones.isEmpty() ? "ninguno" : unir(sinColaboraciones)).append("\n");
        texto.append("Componentes conectados: ").append(componentesConectados(grafo));
        return texto.toString();
    }

    private static void marcarComponente(NodoVertice inicio, boolean[] visitado) {
        Cola cola = new Cola();
        visitado[inicio.getIndice()] = true;
        cola.encolar(inicio);
        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();
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

    private static String unir(ArrayList<String> lista) {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(lista.get(i));
        }
        return texto.toString();
    }
}
