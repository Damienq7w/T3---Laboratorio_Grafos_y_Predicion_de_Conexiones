package estructuras;

import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Grafo NO dirigido implementado con listas de adyacencia y nodos propios.
 *
 * - Lista principal: lista enlazada de NodoVertice ordenada alfabeticamente.
 * - Cada vertice tiene su lista enlazada de NodoAdyacencia, tambien ordenada.
 * - Cada colaboracion A-B se guarda dos veces (A tiene a B y B tiene a A),
 *   pero cantidadAristas la cuenta una sola vez.
 *
 * Mantener todo ordenado hace que BFS, DFS y las recomendaciones
 * den siempre el mismo resultado (orden determinista).
 *
 * Responsable: Damian Cunalata
 */
public class Grafo {
    private NodoVertice primero;
    private int cantidadVertices;
    private int cantidadAristas;

    public Grafo() {
        this.primero = null;
        this.cantidadVertices = 0;
        this.cantidadAristas = 0;
    }

    // =====================================================================
    // Vertices
    // =====================================================================

    /** RF01. Registra un estudiante. false si el nombre esta vacio o ya existe. O(V) */
    public boolean insertarVertice(String nombre) {
        if (nombre == null) {
            return false;
        }
        String limpio = nombre.trim();
        if (limpio.isEmpty() || existeVertice(limpio)) {
            return false;
        }
        NodoVertice nuevo = new NodoVertice(limpio);
        if (primero == null || limpio.compareToIgnoreCase(primero.getNombre()) < 0) {
            nuevo.setSiguiente(primero);
            primero = nuevo;
        } else {
            NodoVertice actual = primero;
            while (actual.getSiguiente() != null
                    && actual.getSiguiente().getNombre().compareToIgnoreCase(limpio) < 0) {
                actual = actual.getSiguiente();
            }
            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }
        cantidadVertices++;
        renumerar();
        return true;
    }

    /** Busca un estudiante por nombre (sin distinguir mayusculas). null si no existe. O(V) */
    public NodoVertice buscarVertice(String nombre) {
        if (nombre == null) {
            return null;
        }
        String limpio = nombre.trim();
        NodoVertice actual = primero;
        while (actual != null) {
            if (actual.getNombre().equalsIgnoreCase(limpio)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean existeVertice(String nombre) {
        return buscarVertice(nombre) != null;
    }

    /**
     * RF04. Elimina un estudiante y TODAS sus colaboraciones.
     * Primero lo quita de la lista de cada colaborador y luego de la lista principal.
     * false si no existe. O(V + E)
     */
    public boolean eliminarVertice(String nombre) {
        NodoVertice objetivo = buscarVertice(nombre);
        if (objetivo == null) {
            return false;
        }
        NodoAdyacencia ady = objetivo.getPrimerAdyacente();
        while (ady != null) {
            quitarAdyacente(ady.getDestino(), objetivo);
            cantidadAristas--;
            ady = ady.getSiguiente();
        }
        objetivo.setPrimerAdyacente(null);

        if (primero == objetivo) {
            primero = objetivo.getSiguiente();
        } else {
            NodoVertice actual = primero;
            while (actual.getSiguiente() != objetivo) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(objetivo.getSiguiente());
        }
        objetivo.setSiguiente(null);
        cantidadVertices--;
        renumerar();
        return true;
    }

    // =====================================================================
    // Aristas
    // =====================================================================

    /**
     * RF02. Crea la colaboracion a-b en ambos sentidos.
     * false si falta alguno, si a y b son el mismo o si la colaboracion ya existe. O(V + g)
     */
    public boolean insertarArista(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || va == vb || contieneAdyacente(va, vb)) {
            return false;
        }
        agregarAdyacenteOrdenado(va, vb);
        agregarAdyacenteOrdenado(vb, va);
        cantidadAristas++;
        return true;
    }

    public boolean existeArista(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null) {
            return false;
        }
        return contieneAdyacente(va, vb);
    }

    /** RF03. Elimina la colaboracion a-b de ambos extremos. false si no existe. O(V + g) */
    public boolean eliminarArista(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || !contieneAdyacente(va, vb)) {
            return false;
        }
        quitarAdyacente(va, vb);
        quitarAdyacente(vb, va);
        cantidadAristas--;
        return true;
    }

    // =====================================================================
    // Consultas
    // =====================================================================

