package mx.unam.ciencias.edd.proyecto2;
/**
 * Clase para excepciones de índices de lista inválidos.
 */
public class ExcepcionIndiceInvalido extends IndexOutOfBoundsException {

    /**
     * Constructor vacío.
     */
    public ExcepcionIndiceInvalido() {
        super();
    }

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public ExcepcionIndiceInvalido(String mensaje) {
        super(mensaje);
    }
}
