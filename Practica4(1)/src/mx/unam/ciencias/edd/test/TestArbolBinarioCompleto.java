package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Cola;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link ArbolBinarioCompleto}.
 */
public class TestArbolBinarioCompleto {

    private int total;
    private Random random;
    private ArbolBinarioCompleto<Integer> arbol;

    /**
     * Valida un árbol binario completo. Comprueba que todos los
     * niveles del árbol estén llenos excepto tal vez el último.
     * @param <T> tipo del que puede ser el árbol binario completo.
     * @param arbol el árbol a revisar.
     */
    public static <T extends Comparable<T>> void
    arbolBinarioCompletoValido(ArbolBinarioCompleto<T> arbol) {
        if (arbol.getElementos() == 0)
            return;
        UtileriasArbolBinario.arbolBinarioValido(arbol);
        Assert.assertTrue(arbol.profundidad() ==
                          (int)(Math.floor(Math.log(arbol.getElementos()) /
                                           Math.log(2))));
    }

    /**
     * Crea un árbol binario completo para cada prueba.
     */
    public TestArbolBinarioCompleto() {
        random = new Random();
        arbol = new ArbolBinarioCompleto<Integer>();
        total = 3 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arbol.agrega(n);
            Assert.assertTrue(arbol.getElementos() == i+1);
            VerticeArbolBinario<Integer> it = arbol.busca(n);
            Assert.assertTrue(it != null);
            Assert.assertTrue(it.get() == n);
            arbolBinarioCompletoValido(arbol);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#elimina}.
     */
    @Test public void testElimina() {
        Assert.assertTrue(arbol.getElementos() == 0);
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
            arbolBinarioCompletoValido(arbol);
            a[i] = -1;
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#busca}.
     */
    @Test public void testBusca() {
        int[] a = new int[total];
        int ini = random.nextInt(total);
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            arbolBinarioCompletoValido(arbol);
            arbol.agrega(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(arbol.busca(a[i]) != null);
        Assert.assertTrue(arbol.busca(ini - total) == null);
        Assert.assertTrue(arbol.busca(ini + total*2) == null);
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#profundidad}.
     */
    @Test public void testProfundidad() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioCompletoValido(arbol);
            int p = (int)Math.floor(Math.log(i+1)/Math.log(2));
            Assert.assertTrue(arbol.profundidad() == p);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioCompletoValido(arbol);
            Assert.assertTrue(arbol.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#raiz}.
     */
    @Test public void testRaiz() {
        try {
            arbol.raiz();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int p = Integer.MAX_VALUE;
        for (int i = 0; i < total; i++) {
            int v = random.nextInt(total);
            if (p == Integer.MAX_VALUE)
                p = v;
            arbol.agrega(v);
        }
        VerticeArbolBinario<Integer> v = arbol.raiz();
        Assert.assertTrue(v.get() == p);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#esVacio}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(arbol.esVacio());
        arbol.agrega(1);
        Assert.assertFalse(arbol.esVacio());
        arbol.elimina(1);
        Assert.assertTrue(arbol.esVacio());
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            Assert.assertFalse(arbol.esVacio());
        }
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#equals}.
     */
    @Test public void testEquals() {
        arbol = new ArbolBinarioCompleto<Integer>();
        ArbolBinarioCompleto<Integer> arbol2 = new ArbolBinarioCompleto<Integer>();
        Assert.assertTrue(arbol.equals(arbol2));
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertTrue(arbol.equals(arbol2));
        arbol = new ArbolBinarioCompleto<Integer>();
        arbol2 = new ArbolBinarioCompleto<Integer>();
        for (int i = 0; i < total; i++) {
            arbol.agrega(i);
            if (i != total - 1)
                arbol2.agrega(i);
        }
        Assert.assertFalse(arbol == arbol2);
        Assert.assertFalse(arbol.equals(arbol2));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#toString}.
     */
    @Test public void testToString() {
        /* Estoy dispuesto a aceptar una mejor prueba. */
        Assert.assertTrue(arbol.toString() != null &&
                          arbol.toString().equals(""));
        for (int i = 0; i < total; i++) {
            arbol.agrega(random.nextInt(total));
            arbolBinarioCompletoValido(arbol);
            Assert.assertTrue(arbol.toString() != null &&
                              !arbol.toString().equals(""));
        }
        String cadena =
            "1\n" +
            "├─›2\n" +
            "│  ├─›4\n" +
            "│  └─»5\n" +
            "└─»3";
        arbol = new ArbolBinarioCompleto<Integer>();
        for (int i = 1; i <= 5; i++)
            arbol.agrega(i);
        Assert.assertTrue(arbol.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link ArbolBinarioCompleto#iterator}.
     */
    @Test public void testIterator() {
        int[] arreglo = new int[total];
        for (int i = 0; i < total; i++) {
            int n = random.nextInt(100);
            arreglo[i] = n;
            arbol.agrega(n);
        }
        int c = 0;
        for (Integer i : arbol)
            Assert.assertTrue(i == arreglo[c++]);
        Assert.assertTrue(c == total);
    }
}
