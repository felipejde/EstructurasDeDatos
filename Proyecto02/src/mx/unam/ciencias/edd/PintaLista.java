package mx.unam.ciencias.edd.proyecto2;

public class PintaLista{

	Lista<String> lista;
	String svg = null;

	public PintaLista(Lista<String> elementos){
		lista=elementos.copia();
	}

	public String svg(){
		svg += "<?xml version='1.0' encoding='UTF-8' ?>\n";
		svg += "<svg " + "width= '" +lista.getLongitud()*100+ "' " +
				" height= '" +40+"' " + ">\n\t<g>\n";
		nodoSvg();

		return svg;
	}

	private void nodoSvg(){
		int x1,x2;

		x1 = 10;
		x2 = 60;
		for (String e : lista){
			svg +=  "<polygon points=\"" + x1 + ",10 " + x2 + ",10 ";
			svg +=  x2 + ",30 " + x1 + ",30\" ";
			svg += "style=\"fill:white;stroke:black;stroke-width:1\" />\n";
			x1 += 100;
			x2 += 100;
		}
		x1 = 65;
		x2 = 105;
		for (int i=0;i<lista.getLongitud()-1;i++){
			svg += "<line x1=\"" + x1 + "\" y1=\"20\" x2=\"" + x2;
			svg += "\" y2=\"20\" stroke=\"black\" stroke-width=\"2\" />\n";
			svg += "<polygon points=\"" + (x1-5) + ",20 " + (x1+5) + ",15 ";
			svg += (x1+5) + ", 25\" style=\"fill:black,stroke:black;stroke-width:1\" />\n";
			svg += "<polygon points=\"" + (x2+5) + ",20 " + (x2-5) + ",15 ";
			svg += (x2-5) + ", 25\" style=\"fill:black,stroke:black;stroke-width:1\" />\n";
			x1 += 100;
			x2 += 100;
		}
		x1 = 25;
		for(int i=0;i<lista.getLongitud();i++){
			System.out.println(i);
			svg += "<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=";
			svg += "\"" + x1 + "\" y=\"22\" text-anchor=\"middle\" >";
			svg += lista.get(i).toString();
			svg += "</text>\n";
			x1 += 100;
		}
	}

}