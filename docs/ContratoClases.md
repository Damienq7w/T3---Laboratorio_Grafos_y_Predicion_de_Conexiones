# Contrato de clases — T3 Grafos (Grupo 2)

**Red de colaboración académica:** cada estudiante es un vértice y cada colaboración previa es una arista (grafo **no dirigido**).

Este documento fija los paquetes, las clases y los métodos públicos. **Todos programan contra estas firmas.** Si alguien necesita cambiar una firma, lo avisa al grupo y se actualiza este archivo mediante un PR antes de usarla.

---

## 1. Reglas generales

| Regla | Decisión |
|---|---|
| Java | JDK 17 o superior (probado con 21); solo la biblioteca estándar |
| Prohibido | `HashMap`, `HashSet`, `TreeMap`, `TreeSet`, librerías de grafos, `LinkedList`/`ArrayDeque`/`Stack` para la cola y la pila |
| Permitido | Arreglos, `ArrayList` para resultados, `Scanner`, clases propias |
| Nombres | Se guardan con `trim()` y se comparan con `equalsIgnoreCase`, así "ana" y "Ana" cuentan como duplicado |
| Orden | La lista de vértices y cada lista de adyacencia se mantienen **ordenadas alfabéticamente** (`compareToIgnoreCase`) |
| Índices | Cada `NodoVertice` tiene un índice de 0 a n−1. `Grafo` lo actualiza en cada alta o baja y los algoritmos lo usan para los `boolean[]` de visitados |
| Tildes | El código fuente va **sin tildes ni ñ**, también en los textos que se imprimen, para evitar problemas de codificación en la consola de Windows |
| Impresión | **Solo el paquete `app`** (y el ejecutor de pruebas `pruebas.PruebasGrafo`) imprime en pantalla. Las demás clases devuelven `false`, `null` o una lista vacía |
| Aislados | Un estudiante sin colaboraciones es válido |

### Compilar y ejecutar (desde la raíz del repo)

```bash
javac -d bin src/modelo/*.java src/estructuras/*.java src/algoritmos/*.java src/app/*.java src/pruebas/*.java
```

```bash
java -cp bin app.Main
```

```bash
java -cp bin pruebas.PruebasGrafo
```

---

## 2. Dueños de archivos

| Integrante | Archivos | Requisitos |
|---|---|---|
| Kenneth Chalco | `src/modelo/NodoVertice.java`, `src/modelo/NodoAdyacencia.java`, `src/estructuras/ValidadorIntegridad.java` | Base y RF10 |
| Damian Cunalata | `src/estructuras/Grafo.java`, `src/pruebas/PruebasGrafo.java`, `.gitignore`, este contrato. Además integra y revisa los PR | RF01–RF05 y pruebas |
| Darwin Tisalema | `src/algoritmos/Recorridos.java`, `src/algoritmos/Estadisticas.java`, `src/estructuras/Cola.java`, `src/estructuras/Pila.java` | RF06, RF07 y opción 10 |
| Mónica Tacuri | `src/algoritmos/Recomendador.java`, `src/modelo/Candidato.java` | RF08, RF09 y reto del Grupo 2 |
| Luis Silva | `src/app/Main.java`, `src/app/Entrada.java` | Menú y validaciones |
| Josue Jampier Camacho | `src/app/DatosPrueba.java`, `README.md`, `pruebas/CasosPrueba.md`, `docs/InformeTecnico.md`, `evidencias/` | Pruebas y documentación |

**Nadie modifica archivos de otro integrante.** Si encuentras un error en otro archivo, coméntalo en el PR o abre un *issue*.

---

## 3. Paquete `modelo`

### `NodoVertice` (Kenneth)
```java
public class NodoVertice {
    private String nombre;
    private int indice;                      // 0..n-1, lo mantiene Grafo
    private NodoAdyacencia primerAdyacente;  // inicio de su lista de colaboradores
    private NodoVertice siguiente;           // siguiente estudiante en la lista principal

    public NodoVertice(String nombre)        // indice = -1
    public String getNombre()
    public int getIndice()
    public void setIndice(int indice)
    public NodoAdyacencia getPrimerAdyacente()
    public void setPrimerAdyacente(NodoAdyacencia primerAdyacente)
    public NodoVertice getSiguiente()
    public void setSiguiente(NodoVertice siguiente)
}
```

### `NodoAdyacencia` (Kenneth)
```java
public class NodoAdyacencia {
    private NodoVertice destino;       // referencia al vértice real
    private NodoAdyacencia siguiente;

    public NodoAdyacencia(NodoVertice destino)
    public NodoVertice getDestino()
    public NodoAdyacencia getSiguiente()
    public void setSiguiente(NodoAdyacencia siguiente)
}
```

