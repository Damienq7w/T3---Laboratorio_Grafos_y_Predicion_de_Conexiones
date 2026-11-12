package pruebas;

import algoritmos.Estadisticas;
import algoritmos.Recomendador;
import algoritmos.Recorridos;
import app.DatosPrueba;
import estructuras.Grafo;
import estructuras.ValidadorIntegridad;
import java.util.ArrayList;
import modelo.Candidato;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Pruebas automaticas y reproducibles de cada operacion (sin JUnit ni librerias externas).
 * Ejecutar:  java -cp bin pruebas.PruebasGrafo
 * Termina con codigo 1 si alguna prueba falla.
 *
 * Responsable: Damian Cunalata
 */
public class PruebasGrafo {
    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS AUTOMATICAS - GRAFO DE COLABORACION (GRUPO 2) ===");
        probarVertices();
        probarAristas();
        probarEliminarArista();
        probarEliminarVertice();
        probarMostrar();
        probarRecorridos();
        probarVecinosComunes();
        probarRecomendaciones();
        probarEstadisticas();
        probarIntegridad();
        System.out.println();
        System.out.println("Resultado: " + aprobadas + " aprobadas, " + fallidas + " fallidas.");
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static Grafo grafoDePrueba() {
        Grafo grafo = new Grafo();
        DatosPrueba.cargar(grafo);
        return grafo;
    }

    // ------------------------------------------------------------- RF01
    private static void probarVertices() {
        titulo("RF01 Registrar vertice");
        Grafo g = new Grafo();
        verificar("Insertar 'Ana' en grafo vacio", g.insertarVertice("Ana"));
        verificar("No permite duplicado 'Ana'", !g.insertarVertice("Ana"));
        verificar("No permite duplicado sin importar mayusculas 'ana'", !g.insertarVertice("ana"));
        verificar("No permite nombre vacio", !g.insertarVertice("   "));
        verificar("No permite null", !g.insertarVertice(null));
        g.insertarVertice("Carla");
        g.insertarVertice("Bruno");
        igual("Lista principal ordenada", "[Ana, Bruno, Carla]", g.obtenerNombresVertices().toString());
        igual("Cantidad de vertices", "3", "" + g.getCantidadVertices());
        verificar("Buscar existente", g.buscarVertice("bruno") != null);
        verificar("Buscar inexistente devuelve null", g.buscarVertice("Zoe") == null);
    }

    // ------------------------------------------------------------- RF02
    private static void probarAristas() {
        titulo("RF02 Crear conexion");
        Grafo g = new Grafo();
        g.insertarVertice("Ana");
        g.insertarVertice("Bruno");
        verificar("Crear Ana-Bruno", g.insertarArista("Ana", "Bruno"));
        verificar("Es bidireccional (Bruno-Ana existe)", g.existeArista("Bruno", "Ana"));
        verificar("No permite duplicada Bruno-Ana", !g.insertarArista("Bruno", "Ana"));
        verificar("No permite lazo Ana-Ana", !g.insertarArista("Ana", "Ana"));
        verificar("No permite vertice inexistente", !g.insertarArista("Ana", "Zoe"));
        igual("Cuenta 1 arista (no 2)", "1", "" + g.getCantidadAristas());
    }

    // ------------------------------------------------------------- RF03
    private static void probarEliminarArista() {
        titulo("RF03 Eliminar conexion");
        Grafo g = grafoDePrueba();
        verificar("Eliminar Jose-Ana", g.eliminarArista("Jose", "Ana"));
        verificar("Se elimina de Jose", !g.obtenerVecinos("Jose").contains("Ana"));
        verificar("Se elimina de Ana", !g.obtenerVecinos("Ana").contains("Jose"));
        verificar("No elimina una conexion inexistente", !g.eliminarArista("Jose", "Ana"));
        igual("Aristas 17 -> 16", "16", "" + g.getCantidadAristas());
        verificar("Integridad OK despues de eliminar", ValidadorIntegridad.esValido(g));
    }

