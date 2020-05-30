package juegoTexto;

import civitas.CivitasJuego;
import civitas.Diario;
import civitas.OperacionesJuego;
import civitas.SalidasCarcel;
import civitas.Respuestas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import civitas.Jugador;

public class VistaTextual {
  
    CivitasJuego juegoModel; 
    int iGestion;
    int iPropiedad;
    private static String separador = "=====================";

    private Scanner in;

    public VistaTextual () {
        in = new Scanner (System.in);
        iGestion=-1;
        iPropiedad=-1;
    }
  
    void mostrarEstado(String estado) {
      System.out.println (estado);
    }

    void pausa() {
      System.out.print ("Pulsa una tecla");
      in.nextLine();
    }

    int leeEntero (int max, String msg1, String msg2) {
      Boolean ok;
      String cadena;
      int numero = -1;
      do {
        System.out.print (msg1);
        cadena = in.nextLine();
        try {  
          numero = Integer.parseInt(cadena);
          ok = true;
        } catch (NumberFormatException e) { // No se ha introducido un entero
          System.out.println (msg2);
          ok = false;  
        }
        if (ok && (numero < 0 || numero >= max)) {
          System.out.println (msg2);
          ok = false;
        }
      } while (!ok);

      return numero;
    }

    int menu (String titulo, ArrayList<String> lista) {
      String tab = "  ";
      int opcion;
      System.out.println (titulo);
      for (int i = 0; i < lista.size(); i++) {
        System.out.println (tab+i+"-"+lista.get(i));
      }

      opcion = leeEntero(lista.size(),
                            "\n"+tab+"Elige una opción: ",
                            tab+"Valor erróneo");
      return opcion;
    }

    SalidasCarcel salirCarcel() {
      int opcion = menu ("Elige la forma para intentar salir de la carcel",
        new ArrayList<> (Arrays.asList("Pagando","Tirando el dado")));
      return (SalidasCarcel.values()[opcion]);
    }

    Respuestas comprar() {
        int opcion= menu ("Desea comprar la casilla?",
                new ArrayList<> (Arrays.asList("No comprar","Comprar")));
        return Respuestas.values()[opcion];
    }

    void gestionar () {
        int opcion = menu("Elige la gestion inmobiliaria",
          new ArrayList<> (Arrays.asList("Vender","Hipotecar","Cancelar hipoteca",
          "Construir casa","Construir hotel","Terminar")));
        iGestion=opcion;
        //Si no se ha escogido la opcion terminar
        if(opcion != 5){
            Jugador jugadorActual=juegoModel.getJugadorActual();
            iPropiedad= menu("Elige una de tus propiedades",jugadorActual.getNombresPropiedades());     
        }     
    }

    public int getGestion(){
        return iGestion;
    }

    public int getPropiedad(){
        return iPropiedad;
    }


    void mostrarSiguienteOperacion(OperacionesJuego operacion) {
        System.out.println("Se va a realizar la siguiente operacion: "
        + operacion);
    }


    void mostrarEventos() {
      while(Diario.getInstance().eventosPendientes()){
          System.out.println(Diario.getInstance().leerEvento() +"\n");
      }
    }

    public void setCivitasJuego(CivitasJuego civitas){ 
          juegoModel=civitas;
          this.actualizarVista();
      }

    void actualizarVista(){
      //Mostramos informacion del jugador actual
      String infJugador=juegoModel.getJugadorActual().toString();
      System.out.println(infJugador);

      //Mostramos informacion de sus propiedades
      for(int i=0;i<juegoModel.getJugadorActual().getPropiedades().size();i++){
          System.out.println(juegoModel.getJugadorActual().getPropiedades().toString());
      }

      //Mostramos informacion de la casilla actual
      int casillaActual=juegoModel.getJugadorActual().getNumCasillaActual();
      System.out.println(juegoModel.getTablero().getCasilla(casillaActual).toString());

      System.out.println(separador);

    } 

}
