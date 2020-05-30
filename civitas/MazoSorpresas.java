package civitas;
import java.util.ArrayList;
import java.util.Collections;


public class MazoSorpresas {
    //Atributos de clase
    private ArrayList<Sorpresa> sorpresas;
    private boolean barajada;
    private int usadas;
    private boolean debug;
    private ArrayList<Sorpresa> cartasEspeciales;
    private Sorpresa ultimaSorpresa;
    
    //Meotodo que incializa valores
    private void init(){
        sorpresas=new ArrayList();
        cartasEspeciales=new ArrayList();
        barajada=false;
        usadas=0;
        debug=false;
    }
    
    //Constructor con parametro
    MazoSorpresas(boolean debug){  
        init();
        this.debug=debug;
        if(debug){
            Diario.getInstance().ocurreEvento("El modo debug esta activado\n");
        }
    }
    
    //Constructor sin parametros
    MazoSorpresas(){
        init();
        debug=false;
    }
    
    //Metodos con visibilidad de paquete
    void alMazo(Sorpresa s){
        if(!barajada){
            sorpresas.add(s);
        }
    }
    
    Sorpresa siguiente(){
        if((!barajada || usadas==sorpresas.size()) && !debug){
            usadas=0;
            Collections.shuffle(sorpresas);
            barajada=true;
        }
        
        //Incrementamos el valor de usadas
        usadas++;
        ultimaSorpresa=sorpresas.get(0);
        sorpresas.add(ultimaSorpresa);
        sorpresas.remove(0);
        
        return ultimaSorpresa;
    }
    
    void inhabilitarCartaEspecial(Sorpresa sorpresa){
        boolean encontrado=false;
        for(int i=0;i<sorpresas.size() && !encontrado;i++){
            if(sorpresas.get(i)==sorpresa){
                encontrado=true;
                cartasEspeciales.add(sorpresas.get(i));
                sorpresas.remove(i);
                
                Diario.getInstance().ocurreEvento("Se ha inhabilitado una carta "
                + "especial\n");
            }
        }
        
    }
    
    void habilitarCartaEspecial(Sorpresa sorpresa){
        boolean encontrado=false;
        for(int i=0;i<cartasEspeciales.size() && !encontrado;i++){
            if(cartasEspeciales.get(i)==sorpresa){
                encontrado=true;
                
                sorpresas.add(sorpresas.get(i));
                cartasEspeciales.remove(i);
   
                Diario.getInstance().ocurreEvento("Se ha habilitado una carta"
                        + "especial");
            }
            
        }
        
    }
    
    //~getUltimaSorpresa() : Sorpresa
    Sorpresa getUltimaSorpresa(){
        return ultimaSorpresa;
    }
     
}
