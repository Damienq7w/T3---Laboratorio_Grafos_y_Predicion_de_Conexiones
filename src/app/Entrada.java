package app;

import java.util.Scanner;

/**
 * Lectura validada del teclado. Ningun metodo lanza excepcion:
 * si el dato es invalido, muestra el error y vuelve a pedirlo.
 *
 * Responsable: Luis Silva
 */
public class Entrada {
    private static final int LONGITUD_MAXIMA = 40;
    private final Scanner scanner;

    public Entrada(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Repite hasta recibir un entero entre min y max.
     * Si la entrada se termina (fin de archivo), devuelve min.
     */
    public int leerEntero(String mensaje, int min, int max) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) {
                return min;
            }
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("Error: ingrese un numero entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: '" + linea + "' no es un numero entero valido.");
            }
        }
    }

    /**
     * Repite hasta recibir un nombre valido: no vacio, solo letras y espacios,
     * maximo 40 caracteres. Devuelve el nombre sin espacios sobrantes.
     * Si la entrada se termina, devuelve "".
     */
    public String leerNombre(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) {
                return "";
            }
            String error = validarNombre(linea);
            if (error == null) {
                return linea.replaceAll("\\s+", " ");
            }
            System.out.println("Error: " + error);
        }
    }

    /** Pregunta s/n. Acepta s, si, n, no (sin importar mayusculas). */
    public boolean confirmar(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            if (linea == null) {
                return false;
            }
            String respuesta = linea.toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Error: responda 's' o 'n'.");
        }
    }

    /** Devuelve la linea sin espacios al inicio/fin, o null si ya no hay entrada. */
    private String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            System.out.println();
            return null;
        }
        return scanner.nextLine().trim();
    }

    /** null si el nombre es valido; si no, el motivo del error. */
    private String validarNombre(String texto) {
        if (texto.isEmpty()) {
            return "el nombre no puede estar vacio.";
        }
        if (texto.length() > LONGITUD_MAXIMA) {
            return "el nombre no puede superar " + LONGITUD_MAXIMA + " caracteres.";
        }
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return "use solo letras sin tilde y espacios (sin numeros ni simbolos).";
            }
        }
        return null;
    }
}