### `Candidato` (Mónica)
```java
public class Candidato {
    public Candidato(String nombre, ArrayList<String> colaboradoresComunes) // puntaje = tamaño de la lista
    public String getNombre()
    public int getPuntaje()
    public ArrayList<String> getColaboradoresComunes()
    public int compararCon(Candidato otro)   // negativo si va ANTES: mayor puntaje; empate -> alfabético
    public String toString()                 // "Pedro (2): Ana, Maria"
}
```

---

## 4. Paquete `estructuras`

### `Grafo` (Damian)
```java
public class Grafo {
    public Grafo()
    public boolean insertarVertice(String nombre)      // RF01: false si vacío o duplicado
    public NodoVertice buscarVertice(String nombre)    // null si no existe
    public boolean existeVertice(String nombre)
    public boolean eliminarVertice(String nombre)      // RF04: también elimina sus colaboraciones
    public boolean insertarArista(String a, String b)  // RF02: a-b y b-a; false si falta, a==b o ya existe
    public boolean existeArista(String a, String b)
    public boolean eliminarArista(String a, String b)  // RF03: de ambos extremos
    public ArrayList<String> obtenerVecinos(String nombre)  // alfabético; vacía si no existe
    public int grado(String nombre)                    // -1 si no existe
    public int indiceDe(String nombre)                 // -1 si no existe
    public ArrayList<String> obtenerNombresVertices()
    public NodoVertice getPrimero()
    public int getCantidadVertices()
    public int getCantidadAristas()                    // cada colaboración cuenta 1 vez
    public boolean estaVacio()
    public String mostrar()                            // RF05: "Ana -> Bruno -> Carla" / "Xavier -> (sin colaboraciones)"
}
```

### `ValidadorIntegridad` (Kenneth) – RF10
```java
public class ValidadorIntegridad {
    /** Problemas encontrados (vacía = correcto): 1 vértices duplicados, 2 lazos,
        3 aristas repetidas, 4 simetría, 5 referencias inválidas, 6 contadores, 7 índices */
    public static ArrayList<String> validar(Grafo grafo)
    public static boolean esValido(Grafo grafo)
}
```

### `Cola` y `Pila` (Darwin), con nodos propios
```java
public class Cola { public void encolar(NodoVertice v); public NodoVertice desencolar(); public boolean estaVacia(); }
public class Pila { public void apilar(NodoVertice v);  public NodoVertice desapilar();  public boolean estaVacia(); }
```

---

## 5. Paquete `algoritmos`

### `Recorridos` (Darwin)
```java
public static ArrayList<String> bfs(Grafo grafo, String inicio)  // RF06, con Cola; vacía si no existe
public static ArrayList<String> dfs(Grafo grafo, String inicio)  // RF07, con Pila; vecinos apilados al revés
```

### `Estadisticas` (Darwin)
```java
public static double gradoPromedio(Grafo grafo)
public static double densidad(Grafo grafo)
public static ArrayList<String> mayorGrado(Grafo grafo)
public static ArrayList<String> aislados(Grafo grafo)
public static int componentesConectados(Grafo grafo)
public static String resumen(Grafo grafo)
```

### `Recomendador` (Mónica)
```java
public static ArrayList<String> vecinosComunes(Grafo grafo, String a, String b)       // RF08
public static ArrayList<Candidato> recomendar(Grafo grafo, String nombre, int limite) // RF09 + reto
```

---

## 6. Paquete `app` y `pruebas`

### `Entrada` (Luis)
```java
public Entrada(Scanner scanner)
public int leerEntero(String mensaje, int min, int max)  // repite hasta que sea válido; al fin de la entrada devuelve min
public String leerNombre(String mensaje)                 // solo letras y espacios, máximo 40; al fin de la entrada devuelve ""
public boolean confirmar(String mensaje)                 // s / si / n / no
```

### `Main` (Luis): menú obligatorio
```
1 insertarVertice · 2 insertarArista · 3 eliminarArista · 4 eliminarVertice · 5 mostrar
6 Recorridos.bfs · 7 Recorridos.dfs · 8 Recomendador.vecinosComunes
9 Recomendador.recomendar(grafo, nombre, 3) · 10 Estadisticas.resumen + ValidadorIntegridad.validar · 0 Salir
```

### `DatosPrueba` (Josue)
```java
public static void cargar(Grafo grafo)   // 12 estudiantes y 17 colaboraciones
```

### `PruebasGrafo` (Damian)
```java
public static void main(String[] args)   // pruebas automáticas; termina con código 1 si alguna falla
```
