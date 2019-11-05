package modeloqytetet;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class Jugador implements Comparable{
    private boolean encarcelado=false;
    private String nombre;
    private int saldo=7500;
    private Sorpresa cartaLibertad;
    private Casilla casillaActual;
    private ArrayList<TituloPropiedad> propiedades= new ArrayList<>();
    
    public Jugador(String nombre){
        this.nombre=nombre;
    }
    
    public Jugador(Jugador jugador){
        this.encarcelado=jugador.encarcelado;
        this.nombre=jugador.nombre;
        this.saldo=jugador.saldo;
        this.cartaLibertad=jugador.cartaLibertad;
        this.casillaActual=jugador.casillaActual;
        this.propiedades=jugador.propiedades;
    }
    
    boolean cancelarHipoteca(TituloPropiedad titulo){
        int costeCancelar=titulo.calcularCosteCancelar();
        if(saldo>costeCancelar){
            modificarSaldo(-costeCancelar);
            titulo.cancelarHipoteca();
            return true;
        }
        else
            return false;
    }
    
    boolean comprarTituloPropiedad(){
        boolean comprado=false;
        int costeCompra=casillaActual.getCoste();
        if(costeCompra<saldo){
            comprado=true;
            TituloPropiedad titulo=((Calle)casillaActual).asignarPropietario(this);
            propiedades.add(titulo);
            modificarSaldo(-costeCompra);
        }
        return comprado;
    }
    
    protected Especulador convertirme(int fianza){
        Especulador esp = new Especulador(this, fianza);
        return esp;
    }
    
    int cuantasCasasHotelesTengo(){
        int numCasasHoteles=0;
        for(TituloPropiedad p: propiedades)
            numCasasHoteles+=(p.getNumCasas()+p.getNumHoteles());
        return numCasasHoteles;
    }
    
    protected boolean deboIrACarcel(){
        return !tengoCartaLibertad();
    }
    
    boolean deboPagarAlquiler(){
        TituloPropiedad titulo=casillaActual.getTitulo();
        if(!esDeMiPropiedad(titulo) && titulo.tengoPropietario() && !titulo.propietarioEncarcelado() && !titulo.getHipotecada())
            return true;
        else
            return false;
    }
    
    Sorpresa devolverCartaLibertad(){
        Sorpresa aux=cartaLibertad;
        cartaLibertad=null;
        return aux;
    }
    
    boolean edificarCasa(TituloPropiedad titulo){
        if(puedoEdificarCasa(titulo)){
            titulo.edificarCasa();
            modificarSaldo(-titulo.getPrecioEdificar());
            return true;
        }
        else
            return false;
    }
    
    boolean edificarHotel(TituloPropiedad titulo){
        if(puedoEdificarHotel(titulo)){
            titulo.edificarHotel();
            modificarSaldo(-titulo.getPrecioEdificar());
            return true;
        }
        else
            return false;
    }
    
    private void eliminarDeMisPropiedades(TituloPropiedad titulo){
        propiedades.remove(titulo);
        titulo.setPropietario(null);
    }
    
    private boolean esDeMiPropiedad(TituloPropiedad titulo){
        return propiedades.contains(titulo);
    }
    
    boolean estoyEnCalleLibre(){
        return !encarcelado;
    }

    //Getters
    public Sorpresa getCartaLibertad(){
        return cartaLibertad;
    }

    public Casilla getCasillaActual(){
        return casillaActual;
    }
    
    public boolean getEncarcelado(){
        return encarcelado;
    }

    public String getNombre(){
        return nombre;
    }

    public ArrayList<TituloPropiedad> getPropiedades(){
        return propiedades;
    }

    public int getSaldo(){
        return saldo;
    }
    
    void hipotecarPropiedad(TituloPropiedad titulo){
        int costeHipoteca=titulo.hipotecar();
        modificarSaldo(costeHipoteca);
    }
    
    void irACarcel(Casilla casilla){
        setCasillaActual(casilla);
        setEncarcelado(true);
    }
    
    int modificarSaldo(int cantidad){
        saldo+=cantidad;
        return saldo;
    }
    
    int obtenerCapital(){
        int capital=saldo;
        for(TituloPropiedad p: propiedades){
            capital+=(p.getPrecioCompra()+cuantasCasasHotelesTengo()*p.getPrecioEdificar());
            if(p.getHipotecada())
                capital-=p.getHipotecaBase();
        }
        return capital;
    }
    
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada){
        ArrayList<TituloPropiedad>aux=new ArrayList<>();
        for(TituloPropiedad p: propiedades)
            if(p.getHipotecada()==hipotecada)
                aux.add(p);
        return aux;
    }
    
    void pagarAlquiler(){
        int costeAlquiler=((Calle)casillaActual).pagarAlquiler();
        modificarSaldo(-costeAlquiler);
    }
    
    protected void pagarImpuesto(){
        saldo-=casillaActual.getCoste();
    }
    
    void pagarLibertad(int cantidad){
        boolean tengoSaldo=tengoSaldo(cantidad);
        if(tengoSaldo){
            setEncarcelado(false);
            modificarSaldo(-cantidad);
        }
    }
    
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        if(titulo.getNumCasas()<4 && tengoSaldo(titulo.getPrecioEdificar()))
            return true;
        else
            return false;
    }
    
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        if(titulo.getNumHoteles()<4 && tengoSaldo(titulo.getPrecioEdificar()) && titulo.getNumCasas()>=4)
            return true;
        else
            return false;
    }
    
    //Setters
    void setCartaLibertad(Sorpresa cartaLibertad) {
        this.cartaLibertad = cartaLibertad;
    }

    void setCasillaActual(Casilla casillaActual) {
        this.casillaActual = casillaActual;
    }
    
    void setEncarcelado(boolean encarcelado) {
        this.encarcelado = encarcelado;
    }
    
    boolean tengoCartaLibertad(){
        return (cartaLibertad!=null);
    }
    
    protected boolean tengoSaldo(int cantidad){
        return saldo>cantidad;
    }
    
    void venderPropiedad(Casilla casilla){
        TituloPropiedad titulo=casilla.getTitulo();
        eliminarDeMisPropiedades(titulo);
        int precioVenta=titulo.calcularPrecioVenta();
        modificarSaldo(precioVenta);
    }
    
    @Override
    public String toString(){
        return "Jugador{nombre= " + nombre + ", casilla actual= " + casillaActual.getNumeroCasilla() + ", saldo= " + saldo + ", encarcelado= " + encarcelado + ", capital= " + obtenerCapital() + ", cartaLibertad= " + cartaLibertad + ", propiedades= " + propiedades + "}";
    }

    @Override
    public int compareTo(Object otroJugador) {
        int otroCapital=((Jugador) otroJugador).obtenerCapital();
        return otroCapital-obtenerCapital();
    }
}
