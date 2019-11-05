package modeloqytetet;

public class Especulador extends Jugador{
    private int fianza;
    protected Especulador(Jugador jugador, int fianza){
        super(jugador);
        this.fianza=fianza;
    }
    @Override
    protected void pagarImpuesto(){
        modificarSaldo(-getCasillaActual().getCoste()/2);
    }
    protected boolean deboIrACarcel(){
        if(super.deboIrACarcel())
            return !pagarFianza();
        else
            return false;
    }
    protected Especulador convertirme(int fianza){
        return this;
    }
    private boolean pagarFianza(){
        if(getSaldo()>fianza){
            modificarSaldo(-fianza);
            return true;
        }
        else
            return false;
    }
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        if(titulo.getNumCasas()<8 && tengoSaldo(titulo.getPrecioEdificar()))
            return true;
        else
            return false;
    }
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        if(titulo.getNumHoteles()<8 && tengoSaldo(titulo.getPrecioEdificar()) && titulo.getNumCasas()>=4)
            return true;
        else
            return false;
    }
    @Override
    public String toString(){
        return "Especulador: fianza= " + fianza + " " + super.toString();
    }
}
