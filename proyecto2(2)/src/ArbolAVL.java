package proyecto2;
import java.lang.Math;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho es al menos -1, y a lo más
 * 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends ArbolBinario<T>.Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento){
            super(elemento);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
            return altura==vertice.altura && super.equals(o);
        }
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeAVL nuevo= verticeAVL(ultimoAgregado);
        actualizaAltura(nuevo);
        rebalancea(padre(nuevo));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        Vertice v = vertice(busca(elemento));
        if(v==null)
            return;
        elementos--;
        VerticeAVL p = elimina(v);
        actualizaAltura(p);
        rebalancea(p);
    }

    private VerticeAVL elimina(Vertice v){
        Vertice padre = null;
        Vertice anterior = maximoEnSubarbol(v.izquierdo);
        if(anterior==null){
            padre=v.padre;
            if(v.padre==null){
                raiz=v.derecho;
                if(v.derecho!=null)
                    v.derecho.padre=null;
            }else{
                if(v.padre.izquierdo==v)
                    v.padre.izquierdo=v.derecho;
                else
                    v.padre.derecho=v.derecho;
                if(v.derecho!=null)
                    v.derecho.padre=v.padre;
            }
        }else{
            padre=anterior.padre;
            v.elemento=anterior.elemento;
            if(anterior.padre.izquierdo==anterior)
                anterior.padre.izquierdo=anterior.izquierdo;
            else
                anterior.padre.derecho=anterior.izquierdo;
            if(anterior.izquierdo!=null)
                anterior.izquierdo.padre=anterior.padre;
        }
        return padre!=null? verticeAVL(padre) : null;
    }

    private void rebalancea(VerticeAVL v){
        if(v==null)
            return;
        int balance = getAltura(izquierdo(v))-getAltura(derecho(v));
        if(balance==0){
            rebalancea(padre(v));
            return;
        }
        if(balance==-2){
            VerticeAVL d = derecho(v);
            if(getAltura(izquierdo(d))-getAltura(derecho(d))==1)
                giraDerecha(d);
            giraIzquierda(v);
        }
        if(balance==2){
            VerticeAVL i = izquierdo(v);
            if(getAltura(izquierdo(i))-getAltura(derecho(i))==-1)
                giraIzquierda(i);
            giraDerecha(v);
        }
        rebalancea(padre(v));
    }

    /**
     * Regresa la altura del vértice AVL.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    public int getAltura(VerticeArbolBinario<T> vertice) {
        if(vertice==null)
            return -1;
        return verticeAVL(vertice).altura;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada. Una vez hecho el giro, las
     * alturas de los vértices se recalculan. Este método no debe ser llamado
     * por los usuarios de la clase; puede desbalancear el árbol.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        VerticeAVL v = verticeAVL(vertice);
        super.giraDerecha(v);
        v.altura = 1 + (int)Math.max(getAltura(izquierdo(v)),(getAltura(derecho(v))));
        padre(v).altura = 1 + (int)Math.max(getAltura(izquierdo(padre(v))),(getAltura(derecho(padre(v)))));
        actualizaAltura(padre(padre(v)));
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada. Una vez hecho el giro, las
     * alturas de los vértices se recalculan. Este método no debe ser llamado
     * por los usuarios de la clase; puede desbalancear el árbol.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        VerticeAVL v = verticeAVL(vertice);
        super.giraIzquierda(v);
        v.altura = 1 + (int)Math.max(getAltura(izquierdo(v)),(getAltura(derecho(v))));
        padre(v).altura = 1 + (int)Math.max(getAltura(izquierdo(padre(v))),(getAltura(derecho(padre(v)))));
        actualizaAltura(padre(padre(v)));
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeAVL}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice AVL.
     * @return el vértice recibido visto como vértice AVL.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeAVL}.
     */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        VerticeAVL v = (VerticeAVL)vertice;
        return v;
    }

    private void actualizaAltura(VerticeAVL v){
        if(v==null)
            return;
        v.altura= 1 + (int)Math.max(getAltura(v.izquierdo),(getAltura(v.derecho)));
        actualizaAltura(padre(v));
    }

    private VerticeAVL padre(VerticeAVL v){
        if(v==null)
            return null;
        if(v.padre==null)
            return null;
        return verticeAVL(v.padre);
    }

    private VerticeAVL izquierdo(VerticeAVL v){
        if(v==null)
            return null;
        if(v.izquierdo==null)
            return null;
        return verticeAVL(v.izquierdo);
    }

    private VerticeAVL derecho(VerticeAVL v){
        if(v==null)
            return null;
        if(v.derecho==null)
            return null;
        return verticeAVL(v.derecho);
    }

    private void subir(VerticeAVL v,VerticeAVL padre){
        v.padre=padre.padre;
        if(padre.padre!=null)
            if(esDerecho(padre))
                padre.padre.derecho=v;
            else
                padre.padre.izquierdo=v;
        if(padre.padre==null)
            raiz=v;
    }

    private boolean esIzquierdo(VerticeAVL v){
        if(v==null)
            return false;
        if(v.padre==null)
            return false;
        if(v.padre.izquierdo==null)
            return false;
        if(v.padre.izquierdo==v)
            return true;
        return false;
    }

    private boolean esDerecho(VerticeAVL v){
        if(v==null)
            return false;
        if(v.padre==null)
            return false;
        if(v.padre.derecho==null)
            return false;
        if(v.padre.derecho==v)
            return true;
        return false;
    }
}
