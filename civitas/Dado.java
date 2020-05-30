package civitas;
import java.util.Random;

class Dado {
    private Random random;
    private int ultimoResultado;
    private Boolean debug;
    static private Dado instance= new Dado();
    static private int SALIDACARCEL=5;
    
    private Dado(){
        ultimoResultado=0;
        debug=false;
    }
    
    static public Dado getInstance(){
        return instance;
    }
    
    public int tirar(){
        int tirada;
        if(debug==false){
            random=new Random();
            
            tirada=random.nextInt(6);
            while(tirada==0){
                tirada=random.nextInt(6);
            }
        }
        else{
            tirada = 1;
        }
        ultimoResultado=tirada;
        return tirada;
    }
    
    public Boolean salgoDeLaCarcel(){
        Boolean bool=false;
        int numero;
        numero=tirar();
        if(numero>=SALIDACARCEL){
            bool=true;
        }
        return bool;
    }
    
    public int quienEmpieza(int n){
        random=new Random();
        return random.nextInt(n);
    }
    
    public void setDebug(Boolean d){
        debug=d;
    }
    public int getUltimoResultado(){
        return ultimoResultado;
    }
    
}
