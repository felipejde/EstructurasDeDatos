package proyecto2;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador(Grafica<T> grafica) {
            iterador = grafica.vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Aristas para gráficas; para poder guardar el peso de las aristas. */
    private class Arista {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice vecino;
        /* El peso de arista conectando al vértice con el vecino. */
        public double peso;

        /* Construye una nueva arista con el vértice recibido como vecino y el
         * peso especificado. */
        public Arista(Grafica<T>.Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements ComparableIndexable<Vertice>,
        VerticeGrafica<T> {

        /* Iterador para las vecinos del vértice. */
        private class IteradorVecinos implements Iterator<VerticeGrafica<T>> {

            /* Iterador auxiliar. */
            private Iterator<Grafica<T>.Arista> iterador;

            /* Construye un nuevo iterador, auxiliándose de la lista de
             * vecinos. */
            public IteradorVecinos(Iterator<Grafica<T>.Arista> iterador) {
                this.iterador = iterador;
            }

            /* Nos dice si hay un siguiente vecino. */
            @Override public boolean hasNext() {
                return iterador.hasNext();
            }

            /* Regresa el siguiente vecino. */
            @Override public VerticeGrafica<T> next() {
                return iterador.next().vecino;
            }

            /* No lo implementamos: siempre lanza una excepción. */
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de aristas que conectan al vértice con sus vecinos. */
        public Lista<Grafica<T>.Arista> aristas;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            aristas = new Lista<Grafica<T>.Arista>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return aristas.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterador para los vecinos. */
        @Override public Iterator<VerticeGrafica<T>> iterator() {
            return new IteradorVecinos(aristas.iterator());
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if(this.distancia==-1)
                return 1;
            if(vertice.distancia==-1)
                return -1;
            return Double.compare(this.distancia,vertice.distancia);
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(contiene(elemento))
            throw new IllegalArgumentException();
        vertices.agregaFinal(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        if(a.equals(b))
            throw new IllegalArgumentException();
        if(sonVecinos(a,b))
            throw new IllegalArgumentException();
        if(contiene(a)&&contiene(b)){
            Vertice v = busca(a);
            Vertice u = busca(b);
            v.aristas.agregaFinal(new Arista(u,0));
            u.aristas.agregaFinal(new Arista(v,0));
            aristas++;
            return;
        }
        throw new NoSuchElementException();
    }

    private Vertice busca(T a){
        if(a==null)
            return null;
        for (Vertice v: vertices)
            if(v.elemento.equals(a))
                return v;
        return null;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva arista.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b, double peso) {
        if(a.equals(b))
            throw new IllegalArgumentException();
        if(sonVecinos(a,b))
            throw new IllegalArgumentException();
        if(contiene(a)&&contiene(b)){
            Vertice v = busca(a);
            Vertice u = busca(b);
            v.aristas.agregaFinal(new Arista(u,peso));
            u.aristas.agregaFinal(new Arista(v,peso));
            aristas++;
            return;
        }
        throw new NoSuchElementException();
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice v = busca(a);
        Vertice u = busca(b);
        Arista uv,vu;

        if(contiene(a)&&contiene(b)){
            uv = buscaArista(u,v);
            vu = buscaArista(v,u);
            if(v.aristas.contiene(vu)){
                v.aristas.elimina(vu);
                u.aristas.elimina(uv);
                aristas--;
                return;
            }
            throw new IllegalArgumentException();
        }
        throw new NoSuchElementException();
    }

    private Arista buscaArista(Vertice v, Vertice u){
        for (Arista a : v.aristas)         
            if(a.vecino.equals(u))
                return a;
        return null;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice v : vertices)
            if(v.elemento.equals(elemento))
                return true;
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        vertices.elimina(v);
        for(Arista a : v.aristas){
            a.vecino.aristas.elimina(buscaArista(a.vecino,v));
            aristas--;
        }
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if(!contiene(a)||!contiene(b))
            throw new NoSuchElementException();
        Vertice v = busca(a);
        Vertice u = busca(b);
        if(v.aristas.contiene(buscaArista(v,u)))
            return true;
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos, o -1 si los elementos no están
     *         conectados.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public double getPeso(T a, T b) {
        Vertice v = busca(a);
        Vertice u = busca(b);

        if(v==null || u==null)
            throw new NoSuchElementException();

        Arista vu = buscaArista(v,u);

        if(vu==null)
            return -1;
        return vu.peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        return v;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for ( Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        Cola<Vertice> estructura = new Cola<Vertice>();
        paseo(v,estructura,accion);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = busca(elemento);
        if(v==null)
            throw new NoSuchElementException();
        Pila<Vertice> estructura = new Pila<Vertice>();
        paseo(v,estructura,accion);
    }

    private void paseo (Vertice v ,MeteSaca<Vertice> estructura, AccionVerticeGrafica<T> accion){
        for( Vertice u : vertices)
            u.color=Color.NEGRO;
        estructura.mete(v);
        v.color=Color.ROJO;
        while(!estructura.esVacia()){
            v = estructura.saca();
            accion.actua(v);
            for(Arista a: v.aristas)
                if(a.vecino.color==Color.NEGRO){
                    estructura.mete(a.vecino);
                    a.vecino.color=Color.ROJO;
                }
        }
        for(Vertice u: vertices)
            u.color=Color.NINGUNO;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador(this);
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice v = busca(origen);
        Vertice u = busca(destino);
        Cola<Vertice> cola = new Cola<Vertice>();

        if(v==null || u==null)
            throw new NoSuchElementException();

        for(Vertice w : vertices)
            w.distancia = -1;
        v.distancia=0;

        cola.mete(v);
        while(!cola.esVacia()){
            Vertice w = cola.saca();
            for(Arista a:w.aristas)
                if(a.vecino.distancia==-1){
                    cola.mete(a.vecino);
                    a.vecino.distancia = w.distancia+1;
                }
        }

        if(u.distancia==-1)
            return new Lista<VerticeGrafica<T>>();

        return construyeTrayectoriaMinima(u,new Lista<VerticeGrafica<T>>());
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        Vertice v = busca(origen);
        Vertice u = busca(destino);

        if(v==null || u==null)
            throw new NoSuchElementException();

        for(Vertice w : vertices)
            w.distancia = -1;
        v.distancia=0;

        MonticuloMinimo<Vertice> heap = new MonticuloMinimo<Vertice>(vertices);

        while(!heap.esVacio()){
            Vertice w = heap.elimina();
            for(Arista a:w.aristas){
                if(a.vecino.distancia==-1)
                    a.vecino.distancia=a.peso+w.distancia;
                double nuevaDistancia = a.peso+w.distancia;
                if(nuevaDistancia<a.vecino.distancia)
                    a.vecino.distancia=nuevaDistancia;
                heap.reordena(a.vecino);
            }
        }

        if(u.distancia==-1)
            return new Lista<VerticeGrafica<T>>();
        return construyeTrayectoriaDijkstra(u,new Lista<VerticeGrafica<T>>());       
    }

    private Lista<VerticeGrafica<T>> construyeTrayectoriaMinima(Vertice v, Lista<VerticeGrafica<T>> lista){
        lista.agregaFinal(v);

        if(v.distancia==0)
            return lista.reversa();

        return construyeTrayectoriaMinima(buscaAnterior(v),lista);
    }

    private Vertice buscaAnterior(Vertice v){
        for(Arista a: v.aristas)
            if(a.vecino.distancia==v.distancia-1)
                return a.vecino;
        return null;
    }

    private Lista<VerticeGrafica<T>> construyeTrayectoriaDijkstra(Vertice v, Lista<VerticeGrafica<T>> lista){
        lista.agregaFinal(v);

        if(v.distancia==0)
            return lista.reversa();

        return construyeTrayectoriaDijkstra(buscaAnteriorDijkstra(v),lista);
    }

    private Vertice buscaAnteriorDijkstra(Vertice v){
        for(Arista a: v.aristas)
            if(a.vecino.distancia==v.distancia-a.peso)
                return a.vecino;
        return null;
    }
}
