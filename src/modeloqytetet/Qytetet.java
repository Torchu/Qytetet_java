package modeloqytetet;
import java.util.ArrayList;
import static java.util.Collections.sort;
import static java.util.Collections.shuffle;

/**
 *
 * @author root
 */
public class Qytetet {
    //Atributos
    private static final Qytetet instance=new Qytetet();
    public static final int MAX_JUGADORES=4;
    static final int NUM_SORPRESAS=10;
    public static final int NUM_CASILLAS=20;
    static final int PRECIO_LIBERTAD=200;
    static final int SALDO_SALIDA=1000;
    private Sorpresa cartaActual;
    private ArrayList<Sorpresa> mazo = new ArrayList<>();
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private Jugador jugadorActual;
    private Tablero tablero;
    private Dado dado=Dado.getInstance();
    private EstadoJuego estadoJuego;
    
    private Qytetet(){}
    
    public static Qytetet getInstance(){
        return instance;
    }
    
    void actuarSiEnCasillaEdificable(){
        boolean deboPagar=jugadorActual.deboPagarAlquiler();
        if(deboPagar){
            jugadorActual.pagarAlquiler();
            if(jugadorActual.getSaldo()<=0)
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
        }
        Casilla casilla=obtenerCasillaJugadorActual();
        boolean tengoPropietario=((Calle)casilla).tengoPropietario();
        if(estadoJuego!=EstadoJuego.ALGUNJUGADORENBANCARROTA)
            if(tengoPropietario)
                setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
            else
                setEstadoJuego(EstadoJuego.JA_PUEDECOMPRARGESTIONAR);
    }
    
    void actuarSiEnCasillaNoEdificable(){
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        Casilla casillaActual=jugadorActual.getCasillaActual();
        if(casillaActual.getTipo()==TipoCasilla.IMPUESTO)
            jugadorActual.pagarImpuesto();
        else if(casillaActual.getTipo()==TipoCasilla.JUEZ)
            encarcelarJugador();
        else if(casillaActual.getTipo()==TipoCasilla.SORPRESA){
            cartaActual=mazo.remove(0);
            setEstadoJuego(EstadoJuego.JA_CONSORPRESA);
        }
    }
    
    public void aplicarSorpresa(){
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        if(cartaActual.getTipo()==TipoSorpresa.SALIRCARCEL)
            jugadorActual.setCartaLibertad(cartaActual);
        else
            mazo.add(cartaActual);
        if(cartaActual.getTipo()==TipoSorpresa.PAGARCOBRAR){
            jugadorActual.modificarSaldo(cartaActual.getValor());
            if(jugadorActual.getSaldo()<=0)
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
        }
        else if(cartaActual.getTipo()==TipoSorpresa.CONVERTIRME){
            Especulador esp=jugadorActual.convertirme(cartaActual.getValor());
            jugadores.set(jugadores.indexOf(jugadorActual), esp);
            jugadorActual=esp;
        }
        else if(cartaActual.getTipo()==TipoSorpresa.IRACASILLA){
            int valor=cartaActual.getValor();
            boolean casillaCarcel=tablero.esCasillaCarcel(valor);
            if(casillaCarcel)
                encarcelarJugador();
            else
                mover(valor);
        }
        else if(cartaActual.getTipo()==TipoSorpresa.PORCASAHOTEL){
            int cantidad=cartaActual.getValor();
            int numeroTotal=jugadorActual.cuantasCasasHotelesTengo();
            jugadorActual.modificarSaldo(cantidad*numeroTotal);
            if(jugadorActual.getSaldo()<=0)
                setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
        }
        else if(cartaActual.getTipo()==TipoSorpresa.PORJUGADOR){
            for(Jugador jugador:  jugadores){
                if(jugador!=jugadorActual){
                    jugador.modificarSaldo(cartaActual.getValor());
                    if(jugador.getSaldo()<=0)
                        setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                    jugadorActual.modificarSaldo(-cartaActual.getValor());              //Works¿?
                    if(jugadorActual.getSaldo()<=0)
                        setEstadoJuego(EstadoJuego.ALGUNJUGADORENBANCARROTA);
                }
            }
        }
    }
    
