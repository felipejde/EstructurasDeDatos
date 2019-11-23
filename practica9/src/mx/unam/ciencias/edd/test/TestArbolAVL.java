package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolAVL}.
 */
public class TestArbolAVL {

    private int total;
    private Random random;
    private ArbolAVL<Integer> arbol;

    /* Método auxiliar para validar la altura y balance de cada vértice. */
    private static <T extends Comparable<T>> int
    validaAlturasYBalances(ArbolAVL<T> arbol, VerticeArbolBinario<T> vertice) {
        int aIzq = vertice.hayIzquierdo() ?
            validaAlturasYBalances(arbol, vertice.getIzquierdo()) : -1;
        int aDer = vertice.hayDerecho() ?
            validaAlturasYBalances(arbol, vertice.getDerecho()) : -1;
        int altura = Math.max(aIzq, aDer) + 1;
        Assert.assertTrue(arbol.getAltura(vertice) == altura);
        int balance = aIzq - aDer;
        Assert.assertTrue(balance >= -1 && balance <= 1);
        return altura;
    }

    /**
     * Valida un árbol AVL. Comprueba que para todo nodo A se cumpla que su
     * altura sea correcta, y que su balance esté en el rango [-1, 1].
     * @param <T> tipo del que puede ser el árbol AVL.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolAVLValido(ArbolAVL<T> arbol) {
        if (arbol.getElementos() == 0)
            return;
        TestArbolBinarioOrdenado.arbolBinarioOrdenadoValido(arbol);
        VerticeArbolBinario<T> raiz = arbol.raiz();
        validaAlturasYBalances(arbol, raiz);
    }

    /**
     * Crea un árbol AVL para cada prueba.
     */
    public TestArbolAVL() {
        random = new Random();
        arbol = new ArbolAVL<Integer>();
        total = 1 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#ArbolAVL}.
     */
    @Test public void testConstructor() {
        arbol = new ArbolAVL<Integer>();
        Assert.assertTrue(arbol.getElementos() == 0);
    }

    /* Construye un arreglo con elementos no repetidos. */
    private int[] arregloSinRepetidos() {
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
        }
        return a;
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#agrega}.
     */
    @Test public void testAgrega() {
        Assert.assertTrue(arbol.getElementos() == 0);
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolAVLValido(arbol);
        }
        Assert.assertTrue(arbol.getElementos() == total);
    }

    /**
     * Prueba unitaria para {@link ArbolAVL#elimina}.
     */
    @Test public void testElimina() {
        arbol.elimina(0);
        int[] a = arregloSinRepetidos();
        for (int i = 0; i < total; i++)
            arbol.agrega(a[i]);
        int n = total;
        int m = total - 1;
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
            arbolAVLValido(arbol);
            a[i] = -1;
        }
        arbol.elimina(a[a.length/2]);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolAVL<Integer>();
        ArbolAVL<Integer> arbol2 = new ArbolAVL<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolAVL<Integer>();
        arbol2 = new ArbolAVL<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
        Assert.assertFalse(arbol.equals(""));
        Assert.assertFalse(arbol.equals(null));
    }
}
