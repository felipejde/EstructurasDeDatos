package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     */
	@Override public void mete(T elemento) {
        Nodo primero = new Nodo(elemento);
        if(elementos==0){
            cabeza=rabo=primero;
            elementos++;
        }else{
            primero.siguiente=cabeza;
            cabeza=primero;
            this.elementos++;
        }
    }
}
