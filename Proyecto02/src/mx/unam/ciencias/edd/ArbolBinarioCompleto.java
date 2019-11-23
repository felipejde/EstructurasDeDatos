package mx.unam.ciencias.edd.proyecto2;
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
            if(raiz!=null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            ArbolBinario<T>.Vertice v = cola.saca();
            if(v.izquierdo!=null)
                cola.mete(v.izquierdo);
            if(v.derecho!=null)
                cola.mete(v.derecho);
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
        Vertice v=nuevoVertice(elemento);
        ultimoAgregado=v;
        if(this.raiz==null)
            this.raiz=v;
        else{
            Cola<Vertice> cola=new Cola<Vertice>();
            cola.mete(raiz);
            while(!cola.esVacia()){
                Vertice aux=cola.saca();
                if(aux.izquierdo==null){
                    meteIzquierda(aux,v);
                    return;
                }
                cola.mete(aux.izquierdo);
                if(aux.derecho == null){
                    meteDerecha(aux, v);
                    return;
                }
                cola.mete(aux.derecho);
            }
        }
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
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice v = (Vertice)busca(elemento);
        if(v == null) 
            return;
        Vertice ultimo = ultimo();
        T vElem = v.elemento;

        if(v == this.raiz){
            if(this.elementos == 1){
                this.raiz = null;
                this.elementos = 0;
                return;
            }
            this.raiz.elemento = ultimo.elemento;
            ultimo.elemento = vElem;
            if(esIzquierdo(ultimo))
                ultimo.padre.izquierdo = null;
            if(esDerecho(ultimo))
                ultimo.padre.derecho = null;
            ultimo.padre = null;
            this.elementos--;
            return;
        }

        v.elemento = ultimo.elemento;
        ultimo.elemento = vElem;
        ultimo.elemento = vElem;
        if(esIzquierdo(ultimo))
            ultimo.padre.izquierdo = null;
        if(esDerecho(ultimo))
            ultimo.padre.derecho = null;
        ultimo.padre = null;
        this.elementos--;
    }  

    private Vertice ultimo(){
        if(this.elementos == 1) 
            return this.raiz;
        
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(this.raiz);
        Vertice aux = this.raiz;

        while(!cola.esVacia()){
            aux = cola.saca();
            if(aux.izquierdo != null)
                cola.mete(aux.izquierdo);
            if(aux.derecho != null)
                cola.mete(aux.derecho);
        }

        return aux;
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
