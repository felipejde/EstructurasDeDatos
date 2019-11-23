package mx.unam.ciencias.edd;

/**
 * Clase para manipular arreglos genéricos de elementos comparables.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] a) {
        // Aquí va su código.
    	 quickSort(a, 0, a.length-1);
    }
    
    private static <T extends Comparable<T>> void quickSort(T[] a, int ini, int fin) {
        if(ini>=fin){
            return;
        }
        int i=ini+1;
        int j=fin;
        while(i<j){
            if(a[i].compareTo(a[ini])>0&&a[j].compareTo(a[ini])<=0){
                intercambia(a,i++,j--);
            }else{
                if(a[i].compareTo(a[ini])<=0){
                    i++;
                }else{
                    j--;
                }
            }
        }
        if(a[i].compareTo(a[ini])>0){
            i--;
        }
        intercambia(a,ini,i);
        quickSort(a,ini,i-1);
        quickSort(a,i+1,fin);
    }
    
    private static <T extends Comparable<T>> void intercambia(T[] a, int i, int j){
        if(i==j){
            return;
        }else{
            T t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] a) {
        // Aquí va su código.
    	 for(int i = 0; i < a.length -1; i++){
    	        int s = i;
    	        for(int j = i + 1; j < a.length; j++)
    	            if(a[s].compareTo((a[j])) > 0  )
    	            s = j;
    	        T c= a[s];
    	        a[s] = a[i];
    	        a[i] = c;
    	        }
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e) {
        // Aquí va su código.
    int i = 0, j = a.length-1;
        
        while(j>=i){
            int p = (i+j)/2;
            if(a[p].compareTo(e)==0)
                return p;
            else 
                if (a[p].compareTo(e)<0)
                    i = p+1;
            else 
                j = p-1;
        }
        return -1;
    }
}
