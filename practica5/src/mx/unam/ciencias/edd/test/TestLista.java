package mx.unam.ciencias.edd.test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ExcepcionIndiceInvalido;
import mx.unam.ciencias.edd.IteradorLista;
import mx.unam.ciencias.edd.Lista;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link Lista}.
 */
public class TestLista {

    private Random random;
    private int total;
    private Lista<Integer> lista;

    /**
     * Crea un generador de números aleatorios para cada prueba, un número total
     * de elementos para nuestra lista, y una lista.
     */
    public TestLista() {
        random = new Random();
        total = 10 + random.nextInt(90);
        lista = new Lista<Integer>();
    }

    /**
     * Prueba unitaria para {@link Lista#getLongitud}.
     */
    @Test public void testGetLongitud() {
        Assert.assertTrue(lista.getLongitud() == 0);
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(random.nextInt(total));
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        Assert.assertTrue(lista.getLongitud() == total);
    }

    /**
     * Prueba unitaria para {@link Lista#esVacio}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(lista.esVacio());
        lista.agrega(1);
        Assert.assertFalse(lista.esVacio());
        lista.elimina(1);
        Assert.assertTrue(lista.esVacio());
    }

    /**
     * Prueba unitaria para {@link Lista#getElementos}.
     */
    @Test public void testGetElementos() {
        Assert.assertTrue(lista.getElementos() == 0);
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(random.nextInt(total));
            Assert.assertTrue(lista.getElementos() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(random.nextInt(total));
            Assert.assertTrue(lista.getElementos() == i + 1);
        }
        Assert.assertTrue(lista.getElementos() == total);
    }

