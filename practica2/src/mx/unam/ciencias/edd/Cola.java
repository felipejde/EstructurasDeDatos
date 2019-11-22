	package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     */
	@Override public void mete(T elemento) {
        Nodo primero = new Nodo(elemento);
        if(elementos==0){
            cabeza=rabo=primero;
            elementos++;
        }else{
            rabo.siguiente=primero;
            rabo=primero;
            this.elementos++;
        }
    }
}
