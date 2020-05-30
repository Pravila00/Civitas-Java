package civitas;

import java.util.*;

public class Sorpresa {
    private String texto;
    private int valor;
    MazoSorpresas mazo;
    Tablero tablero;
    TipoSorpresa tipo;
    
    private void init(){
        valor=-1;
        mazo=null;
        tablero=null;
    }
    
    Sorpresa(TipoSorpresa tipo, Tablero tablero){
        init();
        this.tablero=tablero;
        this.tipo=tipo;   
    }
    
    Sorpresa(TipoSorpresa tipo, Tablero tablero, int valor, String texto){
        init();
        this.valor=valor;
        this.texto=texto;
        this.tipo=tipo;
        this.tablero=tablero;
    }
    
    Sorpresa(TipoSorpresa tipo, int valor, String texto){
        init();
        this.tipo=tipo;
        this.valor=valor;
        this.texto=texto;        
    }
    
    Sorpresa(TipoSorpresa tipo, MazoSorpresas mazo){
        init();
        this.tipo=tipo;
        this.mazo=mazo;
    }
    
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        boolean esJugadorCorrecto=false;
        
        if(actual >= 0 && actual < todos.size()){
            esJugadorCorrecto=true;
        }
        
        return esJugadorCorrecto;
    }
            
    void informe(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            Diario.getInstance().ocurreEvento("Se aplica la sorpresa " + this.toString() + 
                    " al jugador " + todos.get(actual).getNombre());
        } 
    }
    
    void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        if(tipo==TipoSorpresa.IRCARCEL){
            this.aplicarAJugador_irCarcel(actual, todos);
        }
        
        else if(tipo==TipoSorpresa.IRCASILLA){
            this.aplicarAJugador_irACasilla(actual, todos);
        }
        
        else if(tipo==TipoSorpresa.PAGARCOBRAR){
            this.aplicarAJugador_pagarCobrar(actual, todos);
        }
        
        else if(tipo==TipoSorpresa.PORCASAHOTEL){
            this.aplicarAJugador_porCasaHotel(actual, todos);
        }
        
        else if(tipo==TipoSorpresa.PORJUGADOR){
            this.aplicarAJugador_porJugador(actual, todos);
        }
        
        else if(tipo==TipoSorpresa.SALIRCARCEL){
            this.aplicarAJugador_salirCarcel(actual, todos);
        }
    }
    
    private void aplicarAJugador_irCarcel(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            todos.get(actual).encarcelar(this.tablero.getCarcel());
        }
    }
    
    private void aplicarAJugador_irACasilla(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            int tirada=this.tablero.calcularTirada(todos.get(actual).getNumCasillaActual(),valor);
            int posicionNueva=this.tablero.nuevaPosicion(todos.get(actual).getNumCasillaActual(),tirada);
            todos.get(actual).moverACasilla(posicionNueva);
            this.tablero.getCasilla(posicionNueva).recibeJugador(actual, todos);
        }
    }
    
    private void aplicarAJugador_pagarCobrar(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            todos.get(actual).modificarSaldo(valor);
        }
    }
    
    private void aplicarAJugador_porCasaHotel(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            ArrayList<TituloPropiedad> propiedades=todos.get(actual).getPropiedades();
            int totalCasasHoteles = 0;
            for(int i=0; i<propiedades.size();i++){
                totalCasasHoteles += propiedades.get(i).getNumCasas() + propiedades.get(i).getNumHoteles();
            }
            todos.get(actual).modificarSaldo(valor*totalCasasHoteles);
        }
    }
    
    private void aplicarAJugador_porJugador(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            Sorpresa restoJugadores=new Sorpresa(TipoSorpresa.PAGARCOBRAR, (valor*(-1)), "PagarCobrar");
            for(int i=0;i<todos.size();i++){
                if(todos.get(actual) != todos.get(i)){
                  restoJugadores.aplicarAJugador_pagarCobrar(i, todos);
                }
            }
            Sorpresa actualJugador=new Sorpresa(TipoSorpresa.PAGARCOBRAR,(valor*(todos.size()-1)), "PagarCobrar");
            actualJugador.aplicarAJugador_pagarCobrar(actual, todos);      
        }
    }
    
    private void aplicarAJugador_salirCarcel(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            boolean encontrado=false;
            for(int i=0; i<todos.size() && !encontrado;i++){
                if(todos.get(i).tieneSalvoconducto()){
                    encontrado=true;
                }
            }
            
            if(!encontrado){
                todos.get(actual).obtenerSalvoconducto(this);
                salirDelMazo();
            }
        }
    }
    
    void salirDelMazo(){
        if(tipo==TipoSorpresa.SALIRCARCEL){
            mazo.inhabilitarCartaEspecial(this);
        }
    }
    
    void usada(){
        if(tipo==TipoSorpresa.SALIRCARCEL){
            mazo.habilitarCartaEspecial(this);
        }
    }
    
    public String toString(){
        String string="Sorpresa: " + tipo;
                
        return string;        
    }  
}

