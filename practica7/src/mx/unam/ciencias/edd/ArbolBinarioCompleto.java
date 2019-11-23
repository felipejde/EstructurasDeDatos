package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador(ArbolBinario<T>.Vertice raiz) {
            cola = new Cola<ArbolBinario<T>.Vertice>();
            if(raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            ArbolBinario<T>.Vertice v = cola.saca();
            if(v.izquierdo != null) cola.mete(v.izquierdo);
            if(v.derecho != null) cola.mete(v.derecho);
            return v.elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     */
    @Override public void agrega(T elemento) {
        this.elementos++;
        Vertice nuevo = nuevoVertice(elemento);
        ultimoAgregado = nuevo;
        if(this.raiz == null)
            this.raiz = nuevo;
        else{
            Cola<Vertice> cola = new Cola<Vertice>();
            cola.mete(raiz);
            while(!cola.esVacia()){
                Vertice actual = cola.saca();
                if(actual.izquierdo ==null){
                    insertaIzq(actual, nuevo);
                    return;
                }
                cola.mete(actual.izquierdo);
                if(actual.derecho == null){
                    insertaDer(actual, nuevo);
                    return;
                }
                cola.mete(actual.derecho);
            }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */

    private void insertaIzq(Vertice p, Vertice h){
        p.izquierdo = h;
        h.padre = p;
    }
    private void insertaDer(Vertice p, Vertice h){
        p.derecho = h;
        h.padre = p;
    }

    @Override public void elimina(T elemento) {
        Vertice r = (Vertice)busca(elemento);
        if(r == null) 
            return;
        Vertice reemplazo = buscaUltimo();
        T rElem = r.elemento;

        if(r == this.raiz){
            if(this.elementos == 1){
                this.raiz = null;
                this.elementos = 0;
                return;
            }
            this.raiz.elemento = reemplazo.elemento;
            reemplazo.elemento = rElem;
            if(esIzquierdo(reemplazo))
                reemplazo.padre.izquierdo = null;
            if(esDerecho(reemplazo))
                reemplazo.padre.derecho = null;
            reemplazo.padre = null;
            this.elementos--;
            return;
        }

        r.elemento = reemplazo.elemento;
        reemplazo.elemento = rElem;
        reemplazo.elemento = rElem;
        if(esIzquierdo(reemplazo))
            reemplazo.padre.izquierdo = null;
        if(esDerecho(reemplazo))
            reemplazo.padre.derecho = null;
        reemplazo.padre = null;
        this.elementos--;
    }

    private Vertice buscaUltimo(){
        if(this.elementos == 1) 
            return this.raiz;
        
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        Vertice actual = this.raiz;

        while(!cola.esVacia()){
            actual = cola.saca();
            if(actual.izquierdo != null)
                cola.mete(actual.izquierdo);
            if(actual.derecho != null)
                cola.mete(actual.derecho);
        }

        return actual;
    }

    private boolean esIzquierdo(Vertice v){
        if(v.padre == null) return false;
        return v.padre.izquierdo == v;
    }

    private boolean esDerecho(Vertice v){
        if(v.padre == null) return false;
        return v.padre.derecho == v;
    }

    /**
     * Nos dice si un elemento está en el árbol binario completo.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(this.raiz == null) return false;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        while(!cola.esVacia()){
            Vertice v = cola.saca();
            if(v.elemento == elemento) return true;
            if(v.izquierdo != null) cola.mete(v.izquierdo);
            if(v.derecho != null) cola.mete(v.derecho);
        }
        return false;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador(raiz);
    }
}
