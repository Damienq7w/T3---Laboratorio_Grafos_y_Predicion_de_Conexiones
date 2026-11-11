package modelo;

import java.util.ArrayList;

/**
 * Resultado de una recomendacion: un estudiante sugerido como companero de proyecto,
 * su puntaje y los colaboradores que comparte con el estudiante consultado.
 *
 * Responsable: Monica Tacuri
 */
public class Candidato {
    private String nombre;
    private int puntaje;                          // numero de colaboradores en comun
    private ArrayList<String> colaboradoresComunes;

    public Candidato(String nombre, ArrayList<String> colaboradoresComunes) {
        this.nombre = nombre;
        this.colaboradoresComunes = colaboradoresComunes;
        this.puntaje = colaboradoresComunes.size();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public ArrayList<String> getColaboradoresComunes() {
        return colaboradoresComunes;
    }

    /**
     * Criterio de orden de las recomendaciones.
     * Devuelve un numero negativo si este candidato debe ir ANTES que el otro.
     * 1. Mayor puntaje primero.
     * 2. Si empatan, orden alfabetico del nombre (desempate determinista).
     */
    public int compararCon(Candidato otro) {
        if (this.puntaje != otro.puntaje) {
            return otro.puntaje - this.puntaje;
        }
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    /** Ejemplo: "Pedro (2): Ana, Maria" */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append(nombre).append(" (").append(puntaje).append("): ");
        for (int i = 0; i < colaboradoresComunes.size(); i++) {
            if (i > 0) {
                texto.append(", ");
            }
            texto.append(colaboradoresComunes.get(i));
        }
        return texto.toString();
    }
}
