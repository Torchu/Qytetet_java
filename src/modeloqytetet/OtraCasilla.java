package modeloqytetet;
/**
 *
 * @author torchu
 */
public class OtraCasilla extends Casilla{
    TipoCasilla tipo;
    OtraCasilla(int numeroCasilla, TipoCasilla tipo){
        super(numeroCasilla);
        super.setCoste(0);
        this.tipo=tipo;
    }
    
    @Override
    protected TipoCasilla getTipo(){
        return tipo;
    }
    
    @Override
    protected boolean soyEdificable(){
        return false;
    }
    
    @Override
    protected TituloPropiedad getTitulo(){
        return null;
    }
    
    //Metodo toString
    @Override
    public String toString() {
        return super.toString() + ", tipo= " + tipo + "}";
    }
}