package civitas;
import java.util.*;
import java.lang.Float;


public class Jugador implements Comparable<Jugador> {
    //Atributos
    protected static int CasasMax=4;
    protected static int CasasPorHotel=4;
    protected boolean encarcelado;
    protected static int HotelesMax=4;
    private String nombre;
    private int numCasillaActual;
    protected static float PasoPorSalida=1000;
    protected static float PrecioLibertad=200;
    private boolean puedeComprar;
    private float saldo;
    private static float SaldoInicial=7500;
    private Sorpresa salvoconducto;
    private ArrayList<TituloPropiedad> propiedades;
    
    //Constructor que recibe como argumento el nombre
    Jugador(String nombre){
        this.nombre=nombre;
        salvoconducto=null;
        propiedades=new ArrayList();
        encarcelado=false;
        numCasillaActual=0;
        saldo=SaldoInicial;
        puedeComprar=false;      
    }
    
    //Constructor de coia
    protected Jugador(Jugador jugador){
        this.encarcelado=jugador.encarcelado;
        this.nombre=jugador.nombre;
        this.numCasillaActual=jugador.numCasillaActual;
        this.puedeComprar=jugador.puedeComprar;
        this.saldo=jugador.saldo;
        this.salvoconducto=jugador.salvoconducto;
        this.propiedades=jugador.propiedades;
    }
    
    int cantidadCasasHoteles(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //Usamos compareTo con los saldos
    public int compareTo(Jugador otro){
        Float auxiliar=otro.getSaldo();
        Float saldoThis=saldo;
        int res = saldoThis.compareTo(auxiliar);
        return res;
    }
    
    boolean comprar(TituloPropiedad titulo){
        boolean result=false;
        if(encarcelado){
            return result;
        }
        
        if(puedeComprar){
            float precio=titulo.getPrecioCompra();
            if(puedoGastar(precio)){
                result=titulo.comprar(this);
                if(result){
                    propiedades.add(titulo);
                    Diario.getInstance().ocurreEvento("El jugador" + nombre +
                       "compra la propiedad " +titulo.toString());
                }
            }
            puedeComprar=false;
        }
        return result;
    }
    
    boolean construirCasa(int ip){
        boolean result=false;
        boolean puedoEdificarCasa=false;
        if(encarcelado){
            return result;
        }
        
        else if(!encarcelado){
            boolean existe=existeLaPropiedad(ip);
            if(existe){
                TituloPropiedad propiedad=propiedades.get(ip);
                puedoEdificarCasa=puedoEdificarCasa(propiedad);
                    if(puedoEdificarCasa){
                        result=propiedad.construirCasa(this);
                        if(result){
                            Diario.getInstance().ocurreEvento("El jugador "+nombre+" construye casa en la propiedad"+ip);
                        }
                    }
            }
        }
        return result;
    }
    
    boolean construirHotel(int ip){
        boolean result=false;
        if(encarcelado){
            return result;
        }
        if(existeLaPropiedad(ip)){
            TituloPropiedad propiedad=propiedades.get(ip);
            boolean puedoEdificarHotel=puedoEdificarHotel(propiedad);
            puedoEdificarHotel=false;
            float precio=propiedad.getPrecioEdificar();
            if(puedoGastar(precio)){
                puedoEdificarHotel=puedoEdificarHotel(propiedad);
            }
            if(puedoEdificarHotel){
                result=propiedad.construirHotel(this);
                CasasPorHotel=getCasasPorHotel()+1;
                boolean derruidas=propiedad.derruirCasas(CasasPorHotel,this);
                Diario.getInstance().ocurreEvento("El jugador "+nombre+
                    " construye hotel en la propiedad "+ip);
            }
        }
        return result;
    }
    
    protected boolean debeSerEncarcelado(){
        boolean resultado=true;
        if(isEncarcelado()||tieneSalvoconducto()){
          resultado=false;
        }
        return resultado;
    }
    
    boolean enBancarrota(){
        return saldo<0;
    }
    
    boolean encarcelar(int numCasillaCarcel){
        boolean resultadoFuncion=false;
        if(debeSerEncarcelado()){
            resultadoFuncion=moverACasilla(numCasillaCarcel);
            if(resultadoFuncion){
                encarcelado=true;
                Diario.getInstance().ocurreEvento("El jugador " +nombre+" ha sido encarcelado");
            }   
        }
        return resultadoFuncion;   
    }
        
    
    boolean existeLaPropiedad(int ip){
        boolean resultado=false;
        if(propiedades.get(ip)!=null){
            resultado=true;
        }
        return resultado;
    }
    int getCasasMax(){
        return CasasMax;
    }
    
    int getCasasPorHotel(){
        return CasasPorHotel;
    }
    
    int getHotelesMax(){
        return HotelesMax;
    }
    
    protected String getNombre(){
        return nombre;
    }
    
    public int getNumCasillaActual(){
        return numCasillaActual;
    }
    
    float getPrecioLibertad(){
        return PrecioLibertad;
    }
    
    float getPremioPasoSalida(){
        return PasoPorSalida;
    }
    
    public ArrayList<TituloPropiedad> getPropiedades(){
        return propiedades;
    }
    
    boolean getPuedeComprar(){
        return puedeComprar;
    }
    
    protected float getSaldo(){
        return saldo;
    }
    
    boolean hipotecar(int ip){
        boolean result=false;
        if(encarcelado){
            return result;
        }
        if(existeLaPropiedad(ip)){
            TituloPropiedad propiedad;
            propiedad=propiedades.get(ip);
            result=propiedad.hipotecar(this);
        }
        if(result){
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" hipoteca la propiedad "+ip);
        }
        return result;
    }
    
