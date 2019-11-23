package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para utilerias de pruebas unitarias de la clase {@link ArbolBinario}.
 */
public class UtileriasArbolBinario {

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
     * Valida un árbol. Comprueba que si un vértice A tiene como hijo
     * al vértice B, entonces el vértice B tiene al vértice A como padre.
     * @param arbol el árbol a validar.
     */
    public static void arbolBinarioValido(ArbolBinario<?> arbol) {
        if (arbol.getElementos() == 0)
            return;
        VerticeArbolBinario<?> v = arbol.raiz();
        Assert.assertFalse(v.hayPadre());
        arbolBinarioValido(v);
    }
}
