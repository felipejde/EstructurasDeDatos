package mx.unam.ciencias.edd.proyecto2;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las instancias de la clase Lista implementan la interfaz {@link
 * Coleccion}, y por lo tanto también la interfaz {@link Iterator}, por lo que
 * el recorrerlas es muy sencillo:</p>
 *
<pre>
    for (String s : l)
        System.out.println(s);
</pre>
 *
 * <p>Además, se le puede pedir a una lista una instancia de {@link
 * IteradorLista} para recorrerla en ambas direcciones.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {

        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con el elemento especificado. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador<T> implements IteradorLista<T> {

        /* La lista a iterar. */
        Lista<T> lista;
        /* Elemento anterior. */
        private Lista<T>.Nodo anterior;
        /* Elemento siguiente. */
        private Lista<T>.Nodo siguiente;

        /* El constructor recibe una lista para inicializar su siguiente con la
         * cabeza. */
        public Iterador(Lista<T> lista) {
            this.lista = lista;
            siguiente = lista.cabeza;
        }

        /* Existe un siguiente elemento, si siguiente no es nulo. */
        @Override public boolean hasNext() {
            if(this.siguiente==null){
                return false;
            }else{
                return true;
            }
        }

        /* Regresa el elemento del siguiente, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T next() {
            if(this.hasNext()){
                T elem = siguiente.elemento;
                anterior = siguiente;
                siguiente = siguiente.siguiente;
                return elem;
            }else{
                throw new NoSuchElementException();
            }
        }

        /* Existe un elemento anterior, si anterior no es nulo. */
        @Override public boolean hasPrevious() {
            if(this.anterior==null){
                return false;
            }else{
                return true;
            }
        }

        /* Regresa el elemento del anterior, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T previous() {
            if(this.hasPrevious()){
                T elem = anterior.elemento;
                siguiente = anterior;
                anterior = anterior.anterior;
                return elem;
            }else{
                throw new NoSuchElementException();
            }
        }

        /* No implementamos el método remove(); sencillamente lanzamos la
         * excepción UnsupportedOperationException. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }

        /* Mueve el iterador al inicio de la lista; después de llamar este
         * método, y si la lista no es vacía, hasNext() regresa verdadero y
         * next() regresa el primer elemento. */
        @Override public void start() {
            this.anterior=null;
            this.siguiente=this.lista.cabeza;
        }

        /* Mueve el iterador al final de la lista; después de llamar este
         * método, y si la lista no es vacía, hasPrevious() regresa verdadero y
         * previous() regresa el último elemento. */
        @Override public void end() {
            this.siguiente=null;
            this.anterior=this.lista.rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return this.longitud;
    }

    /**
     * Regresa el número de elementos en la lista. El método es idéntico a
     * {@link getLongitud}.
     * @return el número de elementos en la lista.
     */
    @Override public int getElementos() {
        return this.longitud;
    }

    /**
     * Agrega un elemento al final de la lista. El método es idéntico a {@link
     * #agregaFinal}.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(longitud==0){
            this.rabo= new Nodo(elemento);
            this.cabeza=this.rabo;
            longitud++;
        }else{
            Nodo rabo1=this.rabo;
            this.rabo= new Nodo(elemento);
            this.rabo.anterior=rabo1;
            this.longitud++;
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        if(longitud==0){
            cabeza=rabo=nuevo;
            longitud++;
        }else{
            rabo.siguiente=nuevo;
            rabo.siguiente.anterior=rabo;
            rabo=rabo.siguiente;
            this.longitud++;
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        if(longitud==0){
            cabeza=rabo=nuevo;
            longitud++;
        }else{
            cabeza.anterior=nuevo;
            cabeza.anterior.siguiente=cabeza;
            cabeza=cabeza.anterior;
            this.longitud++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no hace nada. Si el elemento aparece varias veces en la
     * lista, el método elimina el primero.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo n = buscaNodo(elemento,this.cabeza);
        if(n==null){
            return;
        }
        if(cabeza==rabo){
            cabeza=null;
            rabo=null;
            longitud--;
        }else{
            if(n==rabo){
                rabo=n.anterior;
                rabo.siguiente=null;
                longitud--;
            }else{
                if(n==cabeza){
                    cabeza=n.siguiente;
                    cabeza.anterior=null;
                    longitud--;
                }else{
                    n.siguiente.anterior=n.anterior;
                    n.anterior.siguiente=n.siguiente;
                    longitud--;
                }
            }
        }
    }
    /**
    *Busca un nodo con el elemento parametro
    *@param elemento el elemento a buscar
    *@param n Nodo donde comienza la busqueda
    */
    private Nodo buscaNodo(T elemento, Nodo n){
        if(n==null){
            return null;
        }
        if (n.elemento.equals(elemento)){
            return n;
        }else{
            return buscaNodo(elemento,n.siguiente);
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(cabeza==null){
            throw new NoSuchElementException("La lista es vacia");
        }
        T elemento = this.cabeza.elemento;
        this.elimina(elemento);
        return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(rabo==null){
            throw new NoSuchElementException("La lista es vacia");
        }else{
            if(cabeza==rabo){
                T elemento = this.rabo.elemento;
                rabo = cabeza = null;
                longitud--;
                return elemento;
            }else{
                T elemento = this.rabo.elemento;
                rabo = rabo.anterior;
                this.rabo.siguiente = null;
                longitud--;
                return elemento;
            }
        }
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo n = buscaNodo(elemento,this.cabeza);
        if (n==null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa de la que manda llamar el
     *         método.
     */
    public Lista<T> reversa() {
        Lista<T> l = new Lista<T>();
        if(this.longitud!=0){
            IteradorLista<T> it = this.iteradorLista();
            while(it.hasNext()){
                l.agregaInicio(it.next());
            }
         }
         return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
         Lista<T> l= new Lista<T>();
        copia(l,this.cabeza);
        return l;
    }

    private void copia(Lista<T> l, Nodo n){
        if (n ==null){
            return;
        }
        l.agregaFinal(n.elemento);
        copia(l, n.siguiente);
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
        this.cabeza=null;
        this.rabo=this.cabeza;
        this.longitud=0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(this.cabeza==null){
            throw new NoSuchElementException();
        }else{
            return this.cabeza.elemento;
        }
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
       if(this.rabo==null){
            throw new NoSuchElementException();
        }else{
            return this.rabo.elemento;
        }
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si <em>i</em> es mayor
     *         o igual que cero y menor que el número de elementos en la lista.
     * @throws ExcepcionIndiceInvalido si el índice recibido es menor que cero,
     *         o mayor que el número de elementos en la lista menos uno.
     */
    public T get(int i) {
        if (i < 0){
            throw new ExcepcionIndiceInvalido("Indice de la lista invalido");
        }
        return get(this.cabeza, i, 0);
    }

    private T get(Nodo n, int i, int j){
        if(n == null){
            throw new ExcepcionIndiceInvalido("Indice de la lista invalido");
        }
        if(i == j){
            return n.elemento;
        }
        return get(n.siguiente, i, ++j);
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        return indiceDe(elemento,cabeza,0);    
    }

    private int indiceDe(T elem, Nodo n, int i){
        if (n==null){
            return -1;
        }
        if(n.elemento.equals(elem)){
            return i;
        }
        return indiceDe(elem,n.siguiente, i+1);
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        return equals(this.cabeza,lista.cabeza);
    }

    /**
    *Metodo equals para dos nodos
    *@param n Nodo uno que se comparara
    *@param m nodo dos que se comparara
    */
    private boolean equals(Nodo n,Nodo m){
        if(n==null&&m==null){
            return true;
        }
        if(n==null||m==null){
            return false;
        }
        if(n.elemento.equals(m.elemento)){
            return equals(n.siguiente,m.siguiente);
        }
        return false;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (cabeza == null){
            return "[]";
        }
        return "[" + cabeza.elemento.toString() + toString(cabeza.siguiente);
    }

    private String toString(Nodo n){
        if(n == null){
            return "]";
        }
        return ", " + n.elemento.toString() + toString(n.siguiente);
    }

    /**
     * Regresa un iterador para recorrer la lista.
     * @return un iterador para recorrer la lista.
     */
    @Override public Iterator<T> iterator() {
        return iteradorLista();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador<T>(this);
    }

    public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> l){
        if(l.getLongitud() == 0 || l.getLongitud() == 1){
            return l.copia();
        }
        Lista<T> li = new Lista<T>();
        Lista<T> ld = new Lista<T>();
        Lista<T>.Nodo it = l.cabeza;
        for(int i=0;i<l.getLongitud()/2;i++){
            li.agregaFinal(it.elemento);
            it=it.siguiente;
        }
        for(int i=l.getLongitud()/2;i<l.getLongitud();i++){
            ld.agregaFinal(it.elemento);
            it=it.siguiente;
        }
        li = mergeSort(li);
        ld = mergeSort(ld);
        return mezcla(li,ld);
    }

    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> li, Lista<T> ld){
        Lista<T> l = new Lista<T>();
        Lista<T>.Nodo it = li.cabeza;
        Lista<T>.Nodo dt = ld.cabeza;
        while (it != null && dt != null){
            if(it.elemento.compareTo(dt.elemento) < 0){
               l.agregaFinal(it.elemento);
                it = it.siguiente;
            }else{
                l.agregaFinal(dt.elemento);
                dt = dt.siguiente;
            }
        }  
        while (it != null){
            l.agregaFinal(it.elemento);
            it = it.siguiente;
        }
        while(dt != null){
            l.agregaFinal(dt.elemento);
            dt = dt.siguiente;
        }
        return l;
    }

    public static <T extends Comparable<T>> boolean busquedaLineal(Lista<T> l, T e){
        Lista<T>.Nodo it = l.cabeza;
        while(!(it==null)){
            if(it.elemento.compareTo(e)!=0){
                it=it.siguiente;
            }else{
                return true;
            }
        }
        return false;
    }
}