    public boolean cancelarHipoteca(int numeroCasilla){
        Casilla casilla=tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo=casilla.getTitulo();
        boolean cancelada=jugadorActual.cancelarHipoteca(titulo);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return cancelada;
    }
    
    public boolean comprarTituloPropiedad(){
        boolean comprado=jugadorActual.comprarTituloPropiedad();
        if(comprado)
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return comprado;
    }
    
    public boolean edificarCasa(int numeroCasilla){
        Casilla casilla=tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo=casilla.getTitulo();
        boolean edificada=jugadorActual.edificarCasa(titulo);
        if(edificada)
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return edificada;
    }
    
    public boolean edificarHotel(int numeroCasilla){
        Casilla casilla=tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo=casilla.getTitulo();
        boolean edificado=jugadorActual.edificarHotel(titulo);
        if(edificado)
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        return edificado;
    }
    
    private void encarcelarJugador(){
        if(jugadorActual.deboIrACarcel()){
            Casilla casillaCarcel=tablero.getCarcel();
            jugadorActual.irACarcel(casillaCarcel);
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        }else{
            if(jugadorActual.tengoCartaLibertad()){
                Sorpresa carta=jugadorActual.devolverCartaLibertad();
                mazo.add(carta);
            }
            setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
        }
    }
    
    //Getters
    public Sorpresa getCartaActual(){
        return cartaActual;
    }
    
    Dado getDado() {
        return dado;
    }
    
    public EstadoJuego getEstadoJuego(){
        return estadoJuego;
    }
    
