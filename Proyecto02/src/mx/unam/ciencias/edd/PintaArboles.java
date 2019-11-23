package mx.unam.ciencias.edd.proyecto2;

public class PintaArboles{

	private ArbolBinario<String> arbol;
	private String tipo;
	private int profundidad;
	private String svg=null;

	public PintaArboles(String tipo,Lista<String> elementos){
		this.tipo = tipo;
		this.tipo = this.tipo.toUpperCase().trim();
		switch(this.tipo){
			case "ARBOLBINARIOORDENADO" :
				arbol = new ArbolBinarioOrdenado<String>();
				break;
			case "ARBOLBINARIOCOMPLETO" :
				arbol = new ArbolBinarioCompleto<String>();
				break;
			case "ARBOLAVL" :
				arbol = new ArbolAVL<String>();
				break;
			case "ARBOLROJINEGRO" :
				arbol = new ArbolRojinegro<String>();
				break;
			default:
				throw new IllegalArgumentException("Tipo de Arbol Invalido");
		}

		for (String s : elementos)
			arbol.agrega(s);

		profundidad = arbol.profundidad();
	}

	public String svg(){
		svg = "<?xml version='1.0' encoding='UTF-8' ?>\n";
		String espacio= Integer.toString((int)Math.pow(2,(profundidad+1))*100); 
		svg += "<svg width = '" + espacio + "' height = '" + espacio + "'>\n\t<g>\n";

		Lista<Integer> height = new Lista<Integer>();
		
		svgVertice(arbol.raiz(),Integer.parseInt(espacio)/2+50,40,1,height);

		Lista.mergeSort(height);

		svg = svg.replace("height = '" + espacio, "height = '" + Integer.toString(height.eliminaUltimo()+100));
		svg += "\t</g>\n</svg>";

		return svg;
	}

	private void svgVertice( VerticeArbolBinario vertice, int x, int y, int z,Lista<Integer> height){
		height.agregaFinal(y);
		String x1= "\"" + x + "\"";
		String y1= "\"" + y + "\""; 
		z++;

		int a,b;

		if (vertice.hayIzquierdo()){
			a = x-(int)Math.pow(2,profundidad+1-z)*100;
			b = y+100;
			svg += "<line x1=" + x1 + " y1=" + y1 + " x2=\"" + a + "\"";
			svg += " y2=\"" + b + "\"" + " stroke=\"black\" ";
			svg += " stroke-width=\"5\"/>\n";
			svgVertice(vertice.getIzquierdo(),a,b,z,height);
		}
		
		if (vertice.hayDerecho()){
			a = x+(int)Math.pow(2,profundidad+1-z)*100;
			b = y+100;
			svg += "<line x1=" + x1 + " y1=" + y1 + " x2=\"" + a + "\"";
			svg += " y2=\"" + b + "\"" + " stroke=\"black\" ";
			svg += " stroke-width=\"5\"/>\n";
			svgVertice(vertice.getDerecho(),a,b,z,height);
		}

		svg += "<circle cx=" + x1 + " cy=" + y1 + " r=\"" + "20\" ";
		svg += "stroke=\"black\" " + "stroke-width=\"3\" fill=\"";

		if(tipo.equals("ARBOLROJINEGRO"))
			pintaRN((ArbolRojinegro.VerticeRojinegro) vertice);

		svg += "\" />\n";
		svg += "<text fill=\"white\" font-family=\"sans-serif\" font-size=\"20\" x=";
		svg += x1 + " y=" + y1 + " text-anchor=\"middle\" >" + vertice.get().toString();
		svg += "</text>\n";
	}

	public void pintaRN(ArbolRojinegro.VerticeRojinegro v){
		if(v.color.equals(Color.NEGRO))
				svg += "black";
			else
				svg+= "red";
	}


}