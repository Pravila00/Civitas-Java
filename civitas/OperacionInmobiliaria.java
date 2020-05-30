package civitas;

/**
 *
 * @author pablo
 */

public class OperacionInmobiliaria {
    //Atributos
    private GestionesInmobiliarias operacion;
    private int indicePropiedad;
    
    //Constructor
    public OperacionInmobiliaria (int indice, GestionesInmobiliarias gest){
        indicePropiedad=indice;
        operacion=gest;
    }
    
    //Consultores
    public GestionesInmobiliarias getOperacion(){
        return operacion;
    }
    
    public int getIndicePropiedad(){
        return indicePropiedad;
    }
}
