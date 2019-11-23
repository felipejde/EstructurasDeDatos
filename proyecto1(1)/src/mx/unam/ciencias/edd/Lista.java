
package mx.unam.ciencias.edd;

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

  /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
 public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> l){
        if(l.getLongitud() == 0 || l.getLongitud() == 1){
            return l.copia();
        }
        
        Lista<T> izq = new Lista<T>();
        Lista<T> der = new Lista<T>();
        Lista<T>.Nodo it = l.cabeza;
        
        for(int i = 0; i < l.getLongitud()/2; i++){
            
            izq.agregaFinal(it.elemento);
            it = it.siguiente;
        }

        for(int i = l.getLongitud()/2; i < l.getLongitud(); i++){
            der.agregaFinal(it.elemento);
            it = it.siguiente;}
        
        izq = mergeSort(izq);
        der = mergeSort(der);
        
        return mezcla(izq,der);}

    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> izq, Lista<T> der){
        Lista<T> l = new Lista<T>();
        Lista<T>.Nodo it = izq.cabeza;
        Lista<T>.Nodo dt = der.cabeza;
        
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

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista donde se buscará.
     * @param e el elemento a buscar.
     * @return <tt>true</tt> si e está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> l, T e) {
  return (l.buscaNodo(e)!= null);
}

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
    public class Iterador<T> implements IteradorLista<T> {

        /* La lista a iterar. */
        Lista<T> lista;
        /* Elemento anterior. */
        private Lista<T>.Nodo anterior;
        /* Elemento siguiente. */
        private Lista<T>.Nodo siguiente;

        /* El constructor recibe una lista para inicializar su siguiente con la
         * cabeza. */
        public Iterador(Lista<T> lista) {
	    this.lista=lista;
	    this.siguiente=lista.cabeza;
        }

        /* Existe un siguiente elemento, si siguiente no es nulo. */
        @Override public boolean hasNext() {
            return(this.siguiente!=null);
        }

        /* Regresa el elemento del siguiente, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T next() {
	    if(this.hasNext()){
		T regreso=siguiente.elemento;
		this.anterior=this.siguiente;
		this.siguiente=this.siguiente.siguiente;     
		return regreso;}else{
		throw new NoSuchElementException();}
        }

        /* Existe un elemento anterior, si anterior no es nulo. */
        @Override public boolean hasPrevious() {
            return(this.anterior!=null);
        }

        /* Regresa el elemento del anterior, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T previous() {
	    if(this.hasPrevious()){
		T regreso=anterior.elemento;
		this.siguiente=this.anterior;
		this.anterior=this.anterior.anterior;		
		return regreso;}else{
		throw new NoSuchElementException();}

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
	    this.siguiente=this.lista.cabeza;
	    this.anterior=null;
        }

        /* Mueve el iterador al final de la lista; después de llamar este
         * método, y si la lista no es vacía, hasPrevious() regresa verdadero y
         * previous() regresa el último elemento. */
        @Override public void end() {
	    this.anterior=this.lista.rabo;
	    this.siguiente=null;
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
     * {@link #getLongitud}.
     * @return el número de elementos en la lista.
     */
    @Override public int getElementos() {
	return this.longitud;
    }

    /**
     * Nos dice si el conjunto de elementos en la colección es vacío.
     * @return <code>true</code> si el conjunto de elementos en la lista es
     *         vacío, <code>false</code> en otro caso.
     */
    public boolean esVacio() {
	return(this.longitud==0);
    }

    /**
     * Agrega un elemento al final de la lista. El método es idéntico a {@link
     * #agregaFinal}.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	if(this.esVacio()){
        this.rabo=new Nodo (elemento);
        this.cabeza=rabo;
	}else{
        Nodo nuevo=new Nodo (elemento);
        nuevo.anterior=rabo;
        nuevo.anterior.siguiente=nuevo;
		rabo=nuevo;}   
        this.longitud++;   }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) {
    if(this.esVacio()){
        this.rabo=new Nodo (elemento);
        this.cabeza=rabo;
    }else{
        Nodo nuevo=new Nodo (elemento);
        nuevo.anterior=rabo;
        nuevo.anterior.siguiente=nuevo;
        rabo=nuevo;}  
        this.longitud++;    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) {
	if(this.esVacio()){
	    this.cabeza=new Nodo (elemento);
	    this.rabo=cabeza;
	}else{
		Nodo nuevo=new Nodo (elemento);
		nuevo.siguiente=cabeza;
		nuevo.siguiente.anterior=nuevo;
		cabeza=nuevo;  } 
        this.longitud++; }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no hace nada. Si el elemento aparece varias veces en la
     * lista, el método elimina el primero.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if (this.contiene(elemento)) {
            if (this.getLongitud()==1) {
                this.cabeza=null;
                this.rabo=null;
                this.longitud--;
                return ;}       
             if (cabeza.elemento==elemento) {
                cabeza=cabeza.siguiente;
                cabeza.anterior=null;
                this.longitud--;
                return ;}
            if (rabo.elemento==elemento) {
                        rabo=rabo.anterior;
                        rabo.siguiente=null;
                        this.longitud--;
                        return;}
                        Nodo encontrado = this.buscaNodo(elemento);                                      
                            encontrado.siguiente.anterior=encontrado.anterior;
                            encontrado.anterior.siguiente=encontrado.siguiente;
                            this.longitud--; } }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
       if (this.esVacio()) {
           throw  new NoSuchElementException();
       }else{
       T regreso=cabeza.elemento;
        if (this.getLongitud()==1) {
            this.cabeza=null;
            this.rabo=null;
            this.longitud--;}else{
                cabeza=cabeza.siguiente;
                cabeza.anterior=null;
                this.longitud--;}
                return regreso;}}

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
       if (this.esVacio()) {
           throw  new NoSuchElementException();
       }else{
       T regreso=rabo.elemento;
        if (this.getLongitud()==1) {
            this.rabo=null;
            this.cabeza=null;
            this.longitud--;}else{
                rabo=rabo.anterior;
                rabo.siguiente=null;
                this.longitud--;}
                return regreso; }   }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {	
	return (this.buscaNodo(elemento)!=null);
    }

    private Nodo buscaNodo(T elemento){
	Nodo regreso=cabeza;
	while(regreso!=null){
	    if(regreso.elemento.equals(elemento)){return regreso;}
	    regreso=regreso.siguiente;}
	return null; }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa de la que manda llamar el
     *         método.
     */
    public Lista<T> reversa() {
    IteradorLista<T> it=this.iteradorLista();
    Lista<T> regreso=new Lista<T>();     
    it.end();
    while(it.hasPrevious()){
    regreso.agrega(it.previous());} 
    return regreso;}

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
    Lista<T> regreso=new Lista<T>();     
    IteradorLista<T> it=this.iteradorLista();
    while(it.hasNext()){
        regreso.agrega(it.next());}
    return regreso;}
    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
    this.cabeza=null;
    this.rabo=null; 
    this.longitud=0;       // Aquí va su código.
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
    if (this.esVacio()) {
    throw new NoSuchElementException("lista vacía");
        }else{
        return this.cabeza.elemento;    }}

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
    if (this.esVacio()) {
    throw new NoSuchElementException("lista vacía");
        }else{

    return this.rabo.elemento;    }}

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, si <em>i</em> es mayor
     *         o igual que cero y menor que el número de elementos en la lista.
     * @throws ExcepcionIndiceInvalido si el índice recibido es menor que cero,
     *         o mayor que el número de elementos en la lista menos uno.
     */
    public T get(int i) {
    if (i<0 || i>this.getElementos()-1) {
    throw new ExcepcionIndiceInvalido("Indice invalido"); 
        }else{
        int contador=0;
        T regreso=null;
        IteradorLista<T> it=this.iteradorLista();
            while(it.hasNext() && (contador<i+1 )){
                regreso=it.next();
                contador++;}
                return regreso; }}

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
    if (!this.contiene(elemento)) {
    return -1;}else{
        boolean encontro=false;
        int contador=0;
        IteradorLista<T> it=this.iteradorLista();
            while(it.hasNext() && encontro==false){
                if (it.next().equals(elemento)) {
                    encontro=true;
                }else{contador++;}}
                return contador;}
    }

    

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        IteradorLista<T> it1=this.iteradorLista();
        IteradorLista<T> it2=lista.iteradorLista();
        boolean valor=true;
        if (this.getLongitud()!=lista.getLongitud()) {
        return false; }else{
        while(it1.hasNext() && valor==true){
            if (it1.next().equals(it2.next())) {
            }else{valor=false;} }  }
                return valor;}

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
    if (n == null){
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
}
