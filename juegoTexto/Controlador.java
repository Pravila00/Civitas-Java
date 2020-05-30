package juegoTexto;

import java.util.*;

import civitas.Jugador;
import civitas.CivitasJuego;
import civitas.GestionesInmobiliarias;
import civitas.OperacionesJuego;
import civitas.Respuestas;
import civitas.OperacionInmobiliaria;
import civitas.SalidasCarcel;

/**
 *
 */
public class Controlador {
    private CivitasJuego juego;
    private VistaTextual vista;
    
    public Controlador(CivitasJuego juego,VistaTextual vista){
        this.juego=juego;
        this.vista=vista;
    }
    public void juega(){
        vista.setCivitasJuego(juego);
        while(!(juego.finalDelJuego())){
            vista.actualizarVista();
            vista.pausa();
            
            OperacionesJuego op = juego.siguientePaso();
            
            vista.mostrarSiguienteOperacion(op);
            if(op != OperacionesJuego.PASAR_TURNO){
                vista.mostrarEventos();
            }
            if(!(juego.finalDelJuego())){
                if(op == OperacionesJuego.COMPRAR){
                    Respuestas resultado=vista.comprar();
                    if(resultado==Respuestas.SI){
                        juego.comprar();
                         
                    }
                    juego.siguientePasoCompletado(OperacionesJuego.COMPRAR);
                }
                else if(op == OperacionesJuego.GESTIONAR){
                    vista.gestionar();
                    GestionesInmobiliarias gestion;
                    OperacionInmobiliaria operacion;
                    switch (vista.getGestion()){
                        case 0:
                            gestion = GestionesInmobiliarias.VENDER;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            juego.vender(vista.getPropiedad());
                            break;
                        case 1:
                            gestion = GestionesInmobiliarias.HIPOTECAR;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            juego.hipotecar(vista.getPropiedad());
                            break;
                        case 2:
                            gestion = GestionesInmobiliarias.CANCERLAR_HIPOTECA;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            juego.cancelarHipoteca(vista.getPropiedad());
                            break;    
                        case 3:
                            gestion = GestionesInmobiliarias.CONSTRUIR_CASA;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            juego.construirCasa(vista.getPropiedad());
                            break;
                        case 4:
                            gestion = GestionesInmobiliarias.CONSTRUIR_HOTEL;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            boolean construido=juego.construirHotel(vista.getPropiedad());
                            if(!construido){
                                System.out.println("No se puede construir un hotel ");
                            }
                            
                            break;
                        default:
                            gestion = GestionesInmobiliarias.TERMINAR;
                            operacion = new OperacionInmobiliaria(vista.getPropiedad(),gestion);
                            juego.siguientePasoCompletado(op);
                            break;
                        
                    }
                 
                }
                else if(op == OperacionesJuego.SALIR_CARCEL){
                    SalidasCarcel salida = vista.salirCarcel();
                    boolean result = (salida == SalidasCarcel.PAGANDO) ? juego.salirCarcelPagando() : juego.salirCarcelTirando();
                    
                    juego.siguientePasoCompletado(op);
                }
            }
            else{
                juego.finalDelJuego();
            }
            System.out.println("=============================");   
        }
    }
    
}
