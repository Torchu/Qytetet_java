package controladorqytetet;

import java.util.ArrayList;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;

public class ControladorQytetet {
    private static final ControladorQytetet instance=new ControladorQytetet();
    private ArrayList<String> nombreJugadores = new ArrayList<>();
    private modeloqytetet.Qytetet modelo = modeloqytetet.Qytetet.getInstance();
    
    private ControladorQytetet(){}
    
    public static ControladorQytetet getInstance(){
        return instance;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores){
        for(String nombre : nombreJugadores)
            this.nombreJugadores.add(nombre);
    }
    
    public ArrayList<Integer> obtenerOperacionesJuegoValidas(){
        ArrayList<Integer> validas = new ArrayList<>();
        if(modelo.getJugadores().isEmpty())
            validas.add(OpcionMenu.INICIARJUEGO.ordinal());
        else{
            if(modelo.getEstadoJuego() == EstadoJuego.ALGUNJUGADORENBANCARROTA)
                validas.add(OpcionMenu.OBTENERRANKING.ordinal());
            else if(modelo.getEstadoJuego() == EstadoJuego.JA_CONSORPRESA)
                validas.add(OpcionMenu.APLICARSORPRESA.ordinal());
            else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADO)
                validas.add(OpcionMenu.PASARTURNO.ordinal());
            else if(modelo.getEstadoJuego() == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD){
                validas.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                validas.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());
            }
            else if(modelo.getEstadoJuego() == EstadoJuego.JA_PREPARADO)
                validas.add(OpcionMenu.JUGAR.ordinal());
            else if(modelo.getEstadoJuego() == EstadoJuego.JA_PUEDECOMPRARGESTIONAR){
                validas.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
                validas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                validas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                validas.add(OpcionMenu.EDIFICARCASA.ordinal());
                validas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                validas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                validas.add(OpcionMenu.PASARTURNO.ordinal());
            }
            else{
                validas.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                validas.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                validas.add(OpcionMenu.EDIFICARCASA.ordinal());
                validas.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                validas.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                validas.add(OpcionMenu.PASARTURNO.ordinal());
            }
            validas.add(OpcionMenu.TERMINARJUEGO.ordinal());
            validas.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
            validas.add(OpcionMenu.MOSTRARJUGADORES.ordinal());
            validas.add(OpcionMenu.MOSTRARTABLERO.ordinal());
        }
        return validas;
    }
    
    public boolean necesitaElegirCasilla(int opcionMenu){
        if(OpcionMenu.values()[opcionMenu] == OpcionMenu.EDIFICARCASA || OpcionMenu.values()[opcionMenu] == OpcionMenu.EDIFICARHOTEL ||
           OpcionMenu.values()[opcionMenu] == OpcionMenu.HIPOTECARPROPIEDAD || OpcionMenu.values()[opcionMenu] == OpcionMenu.CANCELARHIPOTECA ||
           OpcionMenu.values()[opcionMenu] == OpcionMenu.VENDERPROPIEDAD)
            return true;
        else
            return false;
    }
    
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu){
        if(OpcionMenu.values()[opcionMenu] == OpcionMenu.CANCELARHIPOTECA)
            return modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        else
            return modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
    }
    
    public String realizarOperacion(int opcionElegida, int casillaElegida){
        if(OpcionMenu.values()[opcionElegida] == OpcionMenu.INICIARJUEGO){
            modelo.inicializarJuego(nombreJugadores);
            return "Comieza el juego";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.APLICARSORPRESA){
            modeloqytetet.Sorpresa aux = modelo.getCartaActual();
            modelo.aplicarSorpresa();
            return "Se aplica la sorpresa: " + aux.toString();
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.CANCELARHIPOTECA){
            if(modelo.cancelarHipoteca(casillaElegida))
                return "El jugador cancela la hipoteca de la casilla " + casillaElegida;
            else
                return "No tienes suficiente dinero para eso";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.COMPRARTITULOPROPIEDAD){
            if(modelo.comprarTituloPropiedad())
                return "El jugador compra la propiedad";
            else
                return "No tienes suficiente dinero para eso";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.EDIFICARCASA){
            if(modelo.edificarCasa(casillaElegida))
                return "El jugador construye una casa en la casilla " + casillaElegida;
            else
                return "No tienes suficiente dinero para eso";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.EDIFICARHOTEL){
            if(modelo.edificarHotel(casillaElegida))
                return "El jugador construye un hotel en la casilla " + casillaElegida;
            else
                return "No tienes suficiente dinero para eso o no suficientes casas construidas";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.HIPOTECARPROPIEDAD){
            modelo.hipotecarPropiedad(casillaElegida);
            return "¡Qué dura es la crisis! El jugador hipoteca la propiedad de la casilla " + casillaElegida;
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD){
            modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
            return "El jugador ha pagado la fianza y sale de la carcel";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.INTENTARSALIRCARCELTIRANDODADO){
            if(modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO))
                return "El jugador se ve con suerte, va a probar con los dados y lo consigue. Siguiente parada: el Codere";
            else
                return "El jugador se ve con suerte, va a probar con los dados y pasa esto. *Sale mal*";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.JUGAR){
            modelo.jugar();
            return "Has sacado un " + modelo.obtenerValorDado() + " ve a la casilla " + modelo.obtenerCasillaJugadorActual().toString();
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.OBTENERRANKING){
            modelo.obtenerRanking();
            return "¡Se acabo!\nEsta es la clasificacion: " + modelo.getJugadores().toString();
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.PASARTURNO){
            modelo.siguienteJugador();
            return "¡Cambio de turno! " + modelo.getJugadorActual().getNombre() +" es tu momento de brillar";
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.VENDERPROPIEDAD){
            modelo.venderPropiedad(casillaElegida);
            return "La crisis se lleva todo. El jugador vende la propiedad de la casilla " + casillaElegida;
        }
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.MOSTRARJUGADORACTUAL)
            return modelo.getJugadorActual().toString();
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.MOSTRARJUGADORES)
            return modelo.getJugadores().toString();
        else if(OpcionMenu.values()[opcionElegida] == OpcionMenu.MOSTRARTABLERO)
            return modelo.getTablero().toString();
        else
            return "Juego finalizado";
    }
}
