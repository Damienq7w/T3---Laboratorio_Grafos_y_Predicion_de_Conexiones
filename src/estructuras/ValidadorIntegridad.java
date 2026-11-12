package estructuras;

import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * RF10. Revisa que el grafo no tenga duplicados ni referencias invalidas.
 * No modifica el grafo: solo devuelve la lista de problemas encontrados.
 *
 */
public class ValidadorIntegridad {

    /** Lista de problemas encontrados. Vacia si el grafo esta correcto. */
    public static ArrayList<String> validar(Grafo grafo) {
        ArrayList<String> problemas = new ArrayList<>();
        int verticesContados = 0;
        int entradasAdyacencia = 0;

        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            // 1. Nombres de vertices duplicados
            NodoVertice otro = v.getSiguiente();
            while (otro != null) {
                if (v.getNombre().equalsIgnoreCase(otro.getNombre())) {
                    problemas.add("Vertice duplicado: " + v.getNombre());
                }
                otro = otro.getSiguiente();
            }

            // 7. El indice debe coincidir con la posicion en la lista principal
            if (v.getIndice() != verticesContados) {
                problemas.add("Indice incorrecto en " + v.getNombre() + ": tiene " + v.getIndice()
                        + " y deberia ser " + verticesContados);
            }

            NodoAdyacencia ady = v.getPrimerAdyacente();
            while (ady != null) {
                NodoVertice destino = ady.getDestino();
                entradasAdyacencia++;

                // 2. Lazos: un estudiante no puede colaborar consigo mismo
                if (destino == v) {
                    problemas.add("Lazo: " + v.getNombre() + " esta conectado consigo mismo");
                }

                // 3. Aristas repetidas en la misma lista
                NodoAdyacencia resto = ady.getSiguiente();
                while (resto != null) {
                    if (resto.getDestino() == destino) {
                        problemas.add("Colaboracion repetida: " + v.getNombre() + " - " + destino.getNombre());
                    }
                    resto = resto.getSiguiente();
                }

                // 5. Referencias invalidas: el destino debe estar en la lista principal
                if (!estaEnListaPrincipal(grafo, destino)) {
                    problemas.add("Referencia invalida: " + v.getNombre() + " apunta a "
                            + destino.getNombre() + ", que no esta en el grafo");
                } else if (destino != v && !tieneAdyacente(destino, v)) {
                    // 4. Simetria: si A tiene a B, B debe tener a A
                    problemas.add("Colaboracion no simetrica: " + v.getNombre() + " -> "
                            + destino.getNombre() + " existe, pero no al reves");
                }
                ady = ady.getSiguiente();
            }
            verticesContados++;
            v = v.getSiguiente();
        }

        // 6. Los contadores deben coincidir con lo que realmente hay en las listas
        if (verticesContados != grafo.getCantidadVertices()) {
            problemas.add("Contador de vertices incorrecto: registrado " + grafo.getCantidadVertices()
                    + ", contado " + verticesContados);
        }
        if (entradasAdyacencia != grafo.getCantidadAristas() * 2) {
            problemas.add("Contador de aristas incorrecto: registrado " + grafo.getCantidadAristas()
                    + ", entradas en listas " + entradasAdyacencia + " (deberian ser el doble)");
        }
        return problemas;
    }

    public static boolean esValido(Grafo grafo) {
        return validar(grafo).isEmpty();
    }

    private static boolean estaEnListaPrincipal(Grafo grafo, NodoVertice buscado) {
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            if (actual == buscado) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    private static boolean tieneAdyacente(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia ady = origen.getPrimerAdyacente();
        while (ady != null) {
            if (ady.getDestino() == destino) {
                return true;
            }
            ady = ady.getSiguiente();
        }
        return false;
    }
}
