package modeloqytetet;
/**
 *
 * @author torchu
 */
public class Dado {
    private static final Dado instance=new Dado();
    private int valor;
    private Dado(){
        valor=0;
    }
    
    public static Dado getInstance(){
        return instance;
    }
    
    int tirar(){
        valor=(int) (Math.random()*6+1);
        return valor;
    }
    
    public int getValor(){
        return valor;
    }
    
    @Override
    public String toString(){
      return "Dado{valor= " + valor +"}";
    }
}