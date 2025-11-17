package filosofos;

import java.util.concurrent.Semaphore;

/**
 * Clase principal que inicia la simulación del problema de la Cena de los Filósofos.
 * Configura la mesa, los palillos (semáforos) y lanza los hilos.
 */
public class CenaFilosofos {

    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) {

        // Array de semáforos. Cada semáforo representa un palillo.
        // Semaphore(1) indica que es un semáforo binario, es decir, solo 1 hilo puede tenerlo a la vez.
        Semaphore palillos[] = new Semaphore[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            palillos[i] = new Semaphore(1);
        }

        Thread filosofos[] = new Thread[NUM_FILOSOFOS];

        System.out.println("\nCena de los filosofos iniciada.\n");

        // Creación e inicio de los hilos
        for (int i = 0; i < NUM_FILOSOFOS; i++) {

            // Asignación de palillos:
            // palilloIzq es el del índice actual 'i'.
            Semaphore palilloIzq = palillos[i];

            // palilloDer es el siguiente.
            // El operador módulo (%) permite que el último filósofo
            // tome el palillo 0, cerrando el círculo.
            Semaphore palilloDer = palillos[(i + 1) % NUM_FILOSOFOS];

            // Creamos el objeto Runnable (Filosofo)
            Filosofo filosofo = new Filosofo(i, palilloIzq, palilloDer);

            // Creamos el Hilo y le asignamos el Runnable
            filosofos[i] = new Thread(filosofo, "Filosofo-" + i);

            filosofos[i].start();
        }

        // Esperar a que todos los filósofos terminen (join)
        try {
            for (Thread filosofo : filosofos) {
                filosofo.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Error al esperar a los filosofos: " + e.getMessage());
        }

        System.out.println("\nCena de los filosofos finalizada.");
    }
}