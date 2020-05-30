package civitas;

import java.util.ArrayList;


public class Tablero {
    private int numCasillaCarcel;
    
    private ArrayList<Casilla> casillas;
    
    private int porSalida;
    
    private Boolean tieneJuez;
    
    //Constructor
    public Tablero(int casillaCarcel){
        //Si el valor es aceptable lo incorporamos
        if(casillaCarcel >= 1 ){
            numCasillaCarcel=casillaCarcel;
        }
        //En caso contrario asignamos una por defecto
        else{
            numCasillaCarcel=1;
        }
        
        //Inicializamos casillas a null
        casillas=null;
        casillas= new ArrayList<>();
        //Aniadimos una casilla cuyo nombre es Salida
        Casilla Salida = null;
        Salida= new Casilla("Salida");
        casillas.add(Salida);
        porSalida=0;
        tieneJuez=false;  
    }
    
    //Metodo boolean correcto
    private Boolean correcto(){
        Boolean resultado=false;

        if(casillas.size() > numCasillaCarcel){
            resultado=true;
        }

        return resultado;
    }
    private Boolean correcto(int numCasilla){
        Boolean resultado=false;
        
        if(correcto() && numCasilla < casillas.size()){
            resultado=true;
        }
  
        return resultado;
        
    }
    
    public int getCarcel(){
        return numCasillaCarcel;
    }
    
    public int getPorSalida(){
        int Antes=porSalida;
        if(porSalida > 0){
            porSalida--;
        }
        return Antes;
    }
    
    public void añadeCasilla(Casilla casilla){
        if(casillas.size() == numCasillaCarcel){
            Casilla Carcel = null;
            Carcel= new Casilla("Carcel");
            casillas.add(Carcel);
        }
        casillas.add(casilla);
        
    }
    
    public void añadeJuez(){
        if(tieneJuez==false){
            tieneJuez=true;
            Casilla nueva=new Casilla(numCasillaCarcel,"Juez");
            casillas.add(nueva);
        }
    }
    
    public Casilla getCasilla(int numCasilla){
        Casilla resultado=null;
        if(correcto(numCasilla)){
            resultado = casillas.get(numCasilla);
        }
        return resultado;
    }
    
    public int nuevaPosicion(int actual,int tirada){
        int nuevaPosicion;
        //Si el tablero no es correcto se devuelve -1
        if(!correcto()){
            nuevaPosicion=-1;
        }
        //Calculamos la nueva posicion
        else{
            nuevaPosicion = actual+tirada;
            //Si llegamos a la ultima posicion del tablero
            if(nuevaPosicion >= casillas.size()){
                nuevaPosicion = nuevaPosicion % (casillas.size()-1);
                porSalida++;
            }
        }
        return nuevaPosicion;
        
    }
    public int calcularTirada(int origen,int destino){
        int tiradaDado;
        tiradaDado= destino-origen;
        
        //Si la tirada del dado es negativo
        if(tiradaDado < 0){
            tiradaDado+=casillas.size();           
        }
        return tiradaDado;
    }

}
