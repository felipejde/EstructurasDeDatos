package proyecto2;

public class PintaGraficas{

	String svg;
	Grafica<String> grafica = new Grafica<String>();
	int orden;
	Lista<String> conjunto1;
	Lista<String> conjunto2;
	Lista<String> elementos;

	public PintaGraficas(Lista<String> elementos, Lista<String> conjunto1, Lista<String> conjunto2){
		for( String e : elementos ){
			grafica.agrega(e);
		}
		if( conjunto1!=null && conjunto2!=null)
			for(int i=0;i<conjunto1.getLongitud();i++)			
				grafica.conecta(conjunto1.get(i).trim(),conjunto2.get(i));

		this.elementos = elementos;
		orden = elementos.getLongitud();
		this.conjunto1 = conjunto1;
		this.conjunto2 = conjunto2;
	}

	public String svg(){
		svg = "<?xml version='1.0' encoding='UTF-8' ?>\n";
		svg += "<svg height=\"800\" width=\"800\">\n\t<g>\n";
		double puntos = Math.toRadians((double) 360 / orden);
		Lista<String> puntosx = new Lista<String>();
		Lista<String> puntosy = new Lista<String>();
		String aux = null;
		for(int i=1; i<=orden; i++){
			aux = Double.toString( 400 + 280 * Math.cos( i * puntos ) );
			aux = aux.substring(0,3);
			puntosx.agregaFinal( "\"" + aux + "\"" );
			aux = Double.toString( 400 + 280 * Math.sin( i * puntos ) );
			aux = aux.substring(0,3);
			puntosy.agregaFinal( "\"" + aux + "\"");
		}
		if(orden>1){

			int i1 = 0;
			int i2 = 0;
			
			for(int i=0;i<conjunto1.getLongitud();i++){
				i1 = elementos.indiceDe(conjunto1.get(i));
				i2 = elementos.indiceDe(conjunto2.get(i));
				svg += "<line x1=" + puntosx.get(i1) + " y1=" + puntosy.get(i1) + " x2=";
				svg += puntosx.get(i2);
				svg += " y2="+ puntosy.get(i2)  + " stroke=\"black\" ";
				svg += " stroke-width=\"1\"/>\n";
			}

			int diferencia = diferencia();
			for(int i=0; i<orden; i++){
				svg += "<circle cx=" + puntosx.get(i) + " cy=" + puntosy.get(i) + " r=\"" + Integer.toString(reduccion(diferencia,200));
				svg += "\" stroke=\"black\" " + "stroke-width=\"2\" fill=\"white\" />\n";
			}

			for( int i = 0; i < orden; i ++  ){
				svg += "<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=";
				svg += puntosx.get(i) + " y=" + puntosy.get(i) + " text-anchor=\"middle\" >";
				svg += elementos.get(i).toString();
				svg += "</text>\n";
			}

		}else{

			svg += "<circle cx=\"400\" cy=\"400\" r=\"400\"";
			svg += " stroke=\"black\" " + "stroke-width=\"3\" fill=\"white\" />\n";
			svg += "<text fill=\"black\" font-family=\"sans-serif\" font-size=\"20\" x=\"400\"";
			svg += " y=\"400\""  + " text-anchor=\"middle\" >";
			svg += elementos.get(0).toString();
			svg += "</text>\n";
		}

		svg += "\t</g>\n</svg>";

		return svg;
	}

	private int diferencia(){
		if(orden==1)
			return 1;
		int i=1;
		int a=2;
		while(a<orden){
			a = (int)Math.pow(a,i);
			i++;
		}
		return i;
	}

	private int reduccion(int b, int c){
		if ( b == 0 )
			return c;
		c = c / 2;
		return reduccion( b -1, c );
	}
}