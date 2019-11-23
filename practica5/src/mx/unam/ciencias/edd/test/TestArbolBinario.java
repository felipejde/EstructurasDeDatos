package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para métodos útiles para las pruebas unitarias de las clases que
 * extiendan {@link ArbolBinario}.
 */
public class TestArbolBinario {

    /* Valida un vértice, y recursivamente valida sus hijos. */
    private static void arbolBinarioValido(VerticeArbolBinario<?> v) {
        try {
            if (v.hayIzquierdo()) {
                VerticeArbolBinario<?> i = v.getIzquierdo();
                Assert.assertTrue(i.hayPadre());
                Assert.assertTrue(i.getPadre() == v);
                arbolBinarioValido(i);
            }
            if (v.hayDerecho()) {
                VerticeArbolBinario<?> d = v.getDerecho();
                Assert.assertTrue(d.hayPadre());
                Assert.assertTrue(d.getPadre() == v);
                arbolBinarioValido(d);
            }
        } catch (NoSuchElementException sdee) {
            Assert.fail();
        }
    }

    /**
     * Valida un árbol binario. Para todos sus vértices comprueba que si un
     * vértice A tiene como hijo al vértice B, entonces el vértice B tiene al
     * vértice A como padre.
     * @param arbol el árbol a validar.
     */
    public static void arbolBinarioValido(ArbolBinario<?> arbol) {
        if (arbol.getElementos() == 0)
            return;
        VerticeArbolBinario<?> v = arbol.raiz();
        Assert.assertFalse(v.hayPadre());
        arbolBinarioValido(v);
    }

    /**
     * Prueba unitaria para {@link ArbolBinario#ArbolBinario}.
     */
    @Test public void testConstructor() {
        ArbolBinario<Integer> a = new ArbolBinario<Integer>() {
                @Override public void agrega(Integer elemento) {}
                @Override public void elimina(Integer elemento) {}
                @Override public boolean contiene(Integer elemento) {
                    return false;
                }
                @Override public Iterator<Integer> iterator() {
                    return null;
                }
            };
        Assert.assertTrue(a.getElementos() == 0);
        Assert.assertTrue(a.profundidad() == -1);
    }
}
