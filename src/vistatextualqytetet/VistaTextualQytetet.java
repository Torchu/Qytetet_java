package vistatextualqytetet;

import controladorqytetet.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaTextualQytetet {
    private static ControladorQytetet controlador = ControladorQytetet.getInstance();
    
    public VistaTextualQytetet(){}
    
    public ArrayList<String> obtenerNombreJugadores(){
        ArrayList<String> jugadores = new ArrayList<>();
        System.out.println("Introduce el numero de jugadores: ");
        Scanner in = new Scanner (System.in);
        int numeroJugadores=in.nextInt();
        in.skip("\n");      //Necesario para que in.nextLine no coja el salto de linea del entero como nombre
        for(int i=0; i<numeroJugadores; i++){
            System.out.println("Introduce el nombre del jugador " + i + ": ");
            jugadores.add(in.nextLine());
        }
        return jugadores;
    }
    
    public int elegirCasilla(int OpcionMenu){
        ArrayList<Integer> validas = controlador.obtenerCasillasValidas(OpcionMenu);
        if(validas.size() == 0){
            System.err.println("No hay opciones validas");
            return -1;
        }
        else{
            ArrayList <String> validasToS = new ArrayList<>();
            System.out.println("Elige una opcion entre las de la lista:");
            for(int i : validas){
                validasToS.add(Integer.toString(i));
                System.out.println(i);
            }
            return Integer.parseInt(leerValorCorrecto(validasToS)); 
        }
    }
    
    public String leerValorCorrecto(ArrayList<String> valoresCorrectos){
        String valor;
        do{
            Scanner in = new Scanner (System.in);
            valor = in.nextLine();
        }while(!valoresCorrectos.contains(valor));
        return valor;
    }
    
    public int elegirOperacion(){
        ArrayList<String> validas = new ArrayList<>();
        System.out.println("Elige una opcion entre las de la lista:");
        for(int op : controlador.obtenerOperacionesJuegoValidas()){
            validas.add(Integer.toString(op));
            System.out.println(op + " - " + controladorqytetet.OpcionMenu.values()[op]);
        }
        return Integer.parseInt(leerValorCorrecto(validas));
    }
    
    public static void main(String args[]){
        VistaTextualQytetet ui = new VistaTextualQytetet();
        controlador.setNombreJugadores(ui.obtenerNombreJugadores());
        int operacionElegida, casillaElegida = 0;
        boolean necesitaElegirCasilla;
        
        while(true){
            operacionElegida = ui.elegirOperacion();
            necesitaElegirCasilla = controlador.necesitaElegirCasilla(operacionElegida);
            if(necesitaElegirCasilla)
                casillaElegida = ui.elegirCasilla(operacionElegida);
            if(!necesitaElegirCasilla || casillaElegida>=0){
                System.out.println(controlador.realizarOperacion(operacionElegida, casillaElegida));
                if(OpcionMenu.values()[operacionElegida] == OpcionMenu.OBTENERRANKING || OpcionMenu.values()[operacionElegida] == OpcionMenu.TERMINARJUEGO)
                    break;
            }
        }
    }
}