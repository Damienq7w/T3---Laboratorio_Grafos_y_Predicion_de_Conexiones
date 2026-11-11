package app;

import estructuras.Grafo;
import java.util.Scanner;

/**
 * Menu de consola del sistema de recomendacion de companeros de proyecto.
 * Es la unica capa que imprime: las demas clases solo devuelven resultados.
 *
 * Responsable: Luis Silva
 */
public class Main {
    private static final Grafo grafo = new Grafo();
    private static Entrada entrada;

    public static void main(String[] args) {
        entrada = new Entrada(new Scanner(System.in));
        System.out.println("==================================================");
        System.out.println("  RED DE COLABORACION ACADEMICA - GRUPO 2 (UTA)");
        System.out.println("==================================================");
        if (entrada.confirmar("Desea cargar los datos de prueba? (s/n): ")) {
            DatosPrueba.cargar(grafo);
            System.out.println("Datos cargados: " + grafo.getCantidadVertices() + " estudiantes y "
                    + grafo.getCantidadAristas() + " colaboraciones.");
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = entrada.leerEntero("Seleccione una opcion: ", 0, 5);
            System.out.println();
            switch (opcion) {
                case 1 -> registrarEstudiante();
                case 2 -> crearColaboracion();
                case 3 -> eliminarColaboracion();
                case 4 -> eliminarEstudiante();
                case 5 -> visualizarGrafo();
                case 0 -> System.out.println("Programa finalizado.");
                default -> System.out.println("Opcion no valida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("------------------- MENU -------------------");
        System.out.println(" 1. Registrar usuario o entidad");
        System.out.println(" 2. Crear conexion");
        System.out.println(" 3. Eliminar conexion");
        System.out.println(" 4. Eliminar usuario o entidad");
        System.out.println(" 5. Visualizar grafo");
        System.out.println(" 0. Salir");
        System.out.println("--------------------------------------------");
    }

    // ---------------------------------------------------------------- 1
    private static void registrarEstudiante() {
        String nombre = entrada.leerNombre("Nombre del estudiante: ");
        if (nombre.isEmpty()) {
            return;
        }
        if (grafo.insertarVertice(nombre)) {
            System.out.println("Estudiante registrado: " + nombre);
        } else {
            System.out.println("No se registro: el estudiante '" + nombre + "' ya existe.");
        }
    }

    // ---------------------------------------------------------------- 2
    private static void crearColaboracion() {
        if (grafo.getCantidadVertices() < 2) {
            System.out.println("Se necesitan al menos 2 estudiantes registrados.");
            return;
        }
        String a = pedirExistente("Primer estudiante: ");
        if (a == null) {
            return;
        }
        String b = pedirExistente("Segundo estudiante: ");
        if (b == null) {
            return;
        }
        if (a.equals(b)) {
            System.out.println("Un estudiante no puede colaborar consigo mismo.");
        } else if (grafo.existeArista(a, b)) {
            System.out.println("La colaboracion " + a + " - " + b + " ya existe.");
        } else {
            grafo.insertarArista(a, b);
            System.out.println("Colaboracion creada: " + a + " - " + b);
        }
    }

    // ---------------------------------------------------------------- 3
    private static void eliminarColaboracion() {
        if (!hayDatos()) {
            return;
        }
        String a = pedirExistente("Primer estudiante: ");
        if (a == null) {
            return;
        }
        String b = pedirExistente("Segundo estudiante: ");
        if (b == null) {
            return;
        }
        if (grafo.eliminarArista(a, b)) {
            System.out.println("Colaboracion eliminada: " + a + " - " + b);
        } else {
            System.out.println("No existe una colaboracion entre " + a + " y " + b + ".");
        }
    }

    // ---------------------------------------------------------------- 4
    private static void eliminarEstudiante() {
        if (!hayDatos()) {
            return;
        }
        String nombre = pedirExistente("Estudiante a eliminar: ");
        if (nombre == null) {
            return;
        }
        int colaboraciones = grafo.grado(nombre);
        if (entrada.confirmar("Eliminar a " + nombre + " (colaboraciones que se borraran: "
                + colaboraciones + ")? (s/n): ")) {
            grafo.eliminarVertice(nombre);
            System.out.println("Estudiante " + nombre + " eliminado junto con sus colaboraciones.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    // ---------------------------------------------------------------- 5
    private static void visualizarGrafo() {
        System.out.println("Listas de adyacencia (" + grafo.getCantidadVertices() + " estudiantes, "
                + grafo.getCantidadAristas() + " colaboraciones):");
        System.out.println(grafo.mostrar());
    }

    // ---------------------------------------------------------- auxiliares

    /** Pide un nombre y verifica que exista. Devuelve el nombre tal como esta guardado, o null. */
    private static String pedirExistente(String mensaje) {
        String nombre = entrada.leerNombre(mensaje);
        if (nombre.isEmpty()) {
            return null;
        }
        if (!grafo.existeVertice(nombre)) {
            System.out.println("No existe el estudiante '" + nombre + "'.");
            return null;
        }
        return grafo.buscarVertice(nombre).getNombre();
    }

    private static boolean hayDatos() {
        if (grafo.estaVacio()) {
            System.out.println("El grafo esta vacio. Registre estudiantes primero (opcion 1).");
            return false;
        }
        return true;
    }
}
