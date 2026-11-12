package app;

import estructuras.Grafo;

/**
 * Carga 12 estudiantes y 17 colaboraciones.
 * Los datos permiten probar empates, componentes desconectados
 * y un estudiante aislado.
 *
 * Responsable: Josue Jampier Camacho
 */
public class DatosPrueba {

    private static final String[] ESTUDIANTES = {
        "Ana", "Bruno", "Carla", "Diego", "Elena", "Jose",
        "Maria", "Pedro", "Sofia", "Tomas", "Valeria", "Xavier"
    };

    private static final String[][] COLABORACIONES = {
        // Primer componente: 8 estudiantes y 14 colaboraciones.
        {"Jose", "Ana"}, {"Jose", "Maria"},
        {"Pedro", "Ana"}, {"Pedro", "Maria"},
        {"Ana", "Bruno"}, {"Maria", "Bruno"},
        {"Ana", "Carla"}, {"Carla", "Diego"},
        {"Diego", "Elena"}, {"Elena", "Maria"},
        {"Bruno", "Carla"}, {"Diego", "Pedro"},
        {"Ana", "Elena"}, {"Carla", "Elena"},

        // Segundo componente: 3 estudiantes y 3 colaboraciones.
        {"Sofia", "Tomas"}, {"Tomas", "Valeria"}, {"Sofia", "Valeria"}
        // Xavier queda aislado.
    };

    public static void cargar(Grafo grafo) {
        for (String estudiante : ESTUDIANTES) {
            grafo.insertarVertice(estudiante);
        }

        for (String[] colaboracion : COLABORACIONES) {
            grafo.insertarArista(colaboracion[0], colaboracion[1]);
        }
    }
}