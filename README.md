# T3 · Laboratorio de grafos y predicción de conexiones

**Grupo 2 · Red de colaboración académica**  
Universidad Técnica de Ambato · Ingeniería de Software · Estructura de Datos

## Objetivo

Representar las colaboraciones entre estudiantes mediante un grafo y recomendar compañeros de proyecto a partir de los colaboradores que tienen en común. La aplicación de consola permite registrar y eliminar estudiantes y conexiones, visualizar la red, recorrerla y consultar sus estadísticas.

## Integrantes y aportes

| Integrante            | Usuario de GitHub               | Aporte                                                                  |
| --------------------- | ------------------------------- | ----------------------------------------------------------------------- |
| Damian Cunalata       | `Damienq7w`                     | Operaciones del grafo y pruebas automáticas.                            |
| Kenneth Chalco        | `KEN3DYY`                       | Nodos de vértice y adyacencia, y validación de integridad.              |
| Darwin Tisalema       | `Joel03032007`                  | Cola, pila, recorridos BFS y DFS, y estadísticas.                       |
| Mónica Tacuri         | `tacurisantillanmonicasara-dot` | Candidatos, colaboradores comunes y recomendaciones.                    |
| Luis Silva            | `luuissilva`                    | Menú de consola y validación de entradas.                               |
| Josue Jampier Camacho | `jampiercamacho94-cloud`        | Datos de prueba, informe técnico, README, casos de prueba y evidencias. |

## Cómo ejecutar el programa

Con el JDK instalado, abre la terminal **PowerShell** de VS Code en la raíz del repositorio y compila:

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Recurse -Filter *.java -File).FullName
```

Para abrir el menú:

```powershell
java -cp bin app.Main
```

Responde `s` para cargar **12 estudiantes y 17 colaboraciones**, o `n` para empezar con el grafo vacío.

Para ejecutar las pruebas automáticas:

```powershell
java -cp bin pruebas.PruebasGrafo
```

## Cómo funciona el grafo

Cada estudiante es un vértice y cada colaboración es una arista no dirigida. El proyecto utiliza nodos propios y listas de adyacencia. Una colaboración aparece en la lista de ambos estudiantes y se cuenta una sola vez. Las listas se mantienen en orden alfabético para que los recorridos sean reproducibles.

- **BFS** usa una cola y visita a los estudiantes por niveles. Desde `Jose`, primero alcanza a `Ana` y `Maria`.
- **DFS** usa una pila y explora en profundidad. Desde `Jose`, comienza por `Ana` y continúa por `Bruno` antes de regresar a otras ramas.
- **Recomendaciones** compara las listas de colaboradores para calcular el puntaje de cada candidato. Excluye al estudiante consultado y a sus colaboradores directos. Muestra hasta tres candidatos por puntaje descendente y resuelve los empates por orden alfabético.

## Resultados

La ejecución local terminó con **58 pruebas aprobadas y 0 fallidas**. En los datos de ejemplo, `Bruno`, `Elena` y `Pedro` comparten con `Jose` a `Ana` y `Maria`. Cada candidato obtiene dos puntos. Al registrar la colaboración `Jose–Pedro`, `Pedro` sale de la lista y `Carla` ocupa el tercer lugar.

## Documentación y evidencias

- [Informe técnico](docs/InformeTecnico.md)
- [Casos de prueba](pruebas/CasosPrueba.md)
- [Visualización del grafo](evidencias/grafo.png)
- [Recorridos BFS y DFS](evidencias/bfs-dfs.png)
- [Recomendaciones antes y después de crear una conexión](evidencias/recomendaciones.png)
- [Guía de evidencias](evidencias/LEEME.md)

## Integración del equipo

| Aporte                                      | Pull request       |
| ------------------------------------------- | ------------------ |
| Menú y validación de entradas               | [#1](../../pull/1) |
| Nodos y validación de integridad            | [#2](../../pull/2) |
| Candidatos y recomendaciones                | [#3](../../pull/3) |
| Operaciones del grafo y pruebas automáticas | [#4](../../pull/4) |
| Recorridos y estadísticas                   | [#5](../../pull/5) |

Los commits y las conversaciones de cada pull request permiten consultar qué se integró y qué observaciones quedaron registradas.
