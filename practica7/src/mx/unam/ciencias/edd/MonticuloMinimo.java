package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear un montículo
 * mínimo con <em>n</em> elementos en tiempo <em>O</em>(<em>n</em>), y podemos
 * agregar y actualizar elementos en tiempo <em>O</em>(log <em>n</em>). Eliminar
 * el elemento mínimo también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador<T extends ComparableIndexable<T>>
        implements Iterator<T> {

        /* Indice del iterador. */
        private int indice;
        /* El montículo mínimo. */
        private MonticuloMinimo<T> monticulo;

        /* Construye un nuevo iterador, auxiliándose del montículo mínimo. */
        public Iterador(MonticuloMinimo<T> monticulo) {
            this.monticulo=monticulo;
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            if (indice>=monticulo.siguiente)
                return false;
            return true;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(hasNext())
               return monticulo.arbol[indice++]; 
            return null;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* El siguiente índice dónde agregar un elemento. */
    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Lista)}, pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = creaArregloGenerico(10);
    }

    /**
     * Constructor para montículo mínimo que recibe una lista. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param lista la lista a partir de la cuál queremos construir el
     *              montículo.
     */
    public MonticuloMinimo(Lista<T> lista) {
        arbol = creaArregloGenerico(lista.getLongitud());
        
        for (T e:lista){
            e.setIndice(siguiente);
            arbol[siguiente]=e;
            siguiente++;
        }

        validarMonticulo(arbol.length/2);
    }

    private void validarMonticulo(int actual){
        if(actual<0)
            return;
        minHeapify(actual);
        validarMonticulo(actual-1);
    }

    private void minHeapify(int padre){
        if(padre>=siguiente)
            return;

        int izq = 2*(padre)+1;
        int der = izq + 1;
        int minimo = padre;

        if( izq<siguiente && arbol[izq].compareTo(arbol[minimo])<0)
            minimo = izq;

        if( der<siguiente && arbol[der].compareTo(arbol[minimo])<0)
            minimo = der;

        if(minimo!=padre){
            reemplaza(padre,minimo);
            minHeapify(minimo);
        }
    }

    private void antiMinHeapify(int hijo){
        int padre = (hijo-1)/2;

        if(hijo==0)
            return;

        if(padre<0)
            return;

        if(arbol[padre].compareTo(arbol[hijo])>0){
            reemplaza(padre,hijo);
            antiMinHeapify(padre);
        }
    }

    private void reemplaza(int i, int j){
        if ( ( i>arbol.length || j>arbol.length ) || ( i<0 || j<0 ) ) //si los indices estan fuera del arreglo
            return;

        T reemplazo = arbol[i];
        arbol[i]=arbol[j];
        arbol[j]=reemplazo;
        reemplazo.setIndice(j);
        arbol[i].setIndice(i); 
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        elemento.setIndice(siguiente);

        if(siguiente+1>arbol.length)
            arbol = duplicaArbol();

        arbol[siguiente] = elemento;
        antiMinHeapify(siguiente);
        siguiente++;
    }

    private T[] duplicaArbol(){
        T[] nuevo = creaArregloGenerico(arbol.length*2);
        for (int i=0; i<arbol.length; i++) {
            nuevo[i]=arbol[i];
        }
        return nuevo;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
        if (esVacio())
            throw new IllegalStateException();
        reemplaza(0,siguiente-1);
        T eliminado = arbol[siguiente-1];
        arbol[siguiente-1]=null;
        siguiente--;
        eliminado.setIndice(-1);
        minHeapify(0);
        return eliminado;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i=elemento.getIndice();
        if(i<0 || i>=siguiente || !arbol[i].equals(elemento))
            return;
        while (elemento.getIndice()<siguiente){
            int izq = 2*elemento.getIndice()+1;
            int der = izq+1;
            if (izq<siguiente && der<siguiente){
                if (arbol[izq].compareTo(arbol[der])<0) 
                    reemplaza(elemento.getIndice(),izq);
                else 
                    reemplaza(elemento.getIndice(),der);
            }else 
                if(izq<siguiente) 
                    reemplaza(elemento.getIndice(),izq);
                else 
                    if(der<siguiente){
                        reemplaza( elemento.getIndice(), der );
                        i = der;
                } else 
                    break;
            }
        i = elemento.getIndice();
        int ultimo=siguiente-1;
        if (i!=ultimo){
            reemplaza(i,ultimo);
            reordena(arbol[i]);
        }
        arbol[ultimo]=null;
        siguiente--;
        elemento.setIndice(-1);
    }

    private void cambiarPorHijo(int i){
        int izq = 2*(i)+1;
        int der = izq + 1;
        int minimo = i;

        if(arbol[izq]==null && arbol[der]==null)
            return;

        if(arbol[izq]!=null && arbol[izq].compareTo(arbol[minimo])<0)
            minimo = izq;

        if(arbol[der]!=null && arbol[der].compareTo(arbol[minimo])<0)
            minimo = der;

        reemplaza(i,minimo);
        cambiarPorHijo(minimo);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        try{
            return arbol[elemento.getIndice()]==elemento;
        }catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    public boolean esVacio() {
        return siguiente == 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
        antiMinHeapify(elemento.getIndice());
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    public T get(int i) {
        if(i<0 || i>= arbol.length)
            throw new NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador<T>(this);
    }
}