    /**
     * Prueba unitaria para {@link Lista#agregaFinal}.
     */
    @Test public void testAgregaFinal() {
        lista.agregaFinal(1);
        Assert.assertTrue(1 == lista.getUltimo());
        lista.agregaInicio(2);
        Assert.assertFalse(2 == lista.getUltimo());
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            Assert.assertTrue(e == lista.getUltimo());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#agregaInicio}.
     */
    @Test public void testAgregaInicio() {
        lista.agregaInicio(1);
        Assert.assertTrue(1  == lista.getPrimero());
        lista.agregaFinal(2);
        Assert.assertFalse(2 == lista.getPrimero());
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(e == lista.getPrimero());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#elimina}.
     */
    @Test public void testElimina() {
        lista.elimina(0);
        lista.agrega(1);
        Assert.assertFalse(lista.esVacio());
        lista.elimina(1);
        Assert.assertTrue(lista.esVacio());
        int d = random.nextInt(total);
        int m = d - 10;
        for (int i = 0; i < total; i++) {
            lista.agregaInicio(d++);
            if (i == total / 2)
                m = d - 1;
        }
        int p = lista.getPrimero();
        int u = lista.getUltimo();
        Assert.assertTrue(lista.contiene(p));
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(u));
        lista.elimina(p);
        Assert.assertFalse(lista.contiene(p));
        Assert.assertTrue(lista.getLongitud() == --total);
        lista.elimina(m);
        Assert.assertFalse(lista.contiene(m));
        Assert.assertTrue(lista.getLongitud() == --total);
        lista.elimina(u);
        Assert.assertFalse(lista.contiene(u));
        Assert.assertTrue(lista.getLongitud() == --total);
        while (lista.getLongitud() > 0) {
            lista.elimina(lista.getPrimero());
            Assert.assertTrue(lista.getLongitud() == --total);
            if (lista.getLongitud() == 0)
                continue;
            lista.elimina(lista.getUltimo());
            Assert.assertTrue(lista.getLongitud() == --total);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#eliminaPrimero}.
     */
    @Test public void testEliminaPrimero() {
        try {
            lista.eliminaPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (lista.getLongitud() > 0) {
            Assert.assertTrue(n-- == lista.getLongitud());
            int k = lista.eliminaPrimero();
            Assert.assertTrue(k == a[i++]);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#eliminaUltimo}.
     */
    @Test public void testEliminaUltimo() {
        try {
            lista.eliminaUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (lista.getLongitud() > 0) {
            Assert.assertTrue(n-- == lista.getLongitud());
            int k = lista.eliminaUltimo();
            Assert.assertTrue(k == a[total - ++i]);
        }
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#contiene}.
     */
    @Test public void testContiene() {
        Assert.assertFalse(lista.contiene(0));
        int d = random.nextInt(total);
        int m = d - 10;
        int n = d - 1;
        for (int i = 0; i < total; i++) {
            lista.agregaFinal(d++);
            if (i == total/2)
                m = d - 1;
        }
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(new Integer(m)));
        Assert.assertFalse(lista.contiene(n));
    }

    /**
     * Prueba unitaria para {@link Lista#reversa}.
     */
    @Test public void testReversa() {
        Lista<Integer> reversa = lista.reversa();
        Assert.assertTrue(reversa.esVacio());
        Assert.assertFalse(reversa == lista);
        Assert.assertTrue(reversa.equals(lista));
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        reversa = lista.reversa();
        Assert.assertTrue(reversa.getLongitud() == lista.getLongitud());
        IteradorLista<Integer> i1 = lista.iteradorLista();
        IteradorLista<Integer> i2 = reversa.iteradorLista();
        i2.end();
        while (i1.hasNext() && i2.hasPrevious())
            Assert.assertTrue(i1.next().equals(i2.previous()));
        Assert.assertFalse(i1.hasNext());
        Assert.assertFalse(i2.hasPrevious());
    }

    /**
     * Prueba unitaria para {@link Lista#copia}.
     */
    @Test public void testCopia() {
        Lista<Integer> copia = lista.copia();
        Assert.assertTrue(copia.esVacio());
        Assert.assertFalse(copia == lista);
        Assert.assertTrue(copia.equals(lista));
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        copia = lista.copia();
        Assert.assertFalse(lista == copia);
        Iterator<Integer> il = lista.iterator();
        Iterator<Integer> ic = copia.iterator();
        while (il.hasNext() && ic.hasNext())
            Assert.assertTrue(il.next().equals(ic.next()));
        Assert.assertFalse(il.hasNext());
        Assert.assertFalse(ic.hasNext());
    }

    /**
     * Prueba unitaria para {@link Lista#limpia}.
     */
    @Test public void testLimpia() {
        int primero = random.nextInt(total);
        lista.agregaFinal(primero);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        int ultimo = random.nextInt(total);
        lista.agregaFinal(ultimo);
        Assert.assertFalse(lista.esVacio());
        Assert.assertTrue(lista.getPrimero() == primero);
        Assert.assertTrue(lista.getUltimo() == ultimo);
        lista.limpia();
        Assert.assertTrue(lista.esVacio());
        Assert.assertTrue(lista.getLongitud() == 0);
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Lista#getPrimero}.
     */
    @Test public void testGetPrimero() {
        try {
            lista.getPrimero();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaInicio(e);
            Assert.assertTrue(e == lista.getPrimero());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#getUltimo}.
     */
    @Test public void testGetUltimo() {
        try {
            lista.getUltimo();
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++) {
            int e = random.nextInt(total);
            lista.agregaFinal(e);
            Assert.assertTrue(e == lista.getUltimo());
        }
    }

    /**
     * Prueba unitaria para {@link Lista#get}.
     */
    @Test public void testGet() {
        try {
            lista.get(0);
            Assert.fail();
        } catch (ExcepcionIndiceInvalido eii) {}
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(lista.get(i) == a[i]);
        try {
            lista.get(-1);
            Assert.fail();
        } catch (ExcepcionIndiceInvalido eii) {}
        try {
            lista.get(total);
            Assert.fail();
        } catch (ExcepcionIndiceInvalido eii) {}
    }

    /**
     * Prueba unitaria para {@link Lista#indiceDe}.
     */
    @Test public void testIndiceDe() {
        Assert.assertTrue(lista.indiceDe(0) == -1);
        int ini = random.nextInt(total);
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = ini + i;
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i ++)
            Assert.assertTrue(i == lista.indiceDe(a[i]));
        Assert.assertTrue(lista.indiceDe(ini - 10) == -1);
    }

    /**
     * Prueba unitaria para {@link Lista#iterator}.
     */
    @Test public void testIterator() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] =random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        Iterator<Integer> it = lista.iterator();
        while (it.hasNext()) {
            int k = it.next();
            Assert.assertTrue(k == a[i++]);
        }
    }

    /**
     * Prueba unitaria para {@link Lista#iteradorLista}.
     */
    @Test public void testIteradorLista() {
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = random.nextInt(total);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int c = 0;
        IteradorLista<Integer> it = lista.iteradorLista();
        while (it.hasNext()) {
            int k = it.next();
            Assert.assertTrue(k == a[i++]);
            c++;
        }
        Assert.assertTrue(c == total);
        c = 0;
        while (it.hasPrevious()) {
            int k = it.previous();
            Assert.assertTrue(k == a[--i]);
            c++;
        }
        Assert.assertTrue(c == total);
        c = 0;
        while (it.hasNext()) {
            int k = it.next();
            Assert.assertTrue(k == a[i++]);
            c++;
        }
        Assert.assertTrue(c == total);
        it.start();
        Assert.assertTrue(it.next() == lista.getPrimero());
        it.end();
        Assert.assertTrue(it.previous() == lista.getUltimo());
    }

    /**
     * Prueba unitaria para {@link Lista#equals}.
     */
    @Test public void testEquals() {
        Lista<Integer> otra = new Lista<Integer>();
        Assert.assertTrue(lista != otra);
        Assert.assertTrue(lista.equals(otra));
        for (int i = 0; i < total; i++) {
            int r = random.nextInt(total);
            lista.agregaFinal(r);
            otra.agregaFinal(r);
        }
        Assert.assertTrue(lista.equals(otra));
        int u = lista.getUltimo();
        lista.elimina(u);
        lista.agregaFinal(u+1);
        Assert.assertFalse(lista.equals(otra));
    }

    /**
     * Prueba unitaria para {@link Lista#toString}.
     */
    @Test public void testToString() {
        Assert.assertTrue("[]".equals(lista.toString()));
        int[] a = new int[total];
        for (int i = 0; i < total; i++) {
            a[i] = i;
            lista.agregaFinal(a[i]);
        }
        String s = "[";
        for (int i = 0; i < total-1; i++)
            s += String.format("%s, ", a[i]);
        s += String.format("%s]", a[total-1]);
        Assert.assertTrue(s.equals(lista.toString()));
    }

    /**
     * Prueba unitaria para {@link Lista#mergeSort}.
     */
    @Test public void testMergeSort() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        Lista<Integer> ordenada = Lista.mergeSort(lista);
        Assert.assertTrue(lista.getLongitud() == ordenada.getLongitud());
        for (int e : lista)
            Assert.assertTrue(ordenada.contiene(e));
        int a = ordenada.getPrimero();
        for (int e : ordenada) {
            Assert.assertTrue(a <= e);
            a = e;
        }
    }

    /**
     * Prueba unitaria para {@link Lista#busquedaLineal}.
     */
    @Test public void testBusquedaLineal() {
        for (int i = 0; i < total; i++)
            lista.agregaFinal(random.nextInt(total));
        lista = Lista.mergeSort(lista);
        int m = lista.get(total/2);
        Assert.assertTrue(Lista.busquedaLineal(lista, m));
        int o = lista.getPrimero() - 10;
        Assert.assertFalse(Lista.busquedaLineal(lista, o));
    }
}
