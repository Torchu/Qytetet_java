package modeloqytetet;
/**
 *
 * @author torchu
 */
public class Calle extends Casilla{
    TituloPropiedad titulo;
    
    Calle(int numeroCasilla, TituloPropiedad titulo){
        super(numeroCasilla);
        super.setCoste(titulo.getPrecioCompra());
        this.titulo = titulo;
    }
    
    public TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    }
    
    @Override
    protected TipoCasilla getTipo(){
        return TipoCasilla.CALLE;
    }
    
    @Override
    protected TituloPropiedad getTitulo(){
        return titulo;
    }
    
    int pagarAlquiler(){
        return titulo.pagarAlquiler();
    }
    
    private void setTitulo(TituloPropiedad titulo) {
        this.titulo = titulo;
    }
    
    @Override
    protected boolean soyEdificable(){
        return true;
    }
    
    boolean tengoPropietario(){
        return titulo.tengoPropietario();
    }
    
    @Override
    public String toString() {
        return super.toString() + ", tipo= Calle, titulo= " + titulo + "}";
    }
}
