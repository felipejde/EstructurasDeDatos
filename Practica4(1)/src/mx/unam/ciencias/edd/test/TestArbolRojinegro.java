package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolRojinegro}.
 */
public class TestArbolRojinegro {

    private int total;
    private Random random;
    private ArbolRojinegro<Integer> arbol;

    /* Valida el vértice de un árbol rojinegro, y recursivamente
     * revisa sus hijos. */
    private static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol,
                         VerticeArbolBinario<T> v) {
        switch (arbol.getColor(v)) {
        case NEGRO:
            if (v.hayIzquierdo())
                arbolRojinegroValido(arbol, v.getIzquierdo());
            if (v.hayDerecho())
                arbolRojinegroValido(arbol, v.getDerecho());
            break;
        case ROJO:
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<T> i = v.getIzquierdo();
                Assert.assertTrue(arbol.getColor(i) == Color.NEGRO);
                arbolRojinegroValido(arbol, i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<T> d = v.getDerecho();
                Assert.assertTrue(arbol.getColor(d) == Color.NEGRO);
                arbolRojinegroValido(arbol, d);
            }
            break;
        default:
            Assert.fail();
        }
    }

    /* Valida que los caminos del vértice a sus hojas tengan todos
       el mismo número de vértices negros. */
    private static <T extends Comparable<T>> int
    validaCaminos(ArbolRojinegro<T> arbol,
                  VerticeArbolBinario<T> v) {
        int ni = -1, nd = -1;
        if (v.hayIzquierdo()) {
            VerticeArbolBinario<T> i = v.getIzquierdo();
            ni = validaCaminos(arbol, i);
        } else {
            ni = 1;
        }
        if (v.hayDerecho()) {
            VerticeArbolBinario<T> d = v.getDerecho();
            nd = validaCaminos(arbol, d);
        } else {
            nd = 1;
        }
        Assert.assertTrue(ni == nd);
        switch (arbol.getColor(v)) {
        case NEGRO:
            return 1 + ni;
        case ROJO:
            return ni;
        default:
            Assert.fail();
        }
        // Inalcanzable.
        return -1;
    }

    /**
     * Valida un árbol rojinegro. Comprueba que la raíz sea negra, que las hojas
     * sean negras, que un vértice rojo tenga dos hijos negros, y que todo
     * camino de la raíz a sus hojas tiene el mismo número de vértices negros.
     * @param <T> tipo del que puede ser el árbol rojinegro.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolRojinegroValido(ArbolRojinegro<T> arbol) {
        if (arbol.getElementos() == 0)
            return;
        TestArbolBinarioOrdenado.arbolBinarioOrdenadoValido(arbol);
        VerticeArbolBinario<T> v = arbol.raiz();
        Assert.assertTrue(arbol.getColor(v) == Color.NEGRO);
        arbolRojinegroValido(arbol, v);
        validaCaminos(arbol, v);
    }

    /**
     * Crea un árbol rojo-ngro para cada prueba.
     */
    public TestArbolRojinegro() {
        random = new Random();
        arbol = new ArbolRojinegro<Integer>();
        total = random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolRojinegroValido(arbol);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolRojinegro#elimina}.
     */
    @Test public void testElimina() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            int r;
            boolean repetido = false;
            do {
                r = random.nextInt(1000);
                repetido = false;
                for (int j = 0; j < i; j++)
                    if (r == a[j])
                        repetido = true;
            } while (repetido);
            a[i] = r;
            arbol.agrega(a[i]);
        }
        for (int i : a)
            Assert.assertTrue(arbol.busca(i) != null);
        int n = total;
        while (arbol.getElementos() > 0) {
            Assert.assertTrue(arbol.getElementos() == n);
            int i = random.nextInt(total);
            if (a[i] == -1)
                continue;
            int e = a[i];
            VerticeArbolBinario<Integer> it = arbol.busca(e);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == e);
            arbol.elimina(e);
            it = arbol.busca(e);
            Assert.assertTrue(it == null);
            Assert.assertTrue(arbol.getElementos() == --n);
            arbolRojinegroValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolRojinegro<Integer>();
        ArbolRojinegro<Integer> arbol2 = new ArbolRojinegro<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolRojinegro<Integer>();
        arbol2 = new ArbolRojinegro<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
    }
}