    /** Nombres de los colaboradores en orden alfabetico. Lista vacia si no existe. */
    public ArrayList<String> obtenerVecinos(String nombre) {
        ArrayList<String> vecinos = new ArrayList<>();
        NodoVertice v = buscarVertice(nombre);
        if (v == null) {
            return vecinos;
        }
        NodoAdyacencia ady = v.getPrimerAdyacente();
        while (ady != null) {
            vecinos.add(ady.getDestino().getNombre());
            ady = ady.getSiguiente();
        }
        return vecinos;
    }

    /** Numero de colaboraciones del estudiante. -1 si no existe. */
    public int grado(String nombre) {
        NodoVertice v = buscarVertice(nombre);
        if (v == null) {
            return -1;
        }
        int contador = 0;
        NodoAdyacencia ady = v.getPrimerAdyacente();
        while (ady != null) {
            contador++;
            ady = ady.getSiguiente();
        }
        return contador;
    }

    /** Posicion 0..n-1 del estudiante en la lista principal. -1 si no existe. */
    public int indiceDe(String nombre) {
        NodoVertice v = buscarVertice(nombre);
        return v == null ? -1 : v.getIndice();
    }

    public ArrayList<String> obtenerNombresVertices() {
        ArrayList<String> nombres = new ArrayList<>();
        NodoVertice actual = primero;
        while (actual != null) {
            nombres.add(actual.getNombre());
            actual = actual.getSiguiente();
        }
        return nombres;
    }

    public NodoVertice getPrimero() {
        return primero;
    }

    public int getCantidadVertices() {
        return cantidadVertices;
    }

    public int getCantidadAristas() {
        return cantidadAristas;
    }

    public boolean estaVacio() {
        return primero == null;
    }

    /**
     * RF05. Listas de adyacencia como texto, una linea por estudiante:
     * "Ana -> Bruno -> Carla"   o   "Xavier -> (sin colaboraciones)"
     */
    public String mostrar() {
        if (estaVacio()) {
            return "El grafo esta vacio.";
        }
        StringBuilder texto = new StringBuilder();
        NodoVertice actual = primero;
        while (actual != null) {
            texto.append(actual.getNombre());
            NodoAdyacencia ady = actual.getPrimerAdyacente();
            if (ady == null) {
                texto.append(" -> (sin colaboraciones)");
            }
            while (ady != null) {
                texto.append(" -> ").append(ady.getDestino().getNombre());
                ady = ady.getSiguiente();
            }
            if (actual.getSiguiente() != null) {
                texto.append("\n");
            }
            actual = actual.getSiguiente();
        }
        return texto.toString();
    }

    // =====================================================================
    // Metodos auxiliares privados
    // =====================================================================

    private boolean contieneAdyacente(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia ady = origen.getPrimerAdyacente();
        while (ady != null) {
            if (ady.getDestino() == destino) {
                return true;
            }
            ady = ady.getSiguiente();
        }
        return false;
    }

    /** Inserta destino en la lista de origen manteniendo el orden alfabetico. */
    private void agregarAdyacenteOrdenado(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia nuevo = new NodoAdyacencia(destino);
        NodoAdyacencia cabeza = origen.getPrimerAdyacente();
        String nombre = destino.getNombre();
        if (cabeza == null || nombre.compareToIgnoreCase(cabeza.getDestino().getNombre()) < 0) {
            nuevo.setSiguiente(cabeza);
            origen.setPrimerAdyacente(nuevo);
            return;
        }
        NodoAdyacencia actual = cabeza;
        while (actual.getSiguiente() != null
                && actual.getSiguiente().getDestino().getNombre().compareToIgnoreCase(nombre) < 0) {
            actual = actual.getSiguiente();
        }
        nuevo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevo);
    }

    /** Quita destino de la lista de adyacencia de origen (si esta). */
    private void quitarAdyacente(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia actual = origen.getPrimerAdyacente();
        NodoAdyacencia anterior = null;
        while (actual != null && actual.getDestino() != destino) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual == null) {
            return;
        }
        if (anterior == null) {
            origen.setPrimerAdyacente(actual.getSiguiente());
        } else {
            anterior.setSiguiente(actual.getSiguiente());
        }
    }

    /** Vuelve a numerar los indices 0..n-1 despues de insertar o eliminar un vertice. O(V) */
    private void renumerar() {
        int i = 0;
        NodoVertice actual = primero;
        while (actual != null) {
            actual.setIndice(i);
            i++;
            actual = actual.getSiguiente();
        }
    }
}
