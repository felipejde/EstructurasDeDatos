package mx.unam.ciencias.edd;

/**
 * Clase para fabricar generadores de huellas digitales.
 */
public class FabricaHuellasDigitales {

    /**
     * Regresa una instancia de {@link HuellaDigital} para cadenas.
     * @param algoritmo el algoritmo de huella digital que se desea.
     * @return una instancia de {@link HuellaDigital} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador no
     *         reconocido.
     */

    public static final int BJ_STRING   = 0;
    /**
     * Identificador para fabricar la huella digital de GLib para
     * cadenas.
     */
    public static final int GLIB_STRING = 1;
    /**
     * Identificador para fabricar la huella digital de XOR para
     * cadenas.
     */
    public static final int XOR_STRING  = 2;

    public static HuellaDigital<String> getInstancia(AlgoritmoHuellaDigital algoritmo) {
        switch(algoritmo){
            case BJ_STRING: return huellaBJ();
            case GLIB_STRING: return huellaDigitalGLIB();
            case XOR_STRING: return huellaDigitalXOR();
            default : throw new IllegalArgumentException();
        }
    }

    private static HuellaDigital<String> huellaBJ(){
        return new HuellaDigital<String>(){

            private long a;
            private long b;
            private long c;

            public int huellaDigital(String cadena) {
                int  hd = (int) hash(cadena.getBytes());
                return hd;
            }

            private int hash(byte [] k) {
                int longitud = k.length;
                a = 0x000000009e3779b9L;
                b = a;
                c = 0xffffffff;
                int i = 0;

                while (longitud >= 12) {
                    a += ( k[i] + ( k[i + 1] <<8 ) + ( k[i + 2 ] << 16 ) + ( k[i + 3] << 24 ) );
                    b += ( k[i + 4] + ( k[i + 5] << 8 ) + ( k[i + 6] << 16 ) + ( k[i + 7] << 24 ) );
                    c += ( k[i + 8] + ( k[i + 9] << 8 ) + ( k[i + 10] << 16 ) + ( k[i + 11] << 24 ) );
                    mezcla();
                    i += 12;
                    longitud -= 12;
                }

                c += k.length;
                switch( longitud ) {
                    case 11: c = mas( c, izq( getLongitudByte( k[i + 10] ), 24 ) );
                    case 10: c = mas(c, izq( getLongitudByte( k[i + 9] ), 16 ) );
                    case 9: c = mas( c, izq( getLongitudByte( k[i + 8] ), 8 ) ); 
                    case 8: b = mas( b, izq( getLongitudByte( k[i + 7] ), 24 ) );
                    case 7: b = mas( b, izq( getLongitudByte( k[i + 6] ), 16 ) );
                    case 6: b = mas( b, izq(getLongitudByte( k[i + 5] ), 8 ) );
                    case 5: b = mas( b, ( k[i + 4] ) );
                    case 4: a = mas( a, izq(getLongitudByte( k[i +3] ), 24 ) );
                    case 3: a = mas( a, izq(getLongitudByte( k[i +2] ), 16 ) );
                    case 2: a = mas( a, izq(getLongitudByte( k[i + 1] ), 8 ) );
                    case 1: a = mas( a, ( k[i + 0] ) );
                }

                mezcla();
                return ( int ) ( c & 0xffffffff );
            }

            private long getLongitudByte(byte b) {
                long num = b & 0x7F;
                return num;
            }

            private void mezcla() {
                a = menos( a, b ); a = menos( a, c); a = xor( a, c >> 13 );
                b = menos( b, c ); b = menos( b, a); b = xor( b, izq( a, 8 ) );
                c = menos( c, a ); c = menos(c, b ); c = xor( c, ( b >> 13 ) );
                a = menos( a, b ); a = menos( a, c ); a = xor( a, ( c >> 12 ) );
                b = menos( b, c ); b = menos( b, a ); b = xor( b, izq( a, 16 ) );
                c = menos( c, a ); c = menos( c, b ); c = xor( c, ( b >> 5 ) );         
                a = menos( a, b ); a = menos(a, c); a = xor( a, ( c >> 3 ) );
                b = menos( b, c ); b = menos( b, a); b = xor( b, izq( a, 10 ) );
                c = menos( c, a ); c = menos( c, b); c = xor( c, ( b >> 15 ) );
            }

            private long izq(long a, int b) {
                return (a<<b) & 0x00000000ffffffffL;
            }

            private long menos(long a, long b) {
                return (a-b) & 0x00000000ffffffffL;
            }

            private long xor(long a, long b) {
                return (a^b) & 0x00000000ffffffffL;
            }

            private long mas(long a, long b) {
                return (a+b) & 0x00000000ffffffffL;
            }
        };
    }

    
    private static HuellaDigital<String> huellaDigitalXOR(){
        return new HuellaDigital<String>(){
            public int huellaDigital(String s){
                byte[] k = s.getBytes();
                int r = 0;
                int i = 0;
                int l = k.length;

                while(l>=4){
                    r^=(k[i]<<24)|(k[i+1]<<16)|(k[i+2]<<8)|k[i+3];
                    i+=4;
                    l-=4;
                }

                int t = 0;
                switch(l){
                    case 3: t|= k[i+2]<<8;
                    case 2: t|= k[i+1]<<16;
                    case 1: t|= k[i]<<24;
                }
                r^=t;
                return r;
            }
        };
    }
    
    private static HuellaDigital<String> huellaDigitalGLIB(){
        return new HuellaDigital<String>(){
            public int huellaDigital(String s){
                byte[] k = s.getBytes();
                int h = 5381;
                for(int i=0;i<k.length;i++){
                    char c = (char)k[i];
                    h=h*33+c;
                }
                return h;
            }
        };
    }

}

