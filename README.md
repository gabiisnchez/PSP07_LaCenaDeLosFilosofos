# 🍝 La Cena de los Filósofos

<div align="center">

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Concurrency](https://img.shields.io/badge/Concurrency-Threads-blue?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Terminado-success?style=for-the-badge)

**Simulación clásica de sincronización y concurrencia mediante Semáforos**

*Proyecto para PSP07 - Programación de Servicios y Procesos*

[Características](#-características) •
[Instalación](#-instalación) •
[Cómo Funciona](#-cómo-funciona) •
[Estructura](#-estructura-del-proyecto) •
[Teoría](#-lógica-de-sincronización)

---

</div>

## 📖 Descripción

**La Cena de los Filósofos** es una implementación en Java del famoso problema propuesto por Edsger Dijkstra para ilustrar los desafíos de la sincronización en sistemas operativos. El proyecto simula a 5 filósofos que compiten por recursos limitados (palillos) evitando problemas clásicos como el *Deadlock* (interbloqueo) y la *Inanición*.

Este proyecto demuestra el uso eficiente de la clase `java.util.concurrent.Semaphore` para gestionar el acceso concurrente a recursos compartidos.

### ✨ Lo que hace especial a esta implementación:

- 🛡️ **Anti-Deadlock**: Implementa una solución de ruptura de simetría (jerarquía de recursos) para evitar bloqueos eternos.
- 🧵 **Multihilo Puro**: Cada filósofo es un hilo independiente (`Thread`) con su propio ciclo de vida.
- 🚥 **Semáforos Binarios**: Gestión precisa de los palillos mediante `acquire()` y `release()`.
- 📊 **Traza Visual**: Salida por consola detallada que muestra el estado de los recursos en tiempo real.
- ⏱️ **Ciclo Finito**: Configurado para terminar tras un número específico de comidas (MAX_COMIDAS = 3), ideal para pruebas y corrección.

---

## 🎮 Características

### ⚙️ Mecánicas de la Simulación

- **5 Filósofos (Hilos)**: Comensales sentados en una mesa circular.
- **5 Palillos (Semáforos)**: Recursos compartidos situados entre cada par de filósofos.
- **Estados del Hilo**:
    1. 💭 **Pensando**: Simula procesamiento (tiempo aleatorio).
    2. 😩 **Hambriento**: Intenta adquirir los semáforos (palillos).
    3. 🍝 **Comiendo**: Mantiene los recursos ocupados (tiempo aleatorio).
    4. ✅ **Terminado**: Libera los recursos y notifica qué palillos quedaron libres.

### 🛡️ Solución al Interbloqueo

A diferencia de las implementaciones ingenuas donde todos toman primero el palillo izquierdo (causando deadlock), este proyecto usa una **Estrategia de Jerarquía** implementada en la clase `Filosofo`:

- **Filósofos Pares**: Toman primero el palillo **Izquierdo** y luego el **Derecho**.
- **Filósofos Impares**: Toman primero el palillo **Derecho** y luego el **Izquierdo**.
- Esto rompe la espera circular y garantiza que siempre progrese la ejecución.

---

## 🚀 Instalación

### Requisitos Previos

- Java Development Kit (JDK) 8 o superior.
- Un IDE como IntelliJ IDEA (recomendado) o Eclipse, o simplemente la terminal.

### Ejecución Paso a Paso

**1. Clona o descarga el repositorio:**

```bash
git clone <url-de-tu-repo>
cd PSP07_LaCenaDeLosFilosofos
```

**2. Compila el código:**

Desde la carpeta raíz del proyecto (src):

```bash
javac -d ../out src/filosofos/*.java
```

**3. Ejecuta la simulación:**

```bash
java -cp ../out filosofos.CenaFilosofos
```

---

## 📁 Estructura del Proyecto

```
PSP07_LaCenaDeLosFilosofos/
├── .idea/                      # Configuración del IDE
├── out/                        # Archivos .class compilados
├── src/
│   └── filosofos/
│       ├── CenaFilosofos.java  # Clase Principal (Main)
│       └── Filosofo.java       # Lógica del Hilo y Semáforos
├── PSP07_..._Documentacion.pdf # Documentación funcional
└── README.md                   # Este archivo
```

### Descripción de Clases

| Archivo | Descripción |
|---------|-------------|
| `CenaFilosofos.java` | Main. Inicializa el array de semáforos (palillos), crea los hilos de los filósofos y gestiona el arranque (start) y espera (join) de la simulación. |
| `Filosofo.java` | Runnable. Define el comportamiento del filósofo: pensar, mecanismo para tomar palillos (evitando deadlock), comer y soltar recursos. Contiene los sleep aleatorios. |

---

## 🧠 Lógica de Sincronización

### El Problema de los Palillos

Cada filósofo necesita dos palillos para comer, pero comparte uno con su vecino izquierdo y otro con el derecho.

### Código Clave (Anti-Deadlock)

En `Filosofo.java`, la lógica de adquisición de recursos es asimétrica:

```java
// Estrategia para evitar Deadlock (Filosofo.java)
if (id % 2 == 0) {
    // Pares: Izquierda -> Derecha
    palilloIzquierdo.acquire();
    palilloDerecho.acquire();
} else {
    // Impares: Derecha -> Izquierda
    palilloDerecho.acquire();
    palilloIzquierdo.acquire();
}
```

### Visualización de Salida

El programa imprime una traza clara para verificar que los recursos se liberan correctamente:

```
Filosofo 1 esta pensando
Filosofo 1 esta hambriento
Filosofo 1 esta comiendo
Filosofo 1 ha terminado de comer, palillos libres: 1, 5
```

---

## ⚙️ Configuración

Puedes ajustar los parámetros de la simulación editando las constantes en `Filosofo.java` y `CenaFilosofos.java`:

### Ajustar Duración

En `Filosofo.java`:

```java
// Límite de comidas antes de finalizar el hilo
private static final int MAX_COMIDAS = 3;

// Tiempos de espera (en milisegundos)
Thread.sleep((long) (Math.random() * 1000)); // Tiempo pensando
Thread.sleep((long) (Math.random() * 1500)); // Tiempo comiendo
```

### Ajustar Comensales

En `CenaFilosofos.java`:

```java
private static final int NUM_FILOSOFOS = 5; // Puedes aumentar o reducir la mesa
```

---

## 🎓 Contexto Educativo

Este proyecto ha sido desarrollado como parte del módulo **PSP (Programación de Servicios y Procesos)**, Unidad 07.

### Objetivos de aprendizaje:

- Comprender la condición de carrera (Race Condition).
- Evitar el abrazo mortal (Deadlock) y la inanición (Starvation).
- Uso práctico de `Semaphore`, `acquire()`, `release()` y `Thread.join()`.

---

## 🤝 Contribuir

Este es un proyecto académico, pero si encuentras formas de optimizar el algoritmo o mejorar la visualización:

1. Haz un Fork.
2. Crea tu rama (`git checkout -b feature/MejorasVisuales`).
3. Haz commit (`git commit -m 'Añadir colores a la consola'`).
4. Push a la rama (`git push origin feature/MejorasVisuales`).
5. Abre un Pull Request.

---

## 👨‍💻 Autor

Desarrollado para la asignatura de PSP.

**Contacto:**
- GitHub: [@gabiisnchez](https://github.com/gabiisnchez)

---

<div align="center">

⭐ Si este código te ayudó a entender los semáforos, dale una estrella en GitHub ⭐

*Hecho con ☕ y Java*

</div>