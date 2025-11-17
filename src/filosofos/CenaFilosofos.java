package filosofos;

import java.util.concurrent.Semaphore;

/**
 * Clase princiopal que simula el problema de la cena de los filósofos
 * usando Semaphore para la sincronización.
 */

public class CenaFilosofos {
    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) {
        Semaphore palillos[] = new Semaphore[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            palillos[i] = new Semaphore(1);
        }

        Thread filosofos[] = new Thread[NUM_FILOSOFOS];

        System.out.println("\nCena de los filósofos iniciada.\n");

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Semaphore palilloIzq = palillos[i];
            Semaphore palilloDer = palillos[(i + 1) % NUM_FILOSOFOS];

            Filosofo filosofo = new Filosofo(i, palilloIzq, palilloDer);
            filosofos[i] = new Thread(filosofo, "Filosofo-" + i);

            filosofos[i].start();
        }

        try {
            for (Thread filosofo : filosofos) {
                filosofo.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Error al esperar a los filósofos: " + e.getMessage());
        }

        System.out.println("\nCena de los filósofos finalizada.");
    }
}
