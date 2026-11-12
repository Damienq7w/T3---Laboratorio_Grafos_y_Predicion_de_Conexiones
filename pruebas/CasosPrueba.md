# Casos de prueba — Grupo 2

**Proyecto:** Red de colaboración académica  
**Responsable:** Josue Jampier Camacho

## Datos iniciales

Ejecuta `java -cp bin app.Main` y responde `s` para cargar los datos
de `DatosPrueba.java`. El grafo comienza con **12 estudiantes, 17
colaboraciones y 3 componentes conectados**. Xavier está aislado.

## Pruebas automáticas

```powershell
java -cp bin pruebas.PruebasGrafo
```

Resultado de la ejecución local:

```text
Resultado: 58 aprobadas, 0 fallidas.
```

Las pruebas cubren el registro y la eliminación de estudiantes y
colaboraciones, la visualización, BFS, DFS, los vecinos comunes,
las recomendaciones, las estadísticas y la integridad del grafo.

## Casos del menú con datos precargados

Ejecuta los casos en el orden indicado. Los cambios en el grafo se
mantienen durante la sesión.

| Caso | Entrada u operación                        | Resultado de referencia                                                                                                                     |
| ---- | ------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------- |
| CP01 | `5`                                        | Se muestran las listas de adyacencia con 12 estudiantes y 17 colaboraciones. Xavier aparece sin colaboradores.                              |
| CP02 | `6` → `Jose`                               | BFS: Jose → Ana → Maria → Bruno → Carla → Elena → Pedro → Diego. Se alcanzan 8 de 12 estudiantes.                                           |
| CP03 | `7` → `Jose`                               | DFS: Jose → Ana → Bruno → Carla → Diego → Elena → Maria → Pedro. Se alcanzan 8 de 12 estudiantes.                                           |
| CP04 | `8` → `Jose` → `Pedro`                     | Comparten a Ana y Maria. Puntaje: 2.                                                                                                        |
| CP05 | `9` → `Jose`                               | Aparecen Bruno, Elena y Pedro, con 2 puntos cada uno. El empate se resuelve alfabéticamente. Jose, Ana y Maria quedan fuera.                |
| CP06 | `2` → `Jose` → `Pedro`, luego `9` → `Jose` | Se crea la colaboración. La nueva lista es Bruno (2), Elena (2) y Carla (1). Pedro queda fuera porque ahora colabora directamente con Jose. |
| CP07 | `9` → `Xavier`                             | Xavier no tiene recomendaciones.                                                                                                            |
| CP08 | `6` → `Sofia`                              | BFS: Sofia → Tomas → Valeria. Se alcanzan 3 de 12 estudiantes.                                                                              |
| CP09 | `10`                                       | Se muestran 12 estudiantes, 18 colaboraciones, 3 componentes, Xavier aislado e integridad correcta. La arista número 18 se añadió en CP06.  |
| CP10 | `3` → `Jose` → `Pedro`                     | Se elimina la colaboración de ambos extremos y el total vuelve a 17.                                                                        |
| CP11 | `4` → `Ana` → `s`, luego `5` y `10`        | Quedan 11 estudiantes y 12 colaboraciones. Se eliminan las cinco conexiones de Ana y la integridad se mantiene.                             |

### Orden de exploración

BFS usa una cola. Desde Jose, primero visita a Ana y Maria. En el
siguiente nivel visita a Bruno, Carla, Elena y Pedro. Diego queda en
el tercer nivel.

DFS usa una pila. Los vecinos se apilan en orden inverso para explorar
primero al que va antes alfabéticamente entre los que quedan por visitar.

### Salida de la recomendación para Jose

Antes de conectar a Jose con Pedro:

```text
Companeros de proyecto recomendados para Jose:
  1. Bruno (2): Ana, Maria
  2. Elena (2): Ana, Maria
  3. Pedro (2): Ana, Maria
```

Después de crear la colaboración directa:

```text
Companeros de proyecto recomendados para Jose:
  1. Bruno (2): Ana, Maria
  2. Elena (2): Ana, Maria
  3. Carla (1): Ana
```

## Validaciones del menú

Reinicia el programa y responde `n` para empezar con el grafo vacío.
Ejecuta los casos en orden. En CP16 y CP17, prueba ambas entradas
inválidas y luego escribe `Ana` para completar el registro.

| Caso | Entrada u operación                                   | Resultado de referencia                                                                     |
| ---- | ----------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| CP12 | Escribir `abc` como opción.                           | Se rechaza la entrada porque debe ser un número entero.                                     |
| CP13 | Escribir `15` o `-1` como opción.                     | Se solicita un número entre 0 y 10.                                                         |
| CP14 | `5` con el grafo vacío.                               | Se informa que el grafo está vacío.                                                         |
| CP15 | `6` con el grafo vacío.                               | Se indica que primero hay que registrar estudiantes.                                        |
| CP16 | `1` → pulsar Enter con el nombre vacío.               | Se solicita un nombre válido.                                                               |
| CP17 | Escribir `Ana1`, luego `Ana`.                         | Se rechaza el nombre con números y se registra `Ana`.                                       |
| CP18 | `1` → `ana` después de registrar `Ana`.               | Se rechaza el duplicado aunque cambien las mayúsculas.                                      |
| CP19 | Registrar `Bruno` con `1`, luego `2` → `Ana` → `Ana`. | Se rechaza la colaboración consigo misma.                                                   |
| CP20 | `2` → `Ana` → `Zoe`.                                  | Se informa que `Zoe` no existe.                                                             |
| CP21 | `2` → `Ana` → `Bruno`, luego `2` → `Bruno` → `Ana`.   | Se crea la primera conexión y se rechaza la segunda como duplicada.                         |
| CP22 | `3` → `Ana` → `Carla`.                                | Se informa que `Carla` no existe.                                                           |
| CP23 | `8` → `Ana` → `Bruno`.                                | No tienen colaboradores comunes, el puntaje es 0 y se indica que ya colaboran directamente. |
| CP24 | `9` → `Ana`.                                          | No aparecen recomendaciones.                                                                |
| CP25 | `4` → `Bruno` → `x` → `n`.                            | Se pide una respuesta válida y se cancela la eliminación. Bruno sigue registrado.           |

## Evidencias

- [Visualización del grafo](../evidencias/grafo.png) — CP01.
- [Recorridos BFS y DFS](../evidencias/bfs-dfs.png) — CP02 y CP03.
- [Recomendaciones antes y después](../evidencias/recomendaciones.png) — CP05 y CP06.
