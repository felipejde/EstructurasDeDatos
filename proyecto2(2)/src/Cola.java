package proyecto2;
/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        if(elementos==0){
            cabeza=rabo=nuevo;
            elementos++;
        }else{
            rabo.siguiente=nuevo;
            rabo=nuevo;
            this.elementos++;
        }
    }
}
