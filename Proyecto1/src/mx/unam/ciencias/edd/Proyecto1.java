package mx.unam.ciencias.edd;

import java.io.*;

public class Proyecto1{
    
    private Lista<String> lineas;
    
    public static void main(String[] args){
    	
    	try{
    		
    		Proyecto1 project = new Proyecto1();
        	project.lineas = new Lista<String>();
    		
        	if (args.length > 0){
        		for(int i=0, sz = args.length; i<sz; i++){
    				project.nuevaLista(args[i]);
    				project.lineas=Lista.mergeSort(project.lineas);
		    }
			    
        		project.listarTodosLosRenglones();
	    		
        		
    			
    		}else{
    			String direccion = project.recibeNombre();
    			project.nuevaLista(direccion);
    			project.lineas = Lista.mergeSort(project.lineas);
    			project.listarTodosLosRenglones();	
    		}
    			
		}catch(ArrayIndexOutOfBoundsException a){
	    	System.out.println("Argumentos insuficientes");
		}
	}
    
   
    public String recibeNombre(){
    	BufferedReader bfrd = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println ("Escribe el nombre del archivo");
    	String aux = null;
    	try {
    		aux = bfrd.readLine();
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    	return aux;
    }
    
    
    public void nuevaLista(String ubicacion){
       	try{
	    	BufferedReader bfrd= new BufferedReader(new FileReader(ubicacion));
	    	String bufer;
	    	while((bufer=bfrd.readLine()) != null){
				bufer=bufer.toLowerCase();
				lineas.agrega(bufer);
				
	    	}
		}catch(Exception e){
		    System.out.println("Archivo no encontrado");
		}
    }
    
    
    public void listarTodosLosRenglones() {
        for (String n: lineas) {
	    	System.out.println (n); 
		}
    }
}

