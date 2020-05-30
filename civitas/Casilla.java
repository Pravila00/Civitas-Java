package civitas;

import java.util.ArrayList;


public class Casilla {
    //**************************************************************************
    //Atributos
    //**************************************************************************
    
    //Atributos simples
    private String nombre;
    static private int carcel;
    private float importe;
    
    
    //Atributos complejos
    private TipoCasilla tipo;
    private Sorpresa sorpresa;
    private TituloPropiedad tituloPropiedad;
    private MazoSorpresas mazo;
    
    //**************************************************************************
    
    //Constructores 

    //~Casilla(nombre : string)
    Casilla(String nombre){
        init();
        this.nombre=nombre;
        this.tipo=TipoCasilla.DESCANSO;
        
    }

    //~Casilla(titulo : TituloPropiedad)
    Casilla(TituloPropiedad titulo){
        init();
        this.tituloPropiedad = titulo;
        nombre=tituloPropiedad.getNombre();
        this.tipo = TipoCasilla.CALLE;
    }
    
    //~Casilla(cantidad : float, nombre : string) 
    Casilla(float cantidad, String nombre){
        init();
        this.importe = cantidad;
        this.nombre = nombre;
        this.tipo = TipoCasilla.IMPUESTO;
    }
    
    //~Casilla(numCasillaCarcel : int, nombre : string)
    Casilla(int numCasillaCarcel, String nombre){
        init();
        this.nombre = nombre;
        this.carcel = numCasillaCarcel;
        this.tipo = TipoCasilla.JUEZ;
    }
    
    //~Casilla(mazo : MazoSorpresas, nombre : string)
    Casilla(MazoSorpresas mazo, String nombre){
        init();
        this.nombre = nombre;
        this.mazo = mazo;
        this.tipo = TipoCasilla.SORPRESA;
    }
    //-init() : void
    private void init(){
        nombre="";
        tituloPropiedad=null;
        importe=0;
        carcel=0;
        tipo=TipoCasilla.CALLE;
        sorpresa=null;
        mazo=null;
    }
    
    //Métodos publicos
    
    //Consultor de nombre
    public String getNombre(){
        return nombre;
    }
    
    //+jugadorCorrecto(iactual : int, todos : Jugador[1..4]) : boolean
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        boolean bool = false;
        if(actual<todos.size() && actual >= 0){
            bool=true;
        }
        return bool;
    }
    
    //+toString() : string
    public String toString(){
        String str;
        if(this.tipo==TipoCasilla.CALLE){
            str=tituloPropiedad.getNombre();
        }
        else if (this.tipo==TipoCasilla.IMPUESTO){
            str="IMPUESTO: " + importe;
        }
        else if (this.tipo==TipoCasilla.JUEZ){
            str="CARCEL ";
        }
        else if (this.tipo==TipoCasilla.SORPRESA){
            str="SORPRESA: " +sorpresa;
        }
        else{
            str="Nombre: " + nombre + " Tipo: " + tipo;
        }
        return str;
    }
    
    //Métodos paquete
    
    //~getTituloPropiedad() : TituloPropiedad
    TituloPropiedad getTituloPropiedad(){
        return tituloPropiedad;
    }
    
    //~recibeJugador(iactual : int, todos : Jugador[1..4]) : void
    void recibeJugador(int iactual, ArrayList<Jugador> todos){
        if(this.tipo==TipoCasilla.CALLE){
            recibeJugador_calle(iactual,todos);
        }
        else if (this.tipo==TipoCasilla.IMPUESTO){
            recibeJugador_impuesto(iactual,todos);
        }
        else if (this.tipo==TipoCasilla.JUEZ){
            recibeJugador_juez(iactual,todos);
        }
        else if (this.tipo==TipoCasilla.SORPRESA){
            recibeJugador_sorpresa(iactual,todos);
        }
        else{
            informe(iactual,todos);
        }
    }
    
    
    //Métodos privados
    
   //-informe(iactual : int, todos : Jugador[1..4]) : void
    private void informe(int actual, ArrayList<Jugador> todos){
        Diario.getInstance().ocurreEvento(todos.get(actual).toString());
        
        Diario.getInstance().ocurreEvento(this.toString());               
    }
    
    //-recibeJugador_calle(iactual : int, todos : Jugador[1..4]) : void
    private void recibeJugador_juez(int actual, ArrayList<Jugador> todos){
        if(this.jugadorCorrecto(actual, todos)){
            this.informe(actual, todos);
            boolean resultado=todos.get(actual).encarcelar(carcel);
        }
    }
    
    //-recibeJugador_impuesto(iactual : int, todos : Jugador[1..4]) : void
    private void recibeJugador_impuesto(int actual, ArrayList<Jugador> todos){
        if(this.jugadorCorrecto(actual, todos)){
            this.informe(actual, todos);
            boolean resultado=todos.get(actual).pagaImpuesto(importe);
        }        
    }
    
    //-recibeJugador_juez(iactual : int, todos : Jugador[1..4]) : void
    private void recibeJugador_sorpresa(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            sorpresa=mazo.siguiente();
            informe(actual,todos);
            sorpresa.aplicarAJugador(actual, todos);
            
        }
    }
    
    private void recibeJugador_calle(int actual, ArrayList<Jugador> todos){
        if(jugadorCorrecto(actual,todos)){
            informe(actual,todos);
            Jugador jugador=todos.get(actual);
            if(!tituloPropiedad.tienePropietario()){
                boolean puede=jugador.puedeComprarCasilla();
            }
            else{
                tituloPropiedad.tramitarAlquiler(jugador);
            }
        }
    }
}
