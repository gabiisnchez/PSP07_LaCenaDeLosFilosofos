package filosofos;

/**
 * Clase principal que solo define la constante y lanza la simulación
 * delegando la ejecución completa a la clase Mesa.
 */
public class CenaFilosofos {

    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) {

        // 1. Instancia la clase Mesa con el número de filósofos.
        Mesa mesa = new Mesa(NUM_FILOSOFOS);

        // 2. Ejecuta toda la simulación con una sola llamada.
        mesa.ejecutarSimulacion();
    }
}