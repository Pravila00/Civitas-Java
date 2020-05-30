/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.*;

public class CivitasJuego {    
    //**************************************************************************    
    //Atributos
    //**************************************************************************
    
    //Atributos privados
    private int indiceJugadorActual;
    
    //Atributos complejos
    private ArrayList<Jugador> jugadores; //jugadores[4] *?*
    private EstadosJuego estado;
    private Tablero tablero;
    private MazoSorpresas mazo;
    private GestorEstados gestorEstados;
    
    //**************************************************************************
    //Métodos
    //**************************************************************************
    
    //Constructores
    
    //+CivitasJuego(nombres : String[1..4])
    public CivitasJuego(ArrayList<String> nombres){
        jugadores=new ArrayList();
        for(int i=0;i<nombres.size();i++){
            //Creamos un jugador
            Jugador unJugador=new Jugador(nombres.get(i));
            jugadores.add(unJugador);
        }
        gestorEstados= new GestorEstados();   
        estado=gestorEstados.estadoInicial();
        indiceJugadorActual=Dado.getInstance().quienEmpieza(nombres.size());
        mazo=new MazoSorpresas();
        inicializarTablero(mazo);
        inicializarMazoSorpresas(tablero);
    } 
    
    
    //Métodos públicos
    
    //+cancelarHipoteca(ip : int) : boolean
    public boolean cancelarHipoteca(int ip){
        return jugadores.get(indiceJugadorActual).cancelarHipoteca(ip);
    }
    
    //+comprar() : boolean
    public boolean comprar(){
        Jugador jugadorActual=jugadores.get(indiceJugadorActual);
        int numCasillaActual=jugadorActual.getNumCasillaActual();
        Casilla casilla=tablero.getCasilla(numCasillaActual);
        TituloPropiedad titulo=casilla.getTituloPropiedad();
        boolean res=jugadorActual.comprar(titulo);
        return res;
    }
    
    //+construirCasa(ip : int) : boolean
    public boolean construirCasa(int ip){
        return jugadores.get(indiceJugadorActual).construirCasa(ip);
    }
    
    //+construirHotel(ip : int) : boolean
    public boolean construirHotel(int ip){
        return jugadores.get(indiceJugadorActual).construirHotel(ip);
    }
    
    //+finalDelJuego() : boolean
    public boolean finalDelJuego(){
        boolean bool=false;
        for(int i=0;i<jugadores.size();i++){
            if(jugadores.get(i).enBancarrota()){
                bool=true;
            }
        }        
        return bool;
    }
    public Tablero getTablero(){
        return tablero;
    }
    
    //+getCasillaActual() : Casilla
    public Casilla getCasillaActual(){
        int casillaActual=jugadores.get(indiceJugadorActual).getNumCasillaActual();
        return tablero.getCasilla(casillaActual);
    }
    
    //+getJugadorActual() : Jugador
    public Jugador getJugadorActual(){
        return jugadores.get(indiceJugadorActual);
    }
    
    //+hipotecar(ip : int) : boolean
    public boolean hipotecar(int ip){
        Jugador jugadorActual=getJugadorActual();
        boolean resultado=jugadorActual.hipotecar(ip);
        return resultado;
    }
    
    //+infoJugadorTexto() : string
    public String infoJugadorTexto(){
        return getJugadorActual().toString();
    }
    
    //+salirCarcelPagando() : boolean
    public boolean salirCarcelPagando(){
        return jugadores.get(indiceJugadorActual).salirCarcelPagando();
    }
    
    //+salirCarcelTirando() : boolean
    public boolean salirCarcelTirando(){
        return jugadores.get(indiceJugadorActual).salirCarcelTirando();
    }
    
    //+siguientePaso() : OperacionesJuego
    public OperacionesJuego siguientePaso(){
        Jugador jugadorActual=jugadores.get(indiceJugadorActual);
        OperacionesJuego operacion=gestorEstados.operacionesPermitidas(jugadorActual,estado);
        if(operacion==OperacionesJuego.PASAR_TURNO){
            pasarTurno();
            siguientePasoCompletado(operacion);
        }
        else if(operacion==OperacionesJuego.AVANZAR){
            avanzaJugador();
            siguientePasoCompletado(operacion);
        }
        return operacion;
    }
    
    //+siguientePasoCompletado(operacion : OperacionesJuego) : void
    public void siguientePasoCompletado(OperacionesJuego operacion){
        Jugador jugadorActual=getJugadorActual();
        EstadosJuego nuevoEstado=gestorEstados.siguienteEstado(jugadorActual, estado, operacion);
        estado=nuevoEstado;
    }   
    