    // ------------------------------------------------------------- RF04
    private static void probarEliminarVertice() {
        titulo("RF04 Eliminar vertice");
        Grafo g = grafoDePrueba();
        verificar("Eliminar Ana (5 colaboraciones)", g.eliminarVertice("Ana"));
        verificar("Ana ya no existe", !g.existeVertice("Ana"));
        verificar("Nadie conserva a Ana como colaborador",
                !g.obtenerVecinos("Bruno").contains("Ana") && !g.obtenerVecinos("Jose").contains("Ana")
                && !g.obtenerVecinos("Pedro").contains("Ana"));
        igual("Vertices 12 -> 11", "11", "" + g.getCantidadVertices());
        igual("Aristas 17 -> 12", "12", "" + g.getCantidadAristas());
        verificar("No elimina inexistente", !g.eliminarVertice("Ana"));
        verificar("Eliminar el primero y el ultimo de la lista",
                g.eliminarVertice("Bruno") && g.eliminarVertice("Xavier"));
        verificar("Integridad OK despues de eliminar", ValidadorIntegridad.esValido(g));
    }

    // ------------------------------------------------------------- RF05
    private static void probarMostrar() {
        titulo("RF05 Mostrar grafo");
        Grafo g = grafoDePrueba();
        String texto = g.mostrar();
        verificar("Linea de Ana ordenada", texto.contains("Ana -> Bruno -> Carla -> Elena -> Jose -> Pedro"));
        verificar("Aislado se muestra", texto.contains("Xavier -> (sin colaboraciones)"));
        igual("Grafo vacio", "El grafo esta vacio.", new Grafo().mostrar());
    }

    // ------------------------------------------------------------- RF06 y RF07
    private static void probarRecorridos() {
        titulo("RF06 BFS y RF07 DFS");
        Grafo g = grafoDePrueba();
        igual("BFS desde Jose",
                "[Jose, Ana, Maria, Bruno, Carla, Elena, Pedro, Diego]", Recorridos.bfs(g, "Jose").toString());
        igual("DFS desde Jose",
                "[Jose, Ana, Bruno, Carla, Diego, Elena, Maria, Pedro]", Recorridos.dfs(g, "Jose").toString());
        igual("BFS en grafo desconectado (solo su componente)",
                "[Sofia, Tomas, Valeria]", Recorridos.bfs(g, "Sofia").toString());
        igual("DFS desde aislado", "[Xavier]", Recorridos.dfs(g, "Xavier").toString());
        verificar("BFS desde inexistente devuelve lista vacia", Recorridos.bfs(g, "Zoe").isEmpty());
    }

    // ------------------------------------------------------------- RF08
    private static void probarVecinosComunes() {
        titulo("RF08 Vecinos comunes");
        Grafo g = grafoDePrueba();
        igual("Jose y Pedro comparten", "[Ana, Maria]", Recomendador.vecinosComunes(g, "Jose", "Pedro").toString());
        igual("Puntaje entero", "2", "" + Recomendador.vecinosComunes(g, "Jose", "Pedro").size());
        verificar("Sin comunes entre componentes distintos",
                Recomendador.vecinosComunes(g, "Jose", "Sofia").isEmpty());
        verificar("Un estudiante consigo mismo no cuenta",
                Recomendador.vecinosComunes(g, "Jose", "Jose").isEmpty());
    }

