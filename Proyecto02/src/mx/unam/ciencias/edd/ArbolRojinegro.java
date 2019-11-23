package mx.unam.ciencias.edd.proyecto2;
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
            super(elemento);
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

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        pintarRojo(verticeRojinegro(ultimoAgregado));
        rebalanceaAgrega(verticeRojinegro(ultimoAgregado));
    }

    private void rebalanceaAgrega(VerticeRojinegro v){
        //Caso 1:
        if(padre(v)==null){
            pintarNegro(v);
            return;
        }
        //Caso 2:
        if(esNegro(padre(v)))
            return;
        //Caso 3:
        if(esRojo(tio(v))){
            pintarNegro(padre(v));
            pintarNegro(tio(v));
            pintarRojo(abuelo(v));
            rebalanceaAgrega(abuelo(v));
            return;
        }
        //Caso 4:
        if(estanCruzados(v,padre(v))){
            if(esIzquierdo(v)){
                giraDerecha(padre(v));
                v=derecho(v);
            }else{
                giraIzquierda(padre(v));
                v=izquierdo(v);
            }
        }
        //Caso 5:
        pintarNegro(padre(v));
        pintarRojo(abuelo(v));
        if(esIzquierdo(v))
            giraDerecha(abuelo(v));
        if(esDerecho(v))
            giraIzquierda(abuelo(v));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = verticeRojinegro(busca(elemento));
        if(v==null)
            return;
        elementos--;
        if(izquierdo(v)!=null){
            VerticeRojinegro u = verticeRojinegro(maximoEnSubarbol(v.izquierdo));
            v.elemento=u.elemento;
            v=u;
        }
        if(izquierdo(v)==null&&derecho(v)==null)
            agregarFantasma(v);
        VerticeRojinegro hijo=null;//<---*
        if(izquierdo(v)==null){
            hijo=derecho(v);
            subir(hijo,v);
        }else{
            hijo=izquierdo(v);
            subir(hijo,v);
        }
        if(esRojo(hijo)){
            pintarNegro(hijo);
            return;
        }
        if(esNegro(v)&&esNegro(hijo))
            rebalanceaElimina(hijo);
        if(hijo.elemento==null)
            if(hijo.padre==null)
                raiz=null;
            else
                if(esIzquierdo(hijo))
                    hijo.padre.izquierdo=null;
                else
                    hijo.padre.derecho=null;
    }

    private void rebalanceaElimina(VerticeRojinegro v){
        //Caso 1:
        if(padre(v)==null)
            return;
        //Caso 2:
        if(esRojo(hermano(v))){
            pintarRojo(padre(v));
            pintarNegro(hermano(v));
            if(esIzquierdo(v))
                giraIzquierda(padre(v));
            else
                giraDerecha(padre(v));
        }
        //Caso 3:
        if(esNegro(padre(v))&&
            esNegro(hermano(v))&&
            esNegro(izquierdo(hermano(v)))
            &&esNegro(derecho(hermano(v)))){
                pintarRojo(hermano(v));
                rebalanceaElimina(padre(v));
                return;
        }
        //Caso 4:
        if(esRojo(padre(v))
            &&esNegro(hermano(v))
            &&esNegro(izquierdo(hermano(v)))
            &&esNegro(derecho(hermano(v)))){
                pintarNegro(padre(v));
                pintarRojo(hermano(v));
                return;
        }
        //Caso 5:
        if(esIzquierdo(v)&&
            esNegro(derecho(hermano(v)))&&
            esRojo(izquierdo(hermano(v)))){
                pintarRojo(hermano(v));
                pintarNegro(izquierdo(hermano(v)));
                giraDerecha(hermano(v));
            }
        if(esDerecho(v)&&
            esNegro(izquierdo(hermano(v)))&&
            esRojo(derecho(hermano(v)))){
                pintarRojo(hermano(v));
                pintarNegro(derecho(hermano(v)));
                giraIzquierda(hermano(v)); 
        }
        //Caso 6:
        if(esNegro(padre(v)))
            pintarNegro(hermano(v));
        else
            pintarRojo(hermano(v));
        pintarNegro(padre(v));
        if(esIzquierdo(v)){
            pintarNegro(derecho(hermano(v)));
            giraIzquierda(padre(v));
        }else{
            pintarNegro(izquierdo(hermano(v)));
            giraDerecha(padre(v));
        }
    }

    private boolean esIzquierdo(VerticeRojinegro v){
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

    private boolean esDerecho(VerticeRojinegro v){
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
    
    private VerticeRojinegro abuelo(VerticeRojinegro v){
        if(v.padre==null)
            return null;
        if(v.padre.padre==null)
            return null;
        return verticeRojinegro(v.padre.padre);
    }
    
    private VerticeRojinegro hermano(VerticeRojinegro v){
        if(v.padre==null)
            return null;
        if(esIzquierdo(v))
            return verticeRojinegro(v.padre.derecho);
        return verticeRojinegro(v.padre.izquierdo);
    }

    private VerticeRojinegro tio(VerticeRojinegro v){
        if(v.padre==null)
            return null;
        return hermano(padre(v));
    }

    private boolean esNegro(VerticeRojinegro v){
        if(v==null)
            return true;
        if(v.color.equals(Color.NEGRO))
            return true;
        return false;
    }

    private boolean esRojo(VerticeRojinegro v){
        if(v==null)
            return false;
        if(v.color.equals(Color.ROJO))
            return true;
        return false;
    }

    private void pintarNegro(VerticeRojinegro v){
        if(v!=null)
            v.color=Color.NEGRO;
    }

    private void pintarRojo(VerticeRojinegro v){
        if(v!=null)
            v.color=Color.ROJO;
    }

    private boolean estanCruzados(VerticeRojinegro v,VerticeRojinegro p){
        if(esIzquierdo(v)&&esDerecho(p))
            return true;
        if(esDerecho(v)&&esIzquierdo(p))
            return true;
        return false;
    }

    private void agregarFantasma(VerticeRojinegro v){
        if(izquierdo(v)==null){
            v.izquierdo=nuevoVertice(null);
            izquierdo(v).color=Color.NEGRO;
        }
        return;
    }

    private void subir(VerticeRojinegro v,VerticeRojinegro padre){
        v.padre=padre.padre;
        if(padre.padre!=null)
            if(esDerecho(padre))
                padre.padre.derecho=v;
            else
                padre.padre.izquierdo=v;
        if(padre.padre==null)
            raiz=v;
    }

    private VerticeRojinegro padre(VerticeRojinegro v){
        if(v==null)
            return null;
        if(v.padre==null)
            return null;
        return verticeRojinegro(v.padre);
    }

    private VerticeRojinegro izquierdo(VerticeRojinegro v){
        if(v==null)
            return null;
        if(v.izquierdo==null)
            return null;
        return verticeRojinegro(v.izquierdo);
    }

    private VerticeRojinegro derecho(VerticeRojinegro v){
        if(v==null)
            return null;
        if(v.derecho==null)
            return null;
        return verticeRojinegro(v.derecho);
    }
}
