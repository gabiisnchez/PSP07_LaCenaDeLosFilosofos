package filosofos;

import java.util.concurrent.Semaphore;

public class Filosofo implements Runnable{
    private int id;
    private Semaphore palilloIzquierdo;
    private Semaphore palilloDerecho;
    private int vecesComido;
    private static final int MAX_COMIDAS = 3;

    /**
     * Constructor del filósofo
     * @param id Identificador único del filósofo
     * @param palilloIzq Semáforo que representa el palillo izquierdo
     * @param palilloDer Semáforo que representa el palillo derecho
     */
    public Filosofo(int id, Semaphore palilloIzq, Semaphore palilloDer) {
        this.id = id + 1;
        this.palilloIzquierdo = palilloIzq;
        this.palilloDerecho = palilloDer;
        this.vecesComido = 0;
    }

    /**
     * Metodo principal que ejecuta el hilo del filósofo
     */
    @Override
    public void run() {
        try {
            while (vecesComido < MAX_COMIDAS) {
                pensar();
                comer();
                vecesComido++;
            }
            System.out.println("Filosofo " + id + " ha comido " + vecesComido + " veces.");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Filósofo " + id + " ha sido interrumpido.");
        }
    }

    /**
     * Simula al filósofo pensando
     */
    private void pensar() throws InterruptedException {
        System.out.println("Filosofo " + id + " está pensando.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    /**
     * Proceso de comer: tomar palillos, comer y soltar palillos
     */
    private void comer() throws InterruptedException{
        System.out.println("Filosofo " + id + " tiene hambre e intenta coger palillos.");

        if ((id - 1) % 2 == 0) {
            tomarPalillos(palilloIzquierdo, palilloDerecho, "izquierdo", "derecho");
        } else {
            tomarPalillos(palilloDerecho, palilloIzquierdo, "derecho", "izquierdo");
        }

        System.out.println("Filosofo " + id + " está comiendo.");
        Thread.sleep((long) (Math.random() * 1500));

        soltarPalillos();
    }

    /**
     * Toma los palillos en el orden especificado
     */
    private void tomarPalillos(Semaphore primero, Semaphore segundo,
                               String nombrePrimero, String nombreSegundo) throws InterruptedException {
        primero.acquire();
        System.out.println("Filosofo " + id + " ha cogido el palillo " + nombrePrimero + ".");

        segundo.acquire();
        System.out.println("Filosofo " + id + " ha cogido el palillo " + nombreSegundo + ".");
    }

    /**
     * Suelta los palillos
     */
    private void soltarPalillos() {
        palilloIzquierdo.release();
        palilloDerecho.release();
        System.out.println("Filosofo " + id + " ha soltado los palillos y terminó de comer.");
    }

}