    //+vender(ip : int) : boolean
    public boolean vender(int ip){
        Jugador jugadorActual=getJugadorActual();
        return jugadorActual.vender(ip);
    }
    
    //Métodos privados
    
    //-avanzaJugador() : void
    private void avanzaJugador(){
        Jugador jugadorActual;
        jugadorActual=jugadores.get(indiceJugadorActual);
        int posicionActual=jugadorActual.getNumCasillaActual();
        int tirada=Dado.getInstance().tirar();
        int posicionNueva=tablero.nuevaPosicion(posicionActual, tirada);
        Casilla casilla=tablero.getCasilla(posicionNueva);
        contabilizarPasosPorSalida(jugadorActual);
        boolean mover = jugadorActual.moverACasilla(posicionNueva);
        casilla.recibeJugador(indiceJugadorActual, jugadores);
        contabilizarPasosPorSalida(jugadorActual);
    }
    
    //-contabilizarPasosPorSalida(jugadorActual : Jugador) : void
    private void contabilizarPasosPorSalida(Jugador jugadorActual){
        int porsalida=tablero.getPorSalida();
        while(porsalida > 0){
            boolean resultado=jugadorActual.pasaPorSalida();
        }
    }
    
    //-inicializarMazoSorpresas(tablero : Tablero) : void
    private void inicializarMazoSorpresas(Tablero tablero){
        //Sorpresa 1
        Sorpresa Cobrar=new Sorpresa(TipoSorpresa.PAGARCOBRAR,500,"Cobrar 500");
        mazo.alMazo(Cobrar);
        //Sorpresa 2
        Sorpresa Pagar=new Sorpresa(TipoSorpresa.PAGARCOBRAR,-500,"Pagar 500");
        mazo.alMazo(Pagar);
        //Sorpresa 3
        Sorpresa Ircasilla1=new Sorpresa(TipoSorpresa.IRCASILLA,tablero,7,"Vas a la casilla del Palenque");
        mazo.alMazo(Ircasilla1);
        //Sorpresa 4
        Sorpresa Ircasilla2=new Sorpresa(TipoSorpresa.IRCASILLA,tablero,12,"Te mueves a la casilla Avenida del Ejercito");
        mazo.alMazo(Ircasilla2);
        //Sorpresa 5
        Sorpresa Ircarcel=new Sorpresa(TipoSorpresa.IRCARCEL,tablero);
        mazo.alMazo(Ircarcel);
        //Sorpresa 6
        Sorpresa CasaHotel1=new Sorpresa(TipoSorpresa.PORCASAHOTEL,-100,"Hacienda viene a cobrar tus propiedades compañero");
        mazo.alMazo(CasaHotel1);
        //Sorpresa 7
        Sorpresa CasaHotel2=new Sorpresa(TipoSorpresa.PORCASAHOTEL,100,"Hacienda te paga por tus casitas y hoteles");
        mazo.alMazo(CasaHotel2);
        //Sorpresa 8
        Sorpresa CobrarJugador=new Sorpresa(TipoSorpresa.PORJUGADOR,100,"Has tenido suerte y tus compañeros te invitan a unas copas");
        mazo.alMazo(CobrarJugador);
        //Sorpresa 9 
        Sorpresa PagarJugador=new Sorpresa(TipoSorpresa.PORJUGADOR,-100,"Que mal, te toca pagar las copas de tus compañeros");
        mazo.alMazo(PagarJugador);
        //Sorpresa 10
        Sorpresa SalirCarcel=new Sorpresa(TipoSorpresa.SALIRCARCEL,mazo);
        mazo.alMazo(SalirCarcel);
    }
    
