package filosofos;

import java.util.concurrent.Semaphore;

/**
 * Representa la mesa circular que gestiona los palillos y la simulación completa.
 * Centraliza la inicialización de recursos, la lógica para coger/soltar palillos
 * y la ejecución de la Cena de los Filósofos.
 */
public class Mesa {

    private final Semaphore[] palillos;
    private final int numFilosofos;

    /**
     * Constructor que inicializa los semáforos de los palillos.
     *
     * @param numFilosofos El número de filósofos/palillos en la mesa.
     */
    public Mesa(int numFilosofos) {
        this.numFilosofos = numFilosofos;
        this.palillos = new Semaphore[numFilosofos];

        // Inicializa cada palillo como un semáforo binario (permiso = 1)
        for (int i = 0; i < numFilosofos; i++) {
            this.palillos[i] = new Semaphore(1);
        }
    }

    /**
     * Ejecuta toda la simulación de la Cena de los Filósofos:
     * 1. Crea y lanza todos los hilos Filosofo.
     * 2. Espera a que todos terminen (join).
     */
    public void ejecutarSimulacion() {
        Thread[] filosofos = new Thread[numFilosofos];

        System.out.println("\nCena de los filosofos iniciada.\n");

        // Creación e inicio de los hilos
        for (int i = 0; i < numFilosofos; i++) {

            // Obtenemos los índices de los palillos.
            int[] indices = getIndicesPalillos(i);
            int palilloIzqIndex = indices[0];
            int palilloDerIndex = indices[1];

            // Creamos el objeto Runnable (Filosofo)
            Filosofo filosofo = new Filosofo(i, this, palilloIzqIndex, palilloDerIndex);

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

    /**
     * Calcula los índices de los palillos izquierdo y derecho para un filósofo.
     *
     * @param filosofoIndex Índice del filósofo (0 a NUM_FILOSOFOS - 1).
     * @return Un array de dos enteros: [indicePalilloIzquierdo, indicePalilloDerecho].
     */
    public int[] getIndicesPalillos(int filosofoIndex) {
        int palilloIzqIndex = filosofoIndex;
        int palilloDerIndex = (filosofoIndex + 1) % numFilosofos;
        return new int[]{palilloIzqIndex, palilloDerIndex};
    }

    /**
     * Lógica para coger los palillos con estrategia Deadlock (pares/impares).
     *
     * @param idFilosofo ID del filósofo (1-5).
     * @param palilloIzqIndex Índice del palillo izquierdo (0-4).
     * @param palilloDerIndex Índice del palillo derecho (0-4).
     * @throws InterruptedException Si el hilo es interrumpido mientras espera.
     */
    public void cogerPalillos(int idFilosofo, int palilloIzqIndex, int palilloDerIndex) throws InterruptedException {

        if (idFilosofo % 2 == 0) { // PAR: Izquierda -> Derecha
            palillos[palilloIzqIndex].acquire();
            palillos[palilloDerIndex].acquire();
        } else { // IMPAR: Derecha -> Izquierda
            palillos[palilloDerIndex].acquire();
            palillos[palilloIzqIndex].acquire();
        }
    }

    /**
     * Libera los semáforos de ambos palillos.
     */
    public void soltarPalillos(int palilloIzqIndex, int palilloDerIndex) {
        palillos[palilloIzqIndex].release();
        palillos[palilloDerIndex].release();
    }
}