package proyecto2;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Pintor{

	public static String estructura;
	public static String elementos;
	public static String relacion;

	public void lector(String archivo){
		try{
			FileReader fR = new FileReader(archivo);
			BufferedReader bR = new BufferedReader(fR);
			String dato;

			dato=bR.readLine();
			if(dato!=null){
				estructura=dato;
			}
			else
				throw new IllegalArgumentException("El Archivo esta vacio o no existe");

			dato=bR.readLine();
			System.out.println(dato);
			if(dato!=null){
				elementos=dato;			}
			else
				throw new IllegalArgumentException("Los elementos no fueron especificados");

			dato=bR.readLine();
			if(dato!=null)
				relacion=dato;
			else
				relacion = null;

			bR.close();

		}catch(Exception e){
			System.out.println(e);
		}
	}

	public Lista<String> elementos(){
		Lista<String> el = new Lista<String>();
		int a,b;
		a = 0;
		b = this.elementos.indexOf(",");

		do{
			el.agregaFinal(this.elementos.substring(a,b).trim());
			a = b + 1;
			b = this.elementos.indexOf( ",", b+1);
		}while( b >= 0);
		el.agregaFinal(this.elementos.substring(a,this.elementos.length()));
		return el;
	}

	public Lista<String> separaRelacion(boolean i){
		if (this.relacion==null)
			throw new IllegalArgumentException("Debe introducir una relacion");

		Lista<String> l = new Lista<String>();
		int a,b,c;
		a=0;
		b=this.relacion.indexOf(";");
		String aux;

		do{
			aux=this.relacion.substring(a,b).trim();
			c = aux.indexOf(",");

			if(i){
				l.agregaFinal(aux.substring(0,c).trim());
			}else
				l.agregaFinal(aux.substring(c+1,aux.length()).trim());

			a=b+1;
			b=this.relacion.indexOf(";",b+1);
		}while(b>=0);
		aux=this.relacion.substring(a,relacion.length()).trim();
		c=aux.indexOf(",");

		if(i){
			l.agregaFinal(aux.substring(0,c).trim());
		}else
			l.agregaFinal(aux.substring(c+1,aux.length()).trim());

		return l;
	}

	public static String pinta(String archivo, Pintor pintor){
		String pintura=null;
			pintor.lector(archivo);
			String estructura = pintor.estructura.replace("#","");
			estructura = estructura.toUpperCase().trim();

			//arboles
			if( estructura.equals("ARBOLBINARIOORDENADO") || estructura.equals("ARBOLBINARIOCOMPLETO") || 
				estructura.equals("ARBOLROJINEGRO") || estructura.equals("ARBOLAVL")){

				Lista<String> elementos = pintor.elementos();

				PintaArboles arbol = new PintaArboles(estructura,elementos);
				pintura = arbol.svg();
			}

			//listas
			if(estructura.equals("LISTA")){
				Lista<String> elementos = pintor.elementos();

				PintaLista lista = new PintaLista(elementos);
				pintura = lista.svg();
			}

			//graficas
			if(estructura.equals("GRAFICA")){
				Lista<String> elementos = pintor.elementos();

				Lista<String> conjunto1 , conjunto2;
				conjunto1 = pintor.separaRelacion(true);
				conjunto2 = pintor.separaRelacion(false);

				PintaGraficas grafica = new PintaGraficas(elementos,conjunto1,conjunto2);

				pintura = grafica.svg();
			}

			return pintura;
	}

	public static  String pinta(Pintor pintor){
		String pintura=null;
			String estructura = pintor.estructura.replace("#","");
			estructura = estructura.toUpperCase().trim();

			//arboles
			if( estructura.equals("ARBOLBINARIOORDENADO") || estructura.equals("ARBOLBINARIOCOMPLETO") || 
				estructura.equals("ARBOLROJINEGRO") || estructura.equals("ARBOLAVL")){

				Lista<String> elementos = pintor.elementos();

				PintaArboles arbol = new PintaArboles(estructura,elementos);
				pintura = arbol.svg();
			}

			//listas
			if(estructura.equals("LISTA")){
				Lista<String> elementos = pintor.elementos();

				PintaLista lista = new PintaLista(elementos);
				pintura = lista.svg();
			}

			//graficas
			if(estructura.equals("GRAFICA")){
				Lista<String> elementos = pintor.elementos();

				Lista<String> conjunto1 , conjunto2;
				conjunto1 = pintor.separaRelacion(true);
				conjunto2 = pintor.separaRelacion(false);

				PintaGraficas grafica = new PintaGraficas(elementos,conjunto1,conjunto2);

				pintura = grafica.svg();
			}

			return pintura;
	}

	public static void main(String[] args){
		Pintor pintor = new Pintor();

		if(args.length!=0){
			
			System.out.println(pinta(args[0],pintor));

		}else{
			try{
				BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
				String dato;

				dato=bR.readLine();
				if(dato!=null){
					pintor.estructura=dato;
				}
				else
					throw new IllegalArgumentException("El Archivo esta vacio o no existe");

				dato=bR.readLine();
				if(dato!=null){
					pintor.elementos=dato;			}
				else
					throw new IllegalArgumentException("Los elementos no fueron especificados");

				dato=bR.readLine();
				if(dato!=null)
					pintor.relacion=dato;
				else
					pintor.relacion = null;

				bR.close();

			}catch(Exception e){
				System.out.println(e);
			}

			System.out.println(pinta(pintor));

		}

	}

}