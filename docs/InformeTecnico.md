# Informe técnico — T3 Laboratorio de grafos y predicción de conexiones

**Grupo 2 · Red de colaboración académica**  
Universidad Técnica de Ambato · Ingeniería de Software · Estructura de Datos

**Responsable de la documentación:** Josue Jampier Camacho.

## 1. Problema y objetivo

El proyecto permite registrar colaboraciones entre estudiantes y sugerir compañeros para futuros trabajos académicos. Cada estudiante se representa como un vértice y cada colaboración previa como una arista no dirigida. Si Ana colaboró con Jose, la relación aparece en la lista de ambos.

Para recomendar compañeros se cuentan los colaboradores en común. El sistema excluye al estudiante consultado y a quienes ya colaboraron directamente con él.

## 2. Diseño del grafo

Se usaron listas de adyacencia enlazadas con nodos propios. `NodoVertice` representa al estudiante y guarda el inicio de su lista de colaboradores. Cada `NodoAdyacencia` contiene una referencia al vértice del colaborador y al siguiente nodo de la lista.

Las listas de adyacencia ocupan O(V + E) de memoria, donde V es la cantidad de estudiantes y E la cantidad de colaboraciones. En una red donde cada estudiante se relaciona con pocos compañeros, esto permite recorrer solo sus conexiones. Una matriz de adyacencia ocuparía O(V²) y permitiría consultar una conexión en O(1) con los índices de ambos estudiantes. Para este proyecto se eligieron listas porque los recorridos y las recomendaciones necesitan visitar a los vecinos de cada estudiante.

| Clase                             | Responsabilidad                                                                                          |
| --------------------------------- | -------------------------------------------------------------------------------------------------------- |
| `Grafo`                           | Registrar, buscar, eliminar y mostrar estudiantes y colaboraciones.                                      |
| `ValidadorIntegridad`             | Detectar duplicados, autorrelaciones, referencias inválidas, falta de simetría y contadores incorrectos. |
| `Recorridos`, `Cola` y `Pila`     | Ejecutar BFS y DFS con estructuras auxiliares propias.                                                   |
| `Recomendador` y `Candidato`      | Calcular colaboradores comunes y ordenar las recomendaciones.                                            |
| `Estadisticas`                    | Calcular grados, densidad, estudiantes aislados y componentes conectados.                                |
| `Main`, `Entrada` y `DatosPrueba` | Mostrar el menú, validar las entradas y cargar la red de ejemplo.                                        |
| `PruebasGrafo`                    | Ejecutar las verificaciones automáticas.                                                                 |

Los nombres se comparan sin distinguir mayúsculas y minúsculas. Las listas se mantienen en orden alfabético para que los recorridos tengan un orden reproducible y se puedan comparar las listas de vecinos en una sola pasada.

## 3. Algoritmos

### 3.1 BFS

BFS recorre el grafo por niveles usando una cola. Se marca al estudiante inicial y se lo encola. Al sacar un estudiante de la cola, se agrega al resultado y se encolan sus colaboradores aún sin marcar. Marcar al encolar evita que un mismo estudiante entre dos veces.

### 3.2 DFS

DFS recorre el grafo en profundidad usando una pila. Al sacar un estudiante por primera vez, se marca como visitado y se agrega al resultado. Sus vecinos pendientes se apilan en orden inverso para que el vecino que va primero alfabéticamente salga antes.

### 3.3 Colaboradores comunes

Se comparan las dos listas ordenadas con un puntero en cada una. Si los nombres coinciden, se registra un colaborador común y se avanzan ambos punteros. Si difieren, se avanza el puntero de la lista que tiene el nombre menor. El puntaje es la cantidad de coincidencias.

### 3.4 Recomendación de compañeros

Primero se identifican los colaboradores directos del estudiante consultado. Luego se recorre la lista de estudiantes una vez y se excluyen el propio estudiante y sus colaboradores directos. Los candidatos con al menos un colaborador común se ordenan por puntaje descendente. Si hay empate, se usa el orden alfabético. El programa muestra hasta tres candidatos e indica con quiénes colaboraron ambos.

En los datos de prueba, Bruno, Elena y Pedro comparten con Jose a Ana y Maria. Los tres obtienen dos puntos y aparecen en orden alfabético. Después de registrar la colaboración Jose–Pedro, Pedro deja de aparecer y Carla ocupa el tercer lugar con un punto.

## 4. Complejidad

Se usa V para la cantidad de vértices, E para la cantidad de aristas, d(A) para el grado de A y k para el número de candidatos con puntaje positivo.

| Operación                             | Costo en el peor caso    | Motivo                                                                                                |
| ------------------------------------- | ------------------------ | ----------------------------------------------------------------------------------------------------- |
| Registrar o buscar un estudiante      | O(V)                     | Se recorre la lista principal. El registro también actualiza los índices.                             |
| Crear o eliminar una colaboración A–B | O(V + d(A) + d(B))       | Se buscan los extremos y se actualizan ambas listas de adyacencia.                                    |
| Eliminar un estudiante                | O(V + E)                 | Se quitan sus referencias de las listas de sus colaboradores y se actualizan los índices.             |
| Mostrar el grafo                      | O(V + E)                 | Se recorren los estudiantes y sus listas de adyacencia.                                               |
| BFS y DFS                             | O(V + E)                 | Se usan índices para consultar los visitados y se inspeccionan las listas de los vértices alcanzados. |
| Consultar vecinos comunes de A y B    | O(V + d(A) + d(B))       | Se buscan ambos estudiantes y se intersectan sus listas ordenadas.                                    |
| Recomendar a un estudiante A          | O(V + V · d(A) + E + k²) | Se comparan las listas de los candidatos y se los ordena por inserción.                               |
| Detectar componentes conectados       | O(V + E)                 | Se comparte el registro de visitados entre los recorridos de los componentes.                         |

## 5. Datos y verificación

`DatosPrueba.java` carga 12 estudiantes y 17 colaboraciones. El conjunto incluye dos grupos sin conexiones entre sí y un estudiante aislado, Xavier. También permite comprobar el desempate de recomendaciones y la exclusión de colaboradores directos.

En la ejecución local del proyecto integrado se aprobaron las 58 pruebas automáticas, sin fallos. Se verificaron las operaciones con vértices y aristas, BFS y DFS, los vecinos comunes, las recomendaciones y la integridad después de realizar altas y bajas.

Las entradas y salidas del menú se documentan en [CasosPrueba.md](../pruebas/CasosPrueba.md). Las capturas de visualización, recorridos y recomendaciones se guardan en [evidencias](../evidencias/).

## 6. Conclusiones

Las listas de adyacencia permiten representar la red con memoria proporcional a los estudiantes y las colaboraciones registradas. BFS muestra a quiénes se puede alcanzar por niveles, mientras que DFS explora una rama antes de continuar con las demás.

El puntaje por colaboradores comunes permite explicar cada recomendación. El caso de Jose y Pedro muestra cómo cambia el resultado al registrar una colaboración directa. El orden alfabético mantiene el mismo resultado cuando varios candidatos tienen igual puntaje.