    //-inicializarTablero(mazo : MazoSorpresas) : void
    private void inicializarTablero(MazoSorpresas mazo){
        int casillaCarcel=10;
        //Salida y Carcel
        tablero=new Tablero(casillaCarcel);
        //Calle 1
        TituloPropiedad uno=new TituloPropiedad("Calle Gibraltar",55,4,400,400,550);
        Casilla calleGibraltar=new Casilla(uno);
        tablero.añadeCasilla(calleGibraltar);
        //Calle 2
        TituloPropiedad dos=new TituloPropiedad("Calle Canarias",75,5,500,500,675);
        Casilla calleCanarias=new Casilla(dos);
        tablero.añadeCasilla(calleCanarias);
        //Calle 3
        TituloPropiedad tres=new TituloPropiedad("La Atunara",95,6,520,520,750);
        Casilla Atunara=new Casilla(tres);
        tablero.añadeCasilla(Atunara);
        //Sorpresa 1
        Casilla mazo1=new Casilla(mazo,"Sorpresa 1");
        tablero.añadeCasilla(mazo1);
        //Calle 4
        TituloPropiedad cuatro=new TituloPropiedad("Avenida María Guerrero",100,7,575,575,900);
        Casilla AvenidaMariaGuerrero=new Casilla(cuatro);
        tablero.añadeCasilla(AvenidaMariaGuerrero);
        //Calle 5
        TituloPropiedad cinco=new TituloPropiedad("El Palenque",120,8,600,600,1000);
        Casilla ElPalenque=new Casilla(cinco);
        tablero.añadeCasilla(ElPalenque);
        //Impuesto
        Casilla Impuesto=new Casilla(1000,"Hacienda");
        tablero.añadeCasilla(Impuesto);
        //Calle 6
        TituloPropiedad seis=new TituloPropiedad("El Zabal",155,9,650,650,1200);
        Casilla ElZabal=new Casilla(seis);
        tablero.añadeCasilla(ElZabal); 
        //Calle 7
        TituloPropiedad siete=new TituloPropiedad("Calle Tabarca",200,10,680,680,1300);
        Casilla calleTabarca=new Casilla(siete);
        tablero.añadeCasilla(calleTabarca);
        //Carcel(posicion 10)
        //Calle 8
        TituloPropiedad ocho=new TituloPropiedad("Avenida del Ejercito",225,11,725,725,1450);
        Casilla AvenidadelEjercito=new Casilla(ocho);
        tablero.añadeCasilla(AvenidadelEjercito);
        //Sorpresa 2
        Casilla mazo2=new Casilla(mazo,"Sorpresa 2");
        tablero.añadeCasilla(mazo2);
        //Juez
        Casilla Juez=new Casilla(casillaCarcel,"Juez");
        tablero.añadeCasilla(Juez);
        //Calle 9
        TituloPropiedad nueve=new TituloPropiedad("Cruz Herrera",260,11,850,850,1900);
        Casilla CruzHerrera=new Casilla(nueve);
        tablero.añadeCasilla(CruzHerrera);
        //Calle 10
        TituloPropiedad diez=new TituloPropiedad("Venta Melchor",450,14,1000,1000,2200);
        Casilla VMelchor=new Casilla(diez);
        tablero.añadeCasilla(VMelchor);
        //Descanso
        Casilla Descanso=new Casilla("Burguer King");
        tablero.añadeCasilla(Descanso);
        //Calle 11
        TituloPropiedad once=new TituloPropiedad("Santa Margarita",750,20,1500,1500,2800);
        Casilla SMargarita=new Casilla(once);
        tablero.añadeCasilla(SMargarita);
        //Sorpresa 3
        Casilla mazo3=new Casilla(mazo,"Sorpresa 2");
        tablero.añadeCasilla(mazo3);
        //Calle 12
        TituloPropiedad doce=new TituloPropiedad("La Alcaidesa",1000,25,2200,2200,4000);
        Casilla Alcaidesa=new Casilla(doce);
        tablero.añadeCasilla(Alcaidesa);
    }
    
    //-pasarTurno() : void
    private void pasarTurno(){
        if(indiceJugadorActual<jugadores.size()-1){
            indiceJugadorActual++;
        }
        else{
            indiceJugadorActual=0;
        }
    }
    
    //-ranking() : Jugador[1..4]
    public ArrayList<Jugador> ranking(){
        ArrayList<Jugador> clasificacion= new ArrayList(jugadores.size());
        float [] saldos= new float[jugadores.size()];
        for(int i=0;i<jugadores.size();i++){
            saldos[i]=jugadores.get(i).getSaldo();
        }
        Arrays.sort(saldos);
        boolean sigue=true;
        for(int i=jugadores.size()-1;i>=0;i--){
            //Buscamos el jugador que tenga ese saldo
            for(int j=0;j<jugadores.size() && sigue;j++){
                if(saldos[i]==jugadores.get(j).getSaldo()){
                    clasificacion.add(jugadores.get(j));
                    sigue=false;
                }
            }
            sigue=true;
        }
        return clasificacion;
    }     
}

    
