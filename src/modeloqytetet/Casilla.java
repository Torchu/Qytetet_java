package modeloqytetet;
public abstract class Casilla {
    //Atributos
    private int coste;                  //Precio de compra (si es una calle)
    private int numeroCasilla=0;          //Numero de la casilla
    
    //Constructor para casillas
    public Casilla(int numeroCasilla){
        this.numeroCasilla=numeroCasilla;
    }
    
    //Getters
    int getCoste() {
        return coste;
    }
    
    int getNumeroCasilla() {
        return numeroCasilla;
    }

    protected abstract TipoCasilla getTipo();

    protected abstract TituloPropiedad getTitulo();
    
    public void setCoste(int coste){
        this.coste = coste;
    }
    
    protected abstract boolean soyEdificable();
    
    //Metodo toString
    @Override
    public String toString() {
        return "Casilla{" + "numeroCasilla=" + numeroCasilla + ", coste=" + coste;
    }
}
