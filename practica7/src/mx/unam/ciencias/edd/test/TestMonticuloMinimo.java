package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.ComparableIndexable;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloMinimo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para pruebas unitarias de la clase {@link MonticuloMinimo}.
 */
public class TestMonticuloMinimo {

    private Random random;
    private int total;
    private MonticuloMinimo<Indexable<String>> monticulo;

    /* Clase interna privada que implementa ComparableIndexable para
     * poder probar a MonticuloMinimo. */
    private class Indexable<T> implements ComparableIndexable<Indexable<T>> {
        public T elemento;
        public double valor;
        public int indice;

        /* Crea un indexable con el elemento elemento y el valor
         * valor. */
        public Indexable(T elemento, double valor) {
            this.elemento = elemento;
            indice = -1;
            this.valor = valor;
        }

        /* Regresa el índice del indexable. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Define el índice del indexable. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos indexables. */
        @Override public int compareTo(Indexable<T> indexable ) {
            if (valor - indexable.valor > 0.0)
                return 1;
            if (valor - indexable.valor < 0.0)
                return -1;
            return 0;
        }
    }

    /* Método auxiliar recursivo. */
    private static <T extends ComparableIndexable<T>> void
    verificaMonticuloMinimo(MonticuloMinimo<T> monticulo,
                            int i, int n) {
        if (i > n)
            return;
        int izq = 2 * i + 1;
        int der = 2 * i + 2;
        T elemento = monticulo.get(i);
        if (izq < n) {
            T izquierdo = monticulo.get(izq);
            Assert.assertTrue(elemento.compareTo(izquierdo) <= 0);
            verificaMonticuloMinimo(monticulo, izq, n);
        }
        if (der < n) {
            T derecho = monticulo.get(der);
            Assert.assertTrue(elemento.compareTo(derecho) <= 0);
            verificaMonticuloMinimo(monticulo, der, n);
        }
    }

    /* Método que verifica que un montículo mínimo cumpla con sus
     * propiedades. */
    private static <T extends ComparableIndexable<T>> void
    verificaMonticuloMinimo(MonticuloMinimo<T> monticulo) {
        int n = monticulo.getElementos();
        if (n == 0)
            return;
        for (int i = 0; i < n; i++)
            Assert.assertTrue(monticulo.get(i).getIndice() == i);
        verificaMonticuloMinimo(monticulo, 0, n);
    }

    /**
     * Crea un montículo mínimo para cada prueba.
     */
    public TestMonticuloMinimo() {
        random = new Random();
        total = 10 + random.nextInt(90);
        Lista<Indexable<String>> l = new Lista<Indexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            Indexable<String> idx = new Indexable<String>(s, p);
            l.agregaFinal(idx);
        }
        monticulo = new MonticuloMinimo<Indexable<String>>(l);
        verificaMonticuloMinimo(monticulo);
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#agrega}.
     */
    @Test public void testAgrega() {
        for (int i = 0; i < total * 4; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            Indexable<String> idx = new Indexable<String>(s, p);
            monticulo.agrega(idx);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == total + i + 1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina}.
     */
    @Test public void testElimina() {
        while (!monticulo.esVacio()) {
            Indexable<String> a = monticulo.elimina();
            Assert.assertTrue(a.getIndice() == -1);
            for (int i = 0; i < monticulo.getElementos(); i++) {
                Indexable<String> b = monticulo.get(i);
                Assert.assertTrue(a.valor <= b.valor);
            }
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
        try {
            monticulo.elimina();
            Assert.fail();
        } catch (IllegalStateException ise) {}
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#elimina(Object)}.
     */
    @Test public void testEliminaElemento() {
        while (!monticulo.esVacio()) {
            int n = monticulo.getElementos();
            Indexable<String> a = monticulo.get(random.nextInt(n));
            monticulo.elimina(a);
            Assert.assertTrue(a.getIndice() == -1);
            verificaMonticuloMinimo(monticulo);
            Assert.assertTrue(monticulo.getElementos() == --total);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < monticulo.getElementos(); i++) {
            Indexable<String> a = monticulo.get(i);
            Assert.assertTrue(monticulo.contiene(a));
        }
        Indexable<String> a = new Indexable<String>("a", 0);
        Assert.assertFalse(monticulo.contiene(a));
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#esVacio}.
     */
    @Test public void testEsVacio() {
        monticulo = new MonticuloMinimo<Indexable<String>>();
        Assert.assertTrue(monticulo.esVacio());
        String s = Integer.toString(random.nextInt());
        double p = random.nextDouble();
        Indexable<String> idx = new Indexable<String>(s, p);
        monticulo.agrega(idx);
        Assert.assertFalse(monticulo.esVacio());
        idx = monticulo.elimina();
        Assert.assertTrue(monticulo.esVacio());
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#reordena}.
     */
    @Test public void testReordena() {
        int n = monticulo.getElementos();
        for (int i = 0; i < n; i++) {
            verificaMonticuloMinimo(monticulo);
            Indexable<String> idx = monticulo.get(random.nextInt(n));
            idx.valor = idx.valor / 2.0;
            monticulo.reordena(idx);
            verificaMonticuloMinimo(monticulo);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#getElementos}.
     */
    @Test public void testGetElementos() {
        monticulo = new MonticuloMinimo<Indexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            Indexable<String> idx = new Indexable<String>(s, p);
            Assert.assertTrue(monticulo.getElementos() == i);
            monticulo.agrega(idx);
            Assert.assertTrue(monticulo.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#get}.
     */
    @Test public void testGet() {
        try {
            monticulo.get(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            monticulo.get(total);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        monticulo = new MonticuloMinimo<Indexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(random.nextInt());
            double p = random.nextDouble();
            Indexable<String> idx = new Indexable<String>(s, p);
            monticulo.agrega(idx);
            Assert.assertTrue(monticulo.getElementos() == i + 1);
            Assert.assertTrue(monticulo.get(idx.getIndice()) == idx);
        }
    }

    /**
     * Prueba unitaria para {@link MonticuloMinimo#iterator}.
     */
    @Test public void testIterator() {
        monticulo = new MonticuloMinimo<Indexable<String>>();
        for (int i = 0; i < total; i++) {
            String s = Integer.toString(i);
            double p = i;
            Indexable<String> idx = new Indexable<String>(s, p);
            monticulo.agrega(idx);
        }
        int i = 0;
        for (Indexable<String> idx : monticulo)
            Assert.assertTrue(idx.valor == i++);
    }
}
