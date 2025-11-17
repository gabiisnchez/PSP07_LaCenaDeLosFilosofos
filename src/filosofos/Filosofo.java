package filosofos;

import java.util.concurrent.Semaphore;

/**
 * Representa a un filósofo en el problema de la "Cena de los Filósofos".
 * Cada filósofo es un hilo (Thread) que alterna entre pensar y comer.
 * Para comer, necesita adquirir dos recursos compartidos (semáforos),
 * que representan los palillos a su izquierda y derecha.
 */
public class Filosofo implements Runnable {

    // Identificador único del filósofo (1-5).
    private int id;

    // Semáforo que controla el acceso al palillo izquierdo.
    private Semaphore palilloIzquierdo;

    // Semáforo que controla el acceso al palillo derecho.
    private Semaphore palilloDerecho;

    // Contador de cuántas veces ha comido el filósofo.
    private int vecesComido;

    // Límite de comidas para que el hilo termine (evita bucle infinito).
    private static final int MAX_COMIDAS = 3;

    // Variables auxiliares solo para mostrar el mensaje de salida exacto de la captura
    private int idPalilloIzqVisual;
    private int idPalilloDerVisual;

    /**
     * Constructor de la clase Filosofo.
     *
     * @param id          Índice del filósofo (0-4). Se transforma a 1-5 internamente.
     * @param palilloIzq  Referencia al semáforo del palillo izquierdo.
     * @param palilloDer  Referencia al semáforo del palillo derecho.
     */
    public Filosofo(int id, Semaphore palilloIzq, Semaphore palilloDer) {
        this.id = id + 1;
        this.palilloIzquierdo = palilloIzq;
        this.palilloDerecho = palilloDer;
        this.vecesComido = 0;

        // Lógica para calcular los IDs de los palillos (para el print final)
        this.idPalilloIzqVisual = this.id;
        // El derecho es el anterior. Si soy el 1, mi derecha es el 5 (mesa circular).
        this.idPalilloDerVisual = (this.id == 1) ? 5 : this.id - 1;
    }

    /**
     * Metodo principal del hilo. Ejecuta el ciclo de vida del filósofo:
     * Pensar -> Intentar comer -> Comer -> Soltar palillos.
     */
    @Override
    public void run() {
        try {
            while (vecesComido < MAX_COMIDAS) {
                pensar();
                comer();
                vecesComido++;
            }
        } catch (InterruptedException e) {

            // Restablece el estado de interrupción y maneja la salida abrupta
            Thread.currentThread().interrupt();
            System.err.println("Filosofo " + id + " fue interrumpido.");
        }
    }

    /**
     * Simula la acción de pensar.
     * El hilo se duerme un tiempo aleatorio para simular procesamiento.
     *
     * @throws InterruptedException Si el hilo es interrumpido mientras duerme.
     */
    private void pensar() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta pensando");
        Thread.sleep((long) (Math.random() * 1000));
    }

    /**
     * Gestiona la adquisición de los palillos (semáforos) y la acción de comer.
     * Se utiliza una estrategia de jerarquía de recursos o ruptura de simetría.
     * Los filósofos pares cogen primero el izquierdo y luego el derecho.
     * Los impares cogen primero el derecho y luego el izquierdo.
     * Esto rompe la espera circular.
     *
     * @throws InterruptedException Si el hilo es interrumpido mientras espera.
     */
    private void comer() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta hambriento");

        // Estrategia para evitar que todos cojan el palillo izquierdo a la vez
        // y se queden esperando eternamente por el derecho (Deadlock).
        if (id % 2 == 0) {

            // Si el ID es PAR: Intenta coger Izquierda -> Derecha
            palilloIzquierdo.acquire(); // Bloquea hasta que el palillo esté disponible
            palilloDerecho.acquire();   // Bloquea hasta que el otro palillo esté disponible
        } else {
            // Si el ID es IMPAR: Intenta coger Derecha -> Izquierda
            palilloDerecho.acquire();
            palilloIzquierdo.acquire();
        }

        // Si llega aquí, tiene ambos semáforos (permisos)
        System.out.println("Filosofo " + id + " esta comiendo");

        // Simula el tiempo que tarda en comer
        Thread.sleep((long) (Math.random() * 1500));

        // Libera los recursos
        soltarPalillos();
    }

    /**
     * Libera los semáforos de ambos palillos y notifica la finalización.
     * Esto permite que los vecinos puedan usar estos palillos.
     */
    private void soltarPalillos() {

        // .release() incrementa el contador del semáforo, despertando hilos en espera
        palilloIzquierdo.release();
        palilloDerecho.release();

        // Mensaje con formato específico solicitado
        System.out.println("Filosofo " + id + " ha terminado de comer, palillos libres: "
                + idPalilloIzqVisual + ", " + idPalilloDerVisual);
    }
}