package modeloqytetet;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class Tablero {
    //Atributos
    private ArrayList<Casilla> casillas;
    private Casilla carcel;
    
    //Constructor (llama al metodo inicializar)
    public Tablero(){
        inicializar();
    }

    boolean esCasillaCarcel(int numeroCasilla){
        return (numeroCasilla==carcel.getNumeroCasilla());
    }
    
    //Getters
    Casilla getCarcel() {
        return carcel;
    }    
    
    ArrayList<Casilla> getCasillas() {
        return casillas;
    }
    
    //Inicializa todas las casillas del tablero y hace que carcel apunte a la casilla de la carcel
    private void inicializar(){
        casillas = new ArrayList<>();
        casillas.add(new OtraCasilla(0,TipoCasilla.SALIDA));
        casillas.add(new Calle(1, new TituloPropiedad("The Ramones", 500, 250, 10, 50, 250)));
        casillas.add(new Calle(2, new TituloPropiedad("Bon Jovi", 500, 250, 10, 50, 250)));
        casillas.add(new OtraCasilla(3,TipoCasilla.SORPRESA));
        casillas.add(new Calle(4, new TituloPropiedad("Led Zeppelin", 550, 300, 11, 55, 275)));
        casillas.add(new OtraCasilla(5,TipoCasilla.CARCEL));
        casillas.add(new Calle(6, new TituloPropiedad("Queen", 650, 400, 13, 65, 325)));
        casillas.add(new Calle(7, new TituloPropiedad("ACDC", 650, 400, 13, 65, 325)));
        casillas.add(new OtraCasilla(8,TipoCasilla.IMPUESTO));
        casillas.add(new Calle(9, new TituloPropiedad("Iron Maiden", 700, 450, 14, 70, 350)));
        casillas.add(new OtraCasilla(10,TipoCasilla.PARKING));
        casillas.add(new Calle(11, new TituloPropiedad("Metallica", 800, 550, 16, 80, 400)));
        casillas.add(new Calle(12, new TituloPropiedad("Warcry", 800, 550, 16, 80, 400)));
        casillas.add(new OtraCasilla(13,TipoCasilla.SORPRESA));
        casillas.add(new Calle(14, new TituloPropiedad("Crazy Lixx", 850, 600, 17, 85, 425)));
        casillas.add(new OtraCasilla(15,TipoCasilla.JUEZ));
        casillas.add(new Calle(16, new TituloPropiedad("Scorpions", 950, 700, 19, 95, 475)));
        casillas.add(new Calle(17, new TituloPropiedad("Van Halen", 950, 700, 19, 95, 475)));
        casillas.add(new OtraCasilla(18,TipoCasilla.SORPRESA));
        casillas.add(new Calle(19, new TituloPropiedad("Guns N Roses", 1000, 750, 20, 100, 500)));
        carcel=casillas.get(5);
    }
    
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento){
        return obtenerCasillaNumero((casilla.getNumeroCasilla()+desplazamiento)%casillas.size());
    }
    
    Casilla obtenerCasillaNumero(int numeroCasilla){
        return casillas.get(numeroCasilla);
    }
    
    //Metodo toString
    @Override
    public String toString() {
        return "Tablero{casillas=" + casillas + ", carcel=" + carcel + '}';
    }
}