    public Jugador getJugadorActual() {
        return jugadorActual;
    }
    
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    ArrayList<Sorpresa> getMazo(){
        return mazo;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    public void hipotecarPropiedad(int numeroCasilla){
        Calle casilla=(Calle) (tablero.obtenerCasillaNumero(numeroCasilla));
        TituloPropiedad titulo=casilla.getTitulo();
        jugadorActual.hipotecarPropiedad(titulo);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }
    
    //Inicializa todas las cartas sorpresa del mazo
    private void inicializarCartasSorpresa(){
        //CONVERTIRME
        mazo.add(new Sorpresa("Has sido ascendido, conviertete en especulador", 3000, TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa("Has sido ascendido, conviertete en especulador", 5000, TipoSorpresa.CONVERTIRME));
        //PAGARCOBRAR
        mazo.add(new Sorpresa("Hoy es tu dia de suerte cobras 1000 sin tener que pasar por la salida", 1000, TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa("No todo iba a ser bueno, pierdes 1000", -1000, TipoSorpresa.PAGARCOBRAR));
        //IRACASILLA
        mazo.add(new Sorpresa("Ve a la casilla 8, si pasas por la salida cobra", 8, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("Ve a la casilla 17, si pasas por la salida cobra", 17, TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa("La nacional te ha pillado con las manos en la masa. Ve a la carcel sin pasar por la salida", tablero.getCarcel().getNumeroCasilla(), TipoSorpresa.IRACASILLA));
        //PORCASAHOTEL
        mazo.add(new Sorpresa("Eres una persona afortunada, cobra 250 por cada casa y hotel en tu propiedad", 250, TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa("La desgracia cae sobre ti, paga 500 por cada casa y hotel en tu propiedad", -500, TipoSorpresa.PORCASAHOTEL));
        //PORJUGADOR
        mazo.add(new Sorpresa("Hoy es tu cumpleanos, recibe 250 de cada jugador", 250, TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa("Hoy invitas tu, paga 250 a cada jugador", -250, TipoSorpresa.PORJUGADOR));
        //SALIRCARCEL
        mazo.add(new Sorpresa("Esta es la llave maestra, usala para salir de la carcel", 0, TipoSorpresa.SALIRCARCEL));
        
        //Mezclar
        shuffle(mazo);
    }
    
    public void inicializarJuego(ArrayList<String> nombres){
        inicializarJugadores(nombres);
        inicializarTablero();
        inicializarCartasSorpresa();
        salidaJugadores();
    }
    
    private void inicializarJugadores(ArrayList<String> nombres){
        for(int i=0; i<nombres.size(); i++)
            jugadores.add(new Jugador(nombres.get(i)));
    }
    
    private void inicializarTablero(){
        tablero  = new Tablero();
    }
    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
        if(metodo==MetodoSalirCarcel.TIRANDODADO){
            int resultado=tirarDado();
            if(resultado>=5)
                jugadorActual.setEncarcelado(false);
        }
        else if(metodo==MetodoSalirCarcel.PAGANDOLIBERTAD)
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
        boolean encarcelado=jugadorActual.getEncarcelado();
        if(encarcelado)
            setEstadoJuego(EstadoJuego.JA_ENCARCELADO);
        else
            setEstadoJuego(EstadoJuego.JA_PREPARADO);
        return !encarcelado;
    }
    
    public void jugar(){
        int valorDado=tirarDado();
        Casilla destino=tablero.obtenerCasillaFinal(obtenerCasillaJugadorActual(), valorDado);
        mover(destino.getNumeroCasilla());
    }
    
    void mover(int numCasillaDestino){
        Casilla casillaInicial=jugadorActual.getCasillaActual();
        Casilla casillaFinal=tablero.obtenerCasillaNumero(numCasillaDestino);
        jugadorActual.setCasillaActual(casillaFinal);
        if(numCasillaDestino<casillaInicial.getNumeroCasilla())
            jugadorActual.modificarSaldo(SALDO_SALIDA);
        if(casillaFinal.soyEdificable())
            actuarSiEnCasillaEdificable();
        else
            actuarSiEnCasillaNoEdificable();
    }
    
    public Casilla obtenerCasillaJugadorActual(){
        return jugadorActual.getCasillaActual();
    }
    
    public ArrayList<Casilla> obtenerCasillasTablero(){
        return tablero.getCasillas();
    }
    
    public ArrayList<Integer> obtenerPropiedadesJugador(){
        ArrayList<Integer> propiedades=new ArrayList<>();
        for(Casilla c: tablero.getCasillas())
            if(jugadorActual.getPropiedades().contains(c.getTitulo()))
                propiedades.add(c.getNumeroCasilla());
        return propiedades;
    }
    
    public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca){
        ArrayList<Integer> propiedades=new ArrayList<>();
        for(Casilla c: tablero.getCasillas())
            if(jugadorActual.obtenerPropiedades(estadoHipoteca).contains(c.getTitulo()))
                propiedades.add(c.getNumeroCasilla());
        return propiedades;
    }
    
    public void obtenerRanking(){
        sort(jugadores);
    }
    
    public int obtenerSaldoJugadorActual(){
        return jugadorActual.getSaldo();
    }
    
    public int obtenerValorDado(){
        return dado.getValor();
    }
    
    private void salidaJugadores(){
        for(Jugador j: jugadores)
            j.setCasillaActual(tablero.getCasillas().get(0));
        jugadorActual=jugadores.get((int) (Math.random()*jugadores.size()));
        estadoJuego=EstadoJuego.JA_PREPARADO;
    }
    
    private void setCartaActual(Sorpresa carta){
        cartaActual=carta;
    }
    
    public void setEstadoJuego(EstadoJuego estadoJuego){
        this.estadoJuego=estadoJuego;
    }
    
    public void siguienteJugador(){
        jugadorActual=jugadores.get((jugadores.indexOf(jugadorActual)+1)%jugadores.size());
        if (jugadorActual.getEncarcelado())
            estadoJuego=EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD;
        else
            estadoJuego=EstadoJuego.JA_PREPARADO;
    }
    
    int tirarDado(){
        return dado.tirar();
    }
    
    public void venderPropiedad(int numeroCasilla){
        Casilla casilla=tablero.obtenerCasillaNumero(numeroCasilla);
        jugadorActual.venderPropiedad(casilla);
        setEstadoJuego(EstadoJuego.JA_PUEDEGESTIONAR);
    }
    
    @Override
    public String toString(){
        return "Qytetet{Maximo de jugadores: "+MAX_JUGADORES+", numero de sorpresas: "+NUM_SORPRESAS+", numero de casillas: "+NUM_CASILLAS+", precio de libertad: "+PRECIO_LIBERTAD+
                ", saldo de salida: "+SALDO_SALIDA+", carta actual: "+cartaActual+", mazo: "+mazo+" jugadores: "+jugadores+", jugador actual: "+jugadorActual+", tablero: "+tablero+
                ", dado: "+dado+"}";
    }
}