    public boolean isEncarcelado(){
        return encarcelado;
    }
    
    boolean modificarSaldo(float cantidad){
        saldo+=cantidad;
        Diario.getInstance().ocurreEvento("El saldo del jugador "+nombre+" ha sido modificado en "+cantidad);
        return true;
    }
    public ArrayList<String> getNombresPropiedades(){
        ArrayList<String> nombres= new ArrayList<String>();
        for(int i=0;i<propiedades.size();i++){
            nombres.add(propiedades.get(i).getNombre());
        }
        return nombres;
    }
    
    boolean moverACasilla(int numCasilla){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            numCasillaActual=numCasilla;
            puedeComprar=false;
            resultado=true;
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" se ha movido a la casilla "+numCasilla);
        }
        return resultado;
    }
    
    boolean obtenerSalvoconducto(Sorpresa sorpresa){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            resultado=true;
            salvoconducto=sorpresa;
        }
        return resultado;
    }
    
    boolean paga(float cantidad){
        return modificarSaldo(cantidad*(-1));
    }
    
    boolean pagaAlquiler(float cantidad){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            resultado=paga(cantidad);
        }
        return resultado;
    }
    
    boolean pagaImpuesto(float cantidad){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            resultado=paga(cantidad);
        }
        return resultado;
    }
    
    boolean pasaPorSalida(){
        boolean resultado=modificarSaldo(PasoPorSalida);
        Diario.getInstance().ocurreEvento("El jugador "+nombre+" ha pasado por la salida");
        return true;
    }
    
    private void perderSalvoconducto(){
        salvoconducto.usada();
        salvoconducto=null;
    }
    
    boolean puedeComprarCasilla(){
        if(isEncarcelado()){
            puedeComprar=false;
        }
        else{
            puedeComprar=true;
        }
        return puedeComprar;
    }
    
    private boolean puedeSalirCarcelPagando(){
        return saldo >=PrecioLibertad;
    }
    
    private boolean puedoEdificarCasa(TituloPropiedad propiedad){
        boolean resultado=false;
        float precio=propiedad.getPrecioEdificar();
        if(puedoGastar(precio) && propiedad.getNumCasas()<getCasasMax()){
            resultado=true;
        }
        return resultado;
    }
    
    private boolean puedoEdificarHotel(TituloPropiedad propiedad){
        boolean resultado=false;
        if(propiedad.getNumHoteles()<propiedad.getHotelesMax() && 
            propiedad.getNumCasas()>=propiedad.getCasasPorHotel()){
            resultado=true;
        }
        return resultado;
    }
    
    private boolean puedoGastar(float precio){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            if(getSaldo()>=precio){
                resultado=true;
            }
            else{
                resultado=false;
            }
        }
        return resultado;
    }
    
    boolean recibe(float cantidad){
        boolean resultado;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            resultado=modificarSaldo(cantidad);
        }
        return resultado;
    }
    
    boolean salirCarcelPagando(){
        if(isEncarcelado() && puedeSalirCarcelPagando()){
            paga(PrecioLibertad);
            encarcelado=false;
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" ha salido de la carcel pagando");
        }
        return true;
    }
    
    boolean salirCarcelTirando(){
        boolean resultado;
        if(isEncarcelado() && Dado.getInstance().salgoDeLaCarcel()){
            encarcelado=false;
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" ha salido de la carcel tirando");
        }
        else{
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" NO ha conseguido salir");
        }
        return encarcelado;
    }
    
    boolean tieneAlgoQueGestionar(){
        boolean resultado=false;
        if(propiedades.size()!=0){
            resultado=true;
        }
        return resultado;
    }
    
    boolean tieneSalvoconducto(){
        boolean resultado=false;
        if(salvoconducto!=null){
            resultado=true;
        }
        return resultado;
    }
    
    public String toString(){
        String cadena="Nombre: "+nombre ;
        cadena+=" Encarcelado: "+encarcelado;
        cadena+=" Casilla actual: "+numCasillaActual;
        cadena+=" Saldo del jugador: "+saldo ;
        cadena+=" Puede comprar: "+puedeComprar;
        cadena+=" Numero de propiedades: "+propiedades.size()+"\n";
        return cadena;
    }
    
    boolean vender(int ip){
        boolean resultado=false;
        if(isEncarcelado()){
            resultado=false;
        }
        else{
            if(existeLaPropiedad(ip)){
                resultado=propiedades.get(ip).vender(this);
                if(resultado){
                    propiedades.remove(ip);
                    Diario.getInstance().ocurreEvento("Se ha vendido la propiedad "+ip+" del jugador "+nombre);
                }
            }
        }
        return resultado;
    }
    
    boolean cancelarHipoteca(int ip){
        boolean result=false;
        if(isEncarcelado()){
            return result;
        }       
        if(existeLaPropiedad(ip)){
            TituloPropiedad propiedad=propiedades.get(ip);
            float cantidad=propiedad.getImporteCancelarHipoteca();
            boolean puedoGastar=puedoGastar(cantidad);
            if(puedoGastar){
                result=propiedad.cancelarHipoteca(this);
                if(result){
                    Diario.getInstance().ocurreEvento("El jugador"+nombre+
                            "cancela la hipoteca de la propiedad" +ip);
                }
            }
        }     
        return result;
    }
    
};
