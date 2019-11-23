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
 * <p>Las instancias de la clase Lista implementan la interfaz {@link Coleccion},
 * y por lo tanto también la interfaz {@link Iterator}, por lo que
 * el recorrerlas es muy sencillo:</p>
 *
 * <pre>
 * for (String s : l)
 * System.out.println(s);
 * </pre>
 *
 * <p>Además, se le puede pedir a una lista una instancia de
 * {@link IteradorLista} para recorrerla en ambas direcciones.</p>
 */

public class Lista<T> implements Coleccion<T>{
    
    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo{
	
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
    private class Iterador<T> implements IteradorLista<T>{
	
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
	    siguiente=lista.cabeza;
	    anterior=null;
       	}
	
        /* Existe un siguiente elemento, si siguiente no es nulo. */
        @Override public boolean hasNext() {
	    // Aquí va su código.
	    if(this.siguiente==null)
		return false;
	    else
		return true;
	    
	}
	
        /* Regresa el elemento del siguiente, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T next()throws NoSuchElementException {
	    if(siguiente!=null){
		T temporal = siguiente.elemento;
		anterior = siguiente;
		siguiente = siguiente.siguiente;
		return temporal;
	    }else{
		throw new NoSuchElementException();
	    }
	}
	/* Existe un elemento anterior, si anterior no es nulo. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
	    if(anterior==null){
		return false;
	    }else{
		return true;
	    }
        }
	
        /* Regresa el elemento del anterior, a menos que sea nulo, en cuyo caso
         * lanza la excepción NoSuchElementException. */
        @Override public T previous()throws NoSuchElementException {
            // Aquí va su código.
	    if(anterior!=null){
		T temporal = anterior.elemento;
		siguiente=anterior;
		anterior = anterior.anterior;
		return temporal;
		// TODO lo mismo va antes del return
	    }else{
		throw new NoSuchElementException();
	    }
        }
	
        /* No implementamos el método remove(); sencillamente lanzamos la
         * excepción UnsupportedOperationException. */
        @Override public void remove()throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
	
        /* Mueve el iterador al inicio de la lista; después de llamar este
         * método, y si la lista no es vacía, hasNext() regresa verdadero y
         * next() regresa el primer elemento. */
        @Override public void start() {
            // Aquí va su código.
	    if(lista!=null){
		this.anterior=null;
		this.siguiente=lista.cabeza;
	    }else{
		this.anterior=null;
		this.siguiente=null;
	    }
	}
	
	/* Mueve el iterador al final de la lista; después de llamar este
         * método, y si la lista no es vacía, hasPrevious() regresa verdadero y
         * previous() regresa el último elemento. */
        @Override public void end() {
            // Aquí va su código.
	    if(lista!=null){
		this.anterior=lista.rabo;
		this.siguiente=null;
	    }else{
		this.anterior=null;
		this.siguiente=null;
	    }
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
	return longitud;
    }
    
    /**
     * Regresa el número de elementos en la lista. El método es idéntico a
     * {@link getLongitud}.
     * @return el número de elementos en la lista.
     */
    @Override public int getElementos() {
	return longitud;
    }
    
    /**
     * Agrega un elemento al final de la lista. El método es idéntico a {@link
     * #agregaFinal}.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	Nodo n = new Nodo(elemento);
    	if(this.longitud==0){
    	    rabo = n;
    	    cabeza = n;
	}else{
	    rabo.siguiente=n;
	    n.anterior=rabo;
	    rabo=n;
	}
	longitud++;
    }
    
    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaFinal(T elemento) {
	Nodo n=new Nodo(elemento);
        if(this.longitud==0){
	    rabo=n;
	    cabeza=n;
	}else{
	    rabo.siguiente=n;
	    n.anterior=rabo;
	    rabo=n;
	}
	longitud++;
    }
    
    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y el último a la vez.
     * @param elemento el elemento a agregar.
     */
    public void agregaInicio(T elemento) {
	Nodo n=new Nodo(elemento);
	if(this.longitud==0){
	    rabo=n;
	    cabeza=rabo;
	}else{
	    cabeza.anterior=n;
	    n.siguiente=cabeza;
	    cabeza=n;
	}
	longitud++;
    }
    private Nodo buscaNodo(T elemento,Nodo n){
	Nodo pivote=n;
	while(pivote!=null){
	    if(pivote.elemento.equals(elemento))
		return pivote;
	    pivote=pivote.siguiente;
	}
	return null;
    }	
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no hace nada. Si el elemento aparece varias veces en la
     * lista, el método elimina el primero.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	Nodo pivote = buscaNodo(elemento,cabeza);
	if(pivote==null){
	    return;
        }else if(cabeza==rabo){//referencias
	    cabeza=null;
	    rabo=null;
	    longitud=0;
        }else if(pivote==rabo){
	    rabo=pivote.anterior;
	    rabo.siguiente=null;
	    longitud--;	
        }else if(pivote==cabeza){
	    cabeza=pivote.siguiente;
	    cabeza.anterior=null;
	    longitud--;
        }else{
	    pivote.anterior.siguiente=pivote.siguiente;
	    pivote.siguiente.anterior=pivote.anterior;
	    longitud--;
        }
    }
    
    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero()throws NoSuchElementException{
	Nodo primero; 
        if(longitud==0){
            throw new NoSuchElementException();
	}else if(longitud==1){
            primero = new Nodo(cabeza.elemento);
	    cabeza=null;
	    rabo=null;
	    longitud--;
	    return primero.elemento;
	}else{
	    primero = new Nodo(cabeza.elemento);
            cabeza=cabeza.siguiente;
	    cabeza.anterior=null;
	    longitud--;
	    return primero.elemento;
	}
    }
    
    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo()throws NoSuchElementException{
	Nodo ant;
	if(longitud==0){
	    throw new NoSuchElementException();
	}else if(longitud==1){
	    ant=new Nodo(rabo.elemento);
	    rabo=null;
	    cabeza=null;
	    longitud--;
	    return ant.elemento;
	}else{
	    ant=new Nodo(rabo.elemento);
	    rabo.anterior.siguiente=null;
	    rabo=rabo.anterior;
	    longitud--;
	    return ant.elemento;
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
       	if(n==null){
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
	Lista<T> reves = new Lista<T>();
       	if(this.longitud==0){
	    return reves;
	}else{
	    Nodo auxiliar=this.cabeza;
	    while(auxiliar!=null){
		reves.agregaInicio(auxiliar.elemento);
		auxiliar=auxiliar.siguiente;
	    }
	    return reves;
	}
    }
    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
	Lista<T> copy = new Lista<T>();
	if(this.longitud==0){
	    return copy;
	}else{
	    Nodo auxiliar=this.cabeza;
	    while(auxiliar!=null){
		copy.agrega(auxiliar.elemento);
		auxiliar=auxiliar.siguiente;
	    }
	    return copy;
	}
    }
    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
	rabo=null;
	cabeza=null;
	longitud=0;
    }
    
    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero()throws NoSuchElementException{
	if(this.longitud!=0){
	    return cabeza.elemento;
       	}else{
	    throw new NoSuchElementException();
       	}
    }
    
    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo()throws NoSuchElementException{
	if (this.longitud!=0) {
	    return this.rabo.elemento;
	}else{
	    throw new NoSuchElementException();
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
    public T get(int i)throws ExcepcionIndiceInvalido{
        // Aquí va su código.}
        if(i>=0 && i<this.longitud){
	    Nodo node=cabeza;
	    for(int j=0;j<i;j++){
		node=node.siguiente;
	    }			
	    return node.elemento;
        }else{
	    throw new ExcepcionIndiceInvalido();
        }
    }
    
    
    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
	if(longitud==0){
	    return -1;
	}else{
	    Nodo pivote=this.cabeza;
	    for(int i=0;i<longitud;i++){ //Dudas
		if(pivote.elemento.equals(elemento)){
		    return i;
		}else{
		    pivote=pivote.siguiente;
		}
	    }
	    return -1;
	}
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
	// Aquí va su código.
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
	if(lista.longitud!=this.longitud){
	    return false;
	}
	Nodo este = this.cabeza;
	Nodo param= lista.cabeza;	
	for(int i=0;i<this.longitud;i++){
	    if(este.elemento.equals(param.elemento)){
		este=este.siguiente;
		param=param.siguiente;
		
	    }else{
		return false;
	    }
	}
	return true;
    }
    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
       	String cadena="[";
	if(longitud==0){
	    return "[]";
	}else{
	    for(T elemen:this){
		if(elemen == rabo.elemento){
		    cadena+=elemen.toString()+"]";
		}else{
		    cadena +=elemen.toString()+", ";
		}
	    }
	    return cadena;
	}
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
    /*
     * Metodo privado
     * Regresa la lista resultante de mezclar en orden 2 listas elemento por elemento.
     */
    private static <T extends Comparable<T>> Lista<T> mezcla(Lista<T> l1,Lista<T> l2){  //El problema esta aqui en la firma del metodo.
	Lista<T> mix = new Lista<T>();
	int l= l1.longitud + l2.longitud;
	Lista<T>.Nodo _1=l1.cabeza;
	Lista<T>.Nodo _2=l2.cabeza;
	for(int i=0;i<l;i++){
	    if(_1 == null){
		mix.agrega(_2.elemento);
		_2=_2.siguiente;
	    }else if(_2==null){
		mix.agrega(_1.elemento);
		_1=_1.siguiente;
	    }else if(_1.elemento.compareTo(_2.elemento)<0){  //Aqui comparo   T < T   ??  what?
 		mix.agrega(_1.elemento);
		_1=_1.siguiente;
	    }else{
		mix.agrega(_2.elemento);
		_2=_2.siguiente;
	    }
	}
	return mix;
    }
    
    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param l la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */   /*<T extends Comparable<T>>*/ 
    public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> l) {
	if(l.longitud<2){
	    return l.copia();
	}else{
	    Lista<T> t1 = new Lista<T>();
	    Lista<T> t2 = new Lista<T>();
	    Lista<T>.Nodo n = l.cabeza;
	    for(int i=0;i<l.longitud/2;i++){
		t1.agrega(n.elemento);
		n=n.siguiente;
	    }
	    for(int j=(l.longitud/2);j<l.longitud;j++){
		t2.agrega(n.elemento);
		n=n.siguiente;
	    }
	    t1=mergeSort(t1);
	    t2=mergeSort(t2);
	    return mezcla(t1,t2); //o probablemente aqui
	}
	//Aquí va su código.
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
    public static <T extends Comparable<T>> boolean busquedaLineal(Lista<T> l, T e) {
	if(l.longitud==0){
	    return false;
	}else{
	    Lista<T>.Nodo n = l.cabeza;
	    for(int i=0;i<l.longitud;i++){
		if(n.elemento.equals(e)){
		    return true;
		}else{
		    n=n.siguiente;
		}
	    }
	    return false;
	}
    }
}
