package filosofos;

/**
 * Representa a un filósofo en el problema de la "Cena de los Filósofos".
 * Cada filósofo es un hilo (Thread) que alterna entre pensar y comer,
 * solicitando y liberando palillos a través de la clase Mesa.
 */
public class Filosofo implements Runnable {

    // Identificador único del filósofo (1-5).
    private int id;
    // Referencia a la mesa para interactuar con los palillos.
    private Mesa mesa;

    // Índices de los palillos en el array de la Mesa (0-4).
    private int palilloIzqIndex;
    private int palilloDerIndex;

    // Contador de cuántas veces ha comido el filósofo.
    private int vecesComido;

    // Límite de comidas para que el hilo termine.
    private static final int MAX_COMIDAS = 3;

    // Variables auxiliares solo para mostrar el mensaje de salida exacto
    private int idPalilloIzqVisual;
    private int idPalilloDerVisual;

    /**
     * Constructor de la clase Filosofo.
     *
     * @param id          Índice del filósofo (0-4). Se transforma a 1-5 internamente.
     * @param mesa        Referencia al objeto Mesa que gestiona los palillos.
     * @param palilloIzqIndex Índice del palillo izquierdo en el array de la Mesa.
     * @param palilloDerIndex Índice del palillo derecho en el array de la Mesa.
     */
    public Filosofo(int id, Mesa mesa, int palilloIzqIndex, int palilloDerIndex) {
        this.id = id + 1;
        this.mesa = mesa;
        this.palilloIzqIndex = palilloIzqIndex;
        this.palilloDerIndex = palilloDerIndex;
        this.vecesComido = 0;

        // Lógica para calcular los IDs de los palillos (para el print final)
        this.idPalilloIzqVisual = this.id;
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
                pensar();       // Simula actividad de pensamiento
                comer();        // Intenta coger los palillos y comer
                vecesComido++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Filosofo " + id + " fue interrumpido.");
        }
    }

    /**
     * Simula la acción de pensar.
     *
     * @throws InterruptedException Si el hilo es interrumpido mientras duerme.
     */
    private void pensar() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta pensando");
        Thread.sleep((long) (Math.random() * 1000));
    }

    /**
     * Llama a la Mesa para gestionar la adquisición y liberación de los palillos.
     *
     * @throws InterruptedException Si el hilo es interrumpido mientras espera.
     */
    private void comer() throws InterruptedException {
        System.out.println("Filosofo " + id + " esta hambriento");

        // Llama al método de la Mesa para coger los palillos
        mesa.cogerPalillos(id, palilloIzqIndex, palilloDerIndex);

        // Si llega aquí, tiene ambos palillos
        System.out.println("Filosofo " + id + " esta comiendo");

        // Simula el tiempo que tarda en comer
        Thread.sleep((long) (Math.random() * 1500));

        // Llama al método de la Mesa para liberar los palillos
        soltarPalillos();
    }

    /**
     * Llama al método de la Mesa para liberar los palillos y notifica.
     */
    private void soltarPalillos() {
        mesa.soltarPalillos(palilloIzqIndex, palilloDerIndex);

        // Mensaje con formato específico solicitado
        System.out.println("Filosofo " + id + " ha terminado de comer, palillos libres: "
                + idPalilloIzqVisual + ", " + idPalilloDerVisual);
    }
}