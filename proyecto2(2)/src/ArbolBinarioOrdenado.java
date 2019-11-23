package proyecto2;
import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;
        
        private void hijoi(ArbolBinario<T>.Vertice v){
            while(v != null){
                pila.mete(v);
                v = v.izquierdo;
            }
        }

        /* Construye un iterador con el vértice recibido. */
        public Iterador(ArbolBinario<T>.Vertice vertice) {
            pila = new Pila<ArbolBinario<T>.Vertice>();
            if(vertice != null)
                hijoi(vertice);
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
            ArbolBinario<T>.Vertice v = pila.saca();
            hijoi(v.derecho);
            return v.elemento;

        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Sencillamente ejecuta el constructor sin
     * parámetros de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de un árbol binario. El
     * árbol binario ordenado tiene los mismos elementos que el árbol recibido,
     * pero ordenados.
     * @param arbol el árbol binario a partir del cuál creamos el
     *        árbol binario ordenado.
     */
    public ArbolBinarioOrdenado(ArbolBinario<T> arbol) {
        super();
        if(arbol.elementos == 0) return;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(arbol.raiz);

        while(!cola.esVacia()){
            Vertice v = cola.saca();
            if(v.izquierdo != null)
                cola.mete(v.izquierdo);
            if(v.derecho != null)
                cola.mete(v.derecho);
            this.agrega(v.elemento);
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(this.raiz == null) {
            this.raiz = nuevoVertice(elemento);
            this.elementos++;
            ultimoAgregado = this.raiz;
            return;
        }
        agrega(this.raiz,elemento);
    }

    private void agrega(Vertice v, T elemento){
        Vertice nuevo = nuevoVertice(elemento);
        ultimoAgregado = nuevo;
        

        if(elemento.compareTo(v.elemento) < 0){
            if(v.izquierdo == null){
                meteIzquierda(v,nuevo);
                this.elementos++;
            }
            else
                agrega(v.izquierdo, elemento);
            return;
        }

        if(elemento.compareTo(v.elemento) >=0){
            if(v.derecho == null){
                meteDerecha(v,nuevo);
                this.elementos++;
            }
            else
                agrega(v.derecho,elemento);
            return;
        } 
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(this.raiz == null) return;
        elimina(this.raiz,elemento);
    }

    private void elimina(Vertice v, T elemento){
        Vertice v1 = busca(v, elemento);
        if(v1 == null)
            return;
        if(v1.izquierdo == null && v1.derecho == null){
            if(v1.padre == null)
                this.raiz = null;
            if(esIzquierdo(v1))
                v1.padre.izquierdo = null;
            if(esDerecho(v1))
                v1.padre.derecho = null;
            v1.padre = null;
            this.elementos--;
            if(this.raiz != null)
                this.raiz.padre = null;
            return;
        }
        if(v1.izquierdo != null && v1.derecho == null){
            if(v1.padre == null)
                this.raiz = v1.izquierdo;
            if(esIzquierdo(v1))
                meteIzquierda(v1.padre,v1.izquierdo);
            if(esDerecho(v1))
                meteDerecha(v1.padre, v1.izquierdo);
            v1.padre = null;
            v1.derecho = null;
            this.elementos--;
            this.raiz.padre = null;
            return;
        }
        if(v1.izquierdo == null && v1.derecho != null){
            if(v1.padre == null)
                this.raiz = v1.derecho;
            if(esIzquierdo(v1))
                meteIzquierda(v1.padre,v1.derecho);
            if(esDerecho(v1))
                meteDerecha(v1.padre, v1.derecho);
            v1.padre = null;
            v1.derecho = null;
            this.elementos--;
            this.raiz.padre = null;
            return;
        }
        if(v1.izquierdo != null && v1.derecho != null){
            Vertice reemplazo = maximoEnSubarbol(v1.izquierdo);
            v1.elemento = reemplazo.elemento;
            elimina(v1.izquierdo, v1.elemento);
            return;
        }

    }

    private boolean esIzquierdo(Vertice v){
        if(v.padre == null)
            return false;
        return v.padre.izquierdo == v;
    }

    private boolean esDerecho(Vertice v){
        if(v.padre == null)
            return false;
        return v.padre.derecho == v;
    }

    private void meteIzquierda(Vertice p, Vertice h){
        p.izquierdo = h;
        h.padre = p;
    }
    private void meteDerecha(Vertice p, Vertice h){
        p.derecho = h;
        h.padre = p;
    }

    /**
     * Nos dice si un elemento está contenido en el árbol.
     * @param elemento el elemento que queremos ver si está en el árbol.
     * @return <code>true</code> si el elemento está contenido en el árbol,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return busca(elemento) != null;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(this.raiz, elemento);
    }

    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {
        if(vertice == null || elemento.equals(vertice.elemento))
            return vertice;
        if(elemento.compareTo(vertice.elemento) < 0)
            return busca(vertice.izquierdo, elemento);
        if(elemento.compareTo(vertice.elemento) > 0)
            return busca(vertice.derecho, elemento);
        return null;
    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {
        if(vertice == null)
            return null;
        if(vertice.derecho == null)
            return vertice;
        return maximoEnSubarbol(vertice.derecho);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador(raiz);
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        Vertice v = vertice(vertice);
        giraDerecha(v);
    }

    /* Gira el árbol a la derecha sobre el vértice recibido. */
    private void giraDerecha(Vertice vertice) {
        if(vertice.izquierdo == null)
            return;
        Vertice hijoPerdido = vertice.izquierdo.derecho;
        Vertice padreDelVertice = vertice.padre;
        Vertice reemplazo = vertice.izquierdo;

        if(esIzquierdo(vertice))
            meteIzquierda(vertice.padre, reemplazo);
        if(esDerecho(vertice))
            meteDerecha(vertice.padre, reemplazo);
        if(padreDelVertice == null){
            this.raiz = reemplazo;
            reemplazo.padre = null;
        }

        reemplazo.derecho = vertice;
        vertice.izquierdo = hijoPerdido;
        vertice.padre = reemplazo;
        if(hijoPerdido != null)
            hijoPerdido.padre = vertice;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice v = vertice(vertice);
        giraIzquierda(v);
    }

    /* Gira el árbol a la izquierda sobre el vértice recibido. */
    private void giraIzquierda(Vertice vertice) {
        if(vertice.derecho == null)
            return;
        Vertice hijoPerdido = vertice.derecho.izquierdo;
        Vertice padreDelVertice = vertice.padre;
        Vertice reemplazo = vertice.derecho;

        if(esIzquierdo(vertice))
            meteIzquierda(vertice.padre, reemplazo);
        if(esDerecho(vertice))
            meteDerecha(vertice.padre, reemplazo);
        if(padreDelVertice == null){
            this.raiz = reemplazo;
            reemplazo.padre = null;
        }

        reemplazo.izquierdo = vertice;
        vertice.derecho = hijoPerdido;
        vertice.padre = reemplazo;
        if(hijoPerdido != null)
            hijoPerdido.padre = vertice;
    }
}
