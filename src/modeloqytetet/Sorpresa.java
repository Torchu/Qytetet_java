package modeloqytetet;

/**
 *
 * @author root
 */
public class Sorpresa {
    //Atributos
    private String texto;
    private TipoSorpresa tipo;
    private int valor;
    
    //Constructor
    public Sorpresa(String texto, int valor, TipoSorpresa tipo){
        this.texto=texto;
        this.valor=valor;
        this.tipo=tipo;
    }
    
    //Getters
    String getTexto(){
        return texto;
    }
    
    int getValor(){
        return valor;
    }
    
    TipoSorpresa getTipo(){
        return tipo;
    }
    
    //Metodo toString
    @Override
    public String toString() {
        return "Sorpresa{ texto= " + texto + " , valor= " + Integer.toString(valor) + " , tipo= " + tipo + "}";
    }
}
