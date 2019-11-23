package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las operaciones de
 * inserción, eliminación y búsqueda pueden realizarse en <i>O</i>(log
 * <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends ArbolBinario<T>.Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super( elemento );
            color = Color.NINGUNO;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = (VerticeRojinegro)o;
            return color == vertice.color && super.equals(o);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice
     *                rojinegro.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos la altura.
     * @return la altura del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = verticeRojinegro(vertice);
        return v.color;
    }

    private void pintarNegro( VerticeRojinegro vertice ){
        if( vertice != null )
            vertice.color=Color.NEGRO;
    }

    private void pintarRojo( VerticeRojinegro vertice ){
        if( vertice != null )
            vertice.color=Color.ROJO;
    }

    private boolean esDerecho( VerticeRojinegro vertice ){
        if( vertice == null )
            return false;
        if( vertice.padre == null )
            return false;
        if( vertice.padre.derecho == null )
            return false;
        if( vertice.padre.derecho == vertice )
            return true;
        return false;
    }

    private boolean esIzquierdo( VerticeRojinegro vertice ){
        if( vertice == null )
            return false;
        if( vertice.padre == null )
            return false;
        if( vertice.padre.izquierdo == null )
            return false;
        if( vertice.padre.izquierdo == vertice )
            return true;
        return false;
    }

        
    private VerticeRojinegro abuelo( VerticeRojinegro vertice ){
        if( vertice.padre == null )
            return null;
        if( vertice.padre.padre == null )
            return null;
        return verticeRojinegro( vertice.padre.padre );
    }
    
    private VerticeRojinegro hermano( VerticeRojinegro vertice ){
        if( vertice.padre == null )
            return null;
        if( esIzquierdo( vertice ) )
            return verticeRojinegro( vertice.padre.derecho );
        return verticeRojinegro( vertice.padre.izquierdo );
    }

    private VerticeRojinegro tio( VerticeRojinegro vertice ){
        if( vertice.padre == null )
            return null;
        return hermano( padre( vertice ) );
    }

    private boolean esNegro( VerticeRojinegro vertice ){
        if( vertice == null )
            return true;
        if( vertice.color.equals( Color.NEGRO ) )
            return true;
        return false;
    }

    private boolean esRojo( VerticeRojinegro vertice ){
        if( vertice == null )
            return false;
        if( vertice.color.equals( Color.ROJO ) )
            return true;
        return false;
    }

    private void subir(VerticeRojinegro vertice,VerticeRojinegro padre){
        vertice.padre = padre.padre;
        if( padre.padre != null )
            if( esDerecho( padre ) )
                padre.padre.derecho = vertice;
            else
                padre.padre.izquierdo=vertice;
        if( padre.padre == null )
            raiz = vertice;
    }

    private VerticeRojinegro padre(VerticeRojinegro vertice){
        if( vertice == null )
            return null;
        if( vertice.padre == null )
            return null;
        return verticeRojinegro( vertice.padre );
    }

    private VerticeRojinegro derecho(VerticeRojinegro vertice){
        if( vertice == null )
            return null;
        if( vertice.derecho == null )
            return null;
        return verticeRojinegro( vertice.derecho );
    }
    
    private VerticeRojinegro izquierdo(VerticeRojinegro vertice){
        if( vertice == null )
            return null;
        if( vertice.izquierdo == null )
            return null;
        return verticeRojinegro(vertice.izquierdo);
    }
    
    private void rebalanceaAgrega( VerticeRojinegro v ){
        if( padre( v ) == null){
            pintarNegro( v );
            return;
        }
        if( esNegro( padre( v ) ) )
            return;
        if( esRojo( tio( v ) ) ){
            pintarNegro( padre( v ) );
            pintarNegro( tio( v ) );
            pintarRojo( abuelo( v ) );
            rebalanceaAgrega( abuelo( v ) );
            return;
        }
        if( estanCruzados( v, padre( v ) ) ){
            if( esIzquierdo( v ) ){
                giraDerecha( padre( v ) );
                v = derecho(v);
            }
	    else{
                giraIzquierda( padre( v ) );
                v = izquierdo( v );
            }
        }
        pintarNegro( padre ( v ) );
        pintarRojo( abuelo( v ) );
        if( esIzquierdo( v ) )
            giraDerecha( abuelo( v ) );
        if( esDerecho( v ) )
            giraIzquierda( abuelo( v ) );
    }
    
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega( elemento );
        pintarRojo( verticeRojinegro( ultimoAgregado ) );
        rebalanceaAgrega( verticeRojinegro( ultimoAgregado ) );
    }

    private void rebalanceaElimina( VerticeRojinegro v ){
        if( padre( v ) == null )
            return;
	if( esRojo( hermano( v ) ) ){
            pintarRojo( padre(v ) );
            pintarNegro( hermano( v ) );
            if( esIzquierdo( v ) )
                giraIzquierda( padre( v ) );
            else
                giraDerecha( padre( v ) );
        }
        if( esNegro( padre( v ) ) && esNegro( hermano( v ) ) &&
            esNegro( izquierdo( hermano( v ) ) ) && esNegro( derecho( hermano( v ) ) ) ){
	    pintarRojo( hermano( v ) );
	    rebalanceaElimina( padre( v ) );
	    return;
        }
        if( esRojo( padre( v ) ) && esNegro( hermano( v ) )
            && esNegro( izquierdo( hermano( v ) ) ) && esNegro( derecho( hermano( v ) ) ) ){
	    pintarNegro( padre( v ) );
	    pintarRojo( hermano( v ) );
	    return;
        }
        if( esIzquierdo( v ) && esNegro( derecho( hermano( v ) ) ) &&
            esRojo(izquierdo( hermano( v ) ) ) ){
	    pintarRojo( hermano( v ) );
	    pintarNegro( izquierdo( hermano( v ) ) );
	    giraDerecha( hermano( v ) );
	}
        if( esDerecho( v ) && esNegro( izquierdo( hermano( v ) ) ) &&
            esRojo( derecho( hermano( v ) ) ) ){
	    pintarRojo( hermano( v ) );
	    pintarNegro( derecho( hermano( v ) ) );
	    giraIzquierda( hermano( v ) ); 
        }
        if( esNegro( padre( v ) ) )
            pintarNegro( hermano( v ) );
        else
            pintarRojo( hermano( v ) );
        pintarNegro( padre( v ) );
        if( esIzquierdo( v ) ){
            pintarNegro( derecho( hermano( v ) ) );
            giraIzquierda( padre( v ) );
        }
	else{
            pintarNegro( izquierdo( hermano( v ) ) );
            giraDerecha( padre( v ) );
        }
    }

    private boolean estanCruzados( VerticeRojinegro vertice, VerticeRojinegro v1 ){
        if( esIzquierdo( vertice )&&esDerecho( v1 ) )
            return true;
        if( esDerecho( vertice ) && esIzquierdo( v1 ) )
            return true;
        return false;
    }

    private void agregarFantasma(VerticeRojinegro vertice){
        if( izquierdo( vertice ) == null ){
            vertice.izquierdo = nuevoVertice( null );
            izquierdo( vertice ).color = Color.NEGRO;
        }
        return;
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = verticeRojinegro( busca( elemento ) );
        if( v == null )
            return;
        elementos--;
        if( izquierdo( v ) != null ){
            VerticeRojinegro u = verticeRojinegro( maximoEnSubarbol( v.izquierdo ) );
            v.elemento = u.elemento;
            v = u;
        }
        if( izquierdo( v ) == null && derecho( v ) == null )
            agregarFantasma( v );
        VerticeRojinegro hijo = null;
        if( izquierdo( v ) == null ){
            hijo = derecho( v );
            subir( hijo, v );
        }
	else{
            hijo = izquierdo( v );
            subir( hijo, v );
        }
        if( esRojo( hijo ) ){
            pintarNegro( hijo );
            return;
        }
        if( esNegro( v ) && esNegro( hijo ) )
            rebalanceaElimina( hijo );
        if( hijo.elemento == null )
            if( hijo.padre == null )
                raiz = null;
            else
                if( esIzquierdo( hijo ) )
                    hijo.padre.izquierdo = null;
                else
                    hijo.padre.derecho = null;
    }

}
