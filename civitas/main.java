package civitas;

import java.util.*;
import juegoTexto.VistaTextual;
import juegoTexto.Controlador;
public class main {
    public static void main(String[] args) {
        //Creamos una vista textual
        VistaTextual vista=new VistaTextual();
        
        //Creamos una instancia de CivitasJuego
        ArrayList<String> nombres=new ArrayList<>();
        nombres.add("Pepe");
        nombres.add("Pablo");
        nombres.add("Juan");
        nombres.add("Jesus");
        
        CivitasJuego juego=new CivitasJuego(nombres);
        
        vista.setCivitasJuego(juego);
        
        //Ponemos el dado en modo debug
        Dado.getInstance().setDebug(false);
        
        //Creamos una instancia del controlador
        Controlador controlador=new Controlador(juego,vista);
        
        //Llamamos al metodo juega
        controlador.juega();
    }
    
}