    // ------------------------------------------------------------- RF09 + reto
    private static void probarRecomendaciones() {
        titulo("RF09 Recomendar conexiones + reto Grupo 2");
        Grafo g = grafoDePrueba();
        ArrayList<Candidato> r = Recomendador.recomendar(g, "Jose", 3);
        igual("Top 3 de Jose (triple empate, desempate alfabetico)",
                "[Bruno (2): Ana, Maria, Elena (2): Ana, Maria, Pedro (2): Ana, Maria]", r.toString());
        verificar("No se recomienda a si mismo", !contiene(r, "Jose"));
        verificar("No recomienda colaboradores directos", !contiene(r, "Ana") && !contiene(r, "Maria"));

        g.insertarArista("Jose", "Pedro");
        r = Recomendador.recomendar(g, "Jose", 3);
        igual("Tras crear Jose-Pedro, Pedro desaparece",
                "[Bruno (2): Ana, Maria, Elena (2): Ana, Maria, Carla (1): Ana]", r.toString());

        verificar("Aislado no recibe recomendaciones", Recomendador.recomendar(g, "Xavier", 3).isEmpty());
        verificar("Inexistente no recibe recomendaciones", Recomendador.recomendar(g, "Zoe", 3).isEmpty());
        igual("Limite 1 devuelve solo el mejor", "1", "" + Recomendador.recomendar(g, "Jose", 1).size());

        ArrayList<Candidato> todos = Recomendador.recomendar(g, "Jose", 100);
        verificar("Sin candidatos duplicados", sinDuplicados(todos));
    }

    // ------------------------------------------------------------- Estadisticas
    private static void probarEstadisticas() {
        titulo("Estadisticas");
        Grafo g = grafoDePrueba();
        igual("Componentes conectados", "3", "" + Estadisticas.componentesConectados(g));
        igual("Aislados", "[Xavier]", Estadisticas.aislados(g).toString());
        igual("Mayor grado", "[Ana]", Estadisticas.mayorGrado(g).toString());
        igual("Componentes tras eliminar Tomas", "3",
                "" + (g.eliminarVertice("Tomas") ? Estadisticas.componentesConectados(g) : -1));
    }

    // ------------------------------------------------------------- RF10
    private static void probarIntegridad() {
        titulo("RF10 Validar integridad");
        Grafo g = grafoDePrueba();
        verificar("Datos de prueba sin problemas", ValidadorIntegridad.esValido(g));
        g.insertarVertice("Zoe");
        g.insertarArista("Zoe", "Xavier");
        g.eliminarVertice("Pedro");
        g.eliminarArista("Ana", "Bruno");
        verificar("Sigue valido tras varias altas y bajas", ValidadorIntegridad.esValido(g));

        // Se rompe el grafo a proposito (saltando la clase Grafo) para comprobar que el validador lo detecta
        Grafo roto = grafoDePrueba();
        NodoVertice xavier = roto.buscarVertice("Xavier");
        xavier.setPrimerAdyacente(new NodoAdyacencia(roto.buscarVertice("Ana")));   // Xavier->Ana sin Ana->Xavier
        ArrayList<String> problemas = ValidadorIntegridad.validar(roto);
        verificar("Detecta colaboracion no simetrica", contieneTexto(problemas, "no simetrica"));
        verificar("Detecta contador de aristas incorrecto", contieneTexto(problemas, "Contador de aristas"));

        Grafo conFantasma = grafoDePrueba();
        NodoVertice fantasma = new NodoVertice("Fantasma");                         // no esta en la lista principal
        conFantasma.buscarVertice("Xavier").setPrimerAdyacente(new NodoAdyacencia(fantasma));
        verificar("Detecta referencia invalida",
                contieneTexto(ValidadorIntegridad.validar(conFantasma), "Referencia invalida"));
    }

    private static boolean contieneTexto(ArrayList<String> lista, String texto) {
        for (String linea : lista) {
            if (linea.contains(texto)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------- utilidades
    private static boolean contiene(ArrayList<Candidato> lista, String nombre) {
        for (Candidato c : lista) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    private static boolean sinDuplicados(ArrayList<Candidato> lista) {
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                if (lista.get(i).getNombre().equalsIgnoreCase(lista.get(j).getNombre())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("-- " + texto);
    }

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("  [OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }

    private static void igual(String descripcion, String esperado, String obtenido) {
        boolean ok = esperado.equals(obtenido);
        verificar(descripcion + (ok ? "" : "  -> esperado " + esperado + " pero se obtuvo " + obtenido), ok);
    }
}
