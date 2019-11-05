package modeloqytetet;
/**
 *
 * @author root
 */
public class TituloPropiedad {
    //Atributos
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private boolean hipotecada;
    private String nombre;
    private int numCasas=0;
    private int numHoteles=0;
    private int precioCompra;
    private int precioEdificar;
    private Jugador propietario;
    
    //Constructor (Default: hipotecada=false, numCasas=0 & numHoteles=0)
    public TituloPropiedad(String nombre, int precioCompra, int alquilerBase, float factorRevalorizacion, int hipotecaBase, int precioEdificar){
        this.nombre=nombre;
        this.hipotecada=false;
        this.precioCompra=precioCompra;
        this.alquilerBase=alquilerBase;
        this.factorRevalorizacion=factorRevalorizacion;
        this.hipotecaBase=hipotecaBase;
        this.precioEdificar=precioEdificar;
    }
    
    int calcularCosteCancelar(){
        return (int) (calcularCosteHipotecar()*1.1);
    }
    
    int calcularCosteHipotecar(){
        return (int) (hipotecaBase*(1+numCasas/2+numHoteles*2));
    }
    
    int calcularImporteAlquiler(){
        return (int) (alquilerBase+(numCasas/2+numHoteles*2));
    }
    
    int calcularPrecioVenta(){
        return (int) (precioCompra+(numCasas+numHoteles)*precioEdificar*factorRevalorizacion);
    }
    
    void cancelarHipoteca(){
        hipotecada=false;
    }
    
    void edificarCasa(){
        numCasas++;
    }
    
    void edificarHotel(){
        numCasas-=4;
        numHoteles++;
    }
    
    //Getters
    int getAlquilerBase(){
        return alquilerBase;
    }
    
    float getFactorRevalorizacion(){
        return factorRevalorizacion;
    }
    
    int getHipotecaBase(){
        return hipotecaBase;
    }
    
    boolean getHipotecada(){
        return hipotecada;
    }
    
    String getNombre(){
        return nombre;
    }
    
    int getNumCasas(){
        return numCasas;
    }
    
    int getNumHoteles(){
        return numHoteles;
    }
    
    int getPrecioCompra(){
        return precioCompra;
    }
    
    int getPrecioEdificar(){
        return precioEdificar;
    }

    Jugador getPropietario() {
        return propietario;
    }
    
    int hipotecar(){
        setHipotecada(true);
        return calcularCosteHipotecar();
    }
    
    int pagarAlquiler(){
        int costeAlquiler=calcularImporteAlquiler();
        propietario.modificarSaldo(costeAlquiler);
        return costeAlquiler;
    }
    
    boolean propietarioEncarcelado(){
        return propietario.getEncarcelado();
    }
    
    //Setter de hipotecada
    void setHipotecada(boolean hipotecada){
        this.hipotecada=hipotecada;
    }
    
    void setPropietario(Jugador propietario){
        this.propietario=propietario;
    }
    
    boolean tengoPropietario(){
        return (propietario!=null);
    }
    
    //Metodo toString
    @Override
    public String toString(){
        if(tengoPropietario())
            return "nombre= " + nombre + ", propietario= " + propietario.getNombre() + ", hipotecada= " + hipotecada + ", alquiler= " + Integer.toString(alquilerBase) + ", casas= " + Integer.toString(numCasas) + ", hoteles= " + Integer.toString(numHoteles)+ ", hipoteca= " + Integer.toString(hipotecaBase) + ", precio de edificar= " + Integer.toString(precioEdificar);
        else
            return "nombre= " + nombre + ", propietario= no tiene" + ", hipotecada= " + hipotecada + ", alquiler= " + Integer.toString(alquilerBase) + ", casas= " + Integer.toString(numCasas) + ", hoteles= " + Integer.toString(numHoteles)+ ", hipoteca= " + Integer.toString(hipotecaBase) + ", precio de edificar= " + Integer.toString(precioEdificar);
    }
}
