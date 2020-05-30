package civitas;

import java.util.*;

public class TituloPropiedad {
    //Atributos de instancia 
    Jugador propietario;
    private float alquilerBase;
    static private float factorInteresesHipoteca= 1.1F;
    private float factorRevalorizacion;
    private float hipotecaBase;
    private boolean hipotecado;
    private String nombre;
    private int numCasas;
    private int numHoteles;
    private float precioCompra;
    private float precioEdificar;
    static private int hotelesMax=4;
    static private int casasPorHotel=4;
    
    
    //Constructor
    TituloPropiedad(String n,float ab,float fr,float bh,float pc, float pe){
        //Asignamos los valores que han sido pasados como parametros
        nombre=n;
        alquilerBase=ab;
        factorRevalorizacion=fr;
        hipotecaBase=bh;
        precioCompra=pc;
        precioEdificar=pe;
        //Inicializamos los valores indicados
        numCasas=0;
        numHoteles=0;
        hipotecado=false;
        propietario=null;
    }
    
    //Metodo que proporciona una representacion del objeto
    public String toString(){
        String resultado;
        resultado="Nombre: " +nombre ;
        resultado+=" Alquiler base: "+alquilerBase ;
        resultado+=" Factor de revalorizacion: " +factorRevalorizacion;
        resultado+=" Hipoteca base: "+hipotecaBase;
        resultado+=" Precio compra: "+precioCompra;
        resultado+=" Precio de edificar: "+precioEdificar;
        resultado+="Num Casas: "+numCasas+"\n";
        resultado+="Num Hoteles: "+numHoteles;
        String estaHipotecado=" La propiedad esta hipotecada";
        String noEstaHipotecado=" La propiedad no esta hipotecada";
        resultado += (hipotecado) ? estaHipotecado : noEstaHipotecado;
        resultado+=" Factor de intereses hipoteca: "+factorInteresesHipoteca+"\n";
        return resultado;  
    }
    
    void actualizaPropietarioPorConversion(Jugador jugador){
        propietario=jugador;
    }
            
    boolean cancelarHipoteca(Jugador jugador){
        boolean result=false;
        if(getHipotecado()){
            if(esEsteElPropietario(jugador)){
                jugador.paga(getImporteCancelarHipoteca());
                hipotecado=false;
                result=true;
            }
        }
        return result;
    }
            
    int cantidadCasasHotel(){
        return numCasas+numHoteles;
    }
    
    boolean comprar(Jugador jugador){
        boolean result=false;
        if(!tienePropietario()){
            propietario=jugador;
            result=jugador.paga(getPrecioCompra());
        }
        return result;
    }
            
    boolean construirCasa(Jugador jugador){
        boolean result=false;
        if(esEsteElPropietario(jugador)){
            result=jugador.paga(precioEdificar);
            numCasas=numCasas+1;        
        }
        return result;
    }
            
    boolean construirHotel(Jugador jugador){
        boolean result=false;
        if(esEsteElPropietario(jugador)){
            jugador.paga(precioEdificar);
            numHoteles=numHoteles+1;
            result=true;
        }
        return result;
    }
            
    boolean derruirCasas(int n,Jugador jugador){
        boolean resultado=false;
        if(esEsteElPropietario(jugador) && numCasas >=n){
            numCasas=numCasas-n;
            resultado=true;
        }
        return resultado;
    }
            
    boolean esEsteElPropietario(Jugador jugador){
        return propietario==jugador;
    }
            
    //Consultores
    public boolean getHipotecado(){
        return hipotecado;
    }
    
    float getImporteCancelarHipoteca(){
        float importeHipoteca=hipotecaBase*(1+(numCasas*0.5F)+(numHoteles*2.5F));
        importeHipoteca=importeHipoteca*factorInteresesHipoteca;
        return importeHipoteca;
    }
    
    float getImporteHipoteca(){
        return hipotecaBase*(1+(numCasas*0.5F)+(numHoteles*2.5F));
    }
            
    String getNombre(){
        return nombre;
    }
    
    int getNumCasas(){
        return numCasas;
    }
            
    int getNumHoteles(){
        return numHoteles;
    }
    
    int getHotelesMax(){
        return hotelesMax;
    }
    
    int getCasasPorHotel(){
        return casasPorHotel;
    }
            
    private float getPrecioAlquiler(){
        return alquilerBase*(1+numCasas*0.5F)+(numHoteles*0.5F);
    }
            
    float getPrecioCompra(){
        return precioCompra;
    }
            
    float getPrecioEdificar(){
        return precioEdificar;
    }
            
    private float getPrecioVenta(){
        return precioCompra+(numCasas+5*numHoteles)*precioEdificar*factorRevalorizacion;
    }
            
    Jugador getPropietario(){
        return propietario;
    }
    
    //Metodos variados
            
    boolean hipotecar(Jugador jugador){
        boolean salida=false;
        double hipoteca=getImporteHipoteca();
        if(!hipotecado && esEsteElPropietario(jugador)){
            jugador.recibe((float)hipoteca);
            hipotecado=true;
            salida=true;
        }
        return salida;
    }
            
    private boolean propietarioEncarcelado(){
        return propietario.isEncarcelado();
    }
            
    boolean tienePropietario(){
        return propietario!=null;
    }
    
    void setHipotecado(boolean estaHipotecado){
        hipotecado=estaHipotecado;
    }
    
    void setPropietario(Jugador nuevoPropietario){
        propietario=nuevoPropietario;
    }
    
    void TituloPropiedad(String nom, float ab, float fr,float hb,float pc,float pe){
        nombre=nom;
        alquilerBase=ab;
        factorRevalorizacion=fr;
        precioCompra=pc;
        precioEdificar=pe;
        hipotecaBase=hb;
    }
            
    void tramitarAlquiler(Jugador jugador){
        if(tienePropietario() && !esEsteElPropietario(jugador)){
            float precio=getPrecioAlquiler();
            jugador.pagaAlquiler(precio);
            propietario.recibe(precio);
        }
    }
            
    boolean vender(Jugador jugador){
        boolean resultado=false;
        if(esEsteElPropietario(jugador)){
            propietario=null;
            resultado=jugador.recibe(getPrecioVenta());                               
        }
        return resultado;
    }
            
}
