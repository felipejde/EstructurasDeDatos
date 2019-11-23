package mx.unam.ciencias.edd;
//import mx.unam.ciencias.edd.*;
import java.io.*;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 2: Pilas, colas, ordenamientos y búsquedas.
 */
public class Proyecto1 {
public static String Buffer(){
BufferedReader lector=new BufferedReader(new InputStreamReader(System.in));
System.out.println("Da el nombre del archivo");
String datos=null;
try {
datos=lector.readLine();

}catch(IOException e){
	e.printStackTrace();
}
return datos;

 }

public static Lista<String> leerTxt(String direccion){
Lista<String> lista = new Lista<String>();
try{
BufferedReader bf=new BufferedReader(new FileReader(direccion));
String bfRead;
while((bfRead=bf.readLine())!=null){
lista.agrega(bfRead);}

}catch (Exception e) {
System.out.println("No se encontro el archivo");	
}
return lista;}	





    public static void main(String[] args) {
	Lista<String> lista = new Lista<String>();
    BufferedReader lector=new BufferedReader(new InputStreamReader(System.in));
	String bfRead=null;
	String prueba=null;
	try{
	System.out.println("Da el nombre del archivo");
	bfRead=lector.readLine();
	prueba=bfRead.substring((bfRead.length())-4);
    }catch(IOException e){	e.printStackTrace();	}
	if (!prueba.equals(".txt")) {
		try{
	bfRead=lector.readLine();
		while((bfRead=lector.readLine())!=null){
	lista.agrega(bfRead);}	
		lista=Lista.mergeSort(lista);
	IteradorLista<String> it=lista.iteradorLista();
	while(it.hasNext()){
    	System.out.println(it.next());}
    }catch(IOException e){	e.printStackTrace();	}

	}else {
    String direccion=bfRead;
	System.out.println(direccion);
	lista=leerTxt(direccion);
	lista=Lista.mergeSort(lista);
	IteradorLista<String> it=lista.iteradorLista();
	while(it.hasNext()){

    	System.out.println(it.next());
}
}


    }
}
