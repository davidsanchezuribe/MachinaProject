
package machinaproject;


public class Control {
    private Machina machina;
    
    public void start(){
        Control control = this;
        this.machina = new Machina();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz(control).setVisible(true);
            }
        });
    }
    
    public int getNumBitsPotencia() {
        return machina.getNumBitsPotencia();
    }

    public int getNumBitsMantisa() {
        return machina.getNumBitsMantisa();
    }
    
    public double getOverflow(){
        return machina.getOverflow();
    }
    
    public double getEpsilon(){
        return machina.getEpsilon();
    }
    
    public double getUnderflow(){
        return machina.getUnderflow();
    }
    
    public void updateMachine(String expo, String mant) throws Exception{
        int numExpo;
        int numMant;
        
        try{
            numExpo = Integer.parseInt(expo);
        } catch(NumberFormatException e){
            throw new Exception("El número de bits del exponente debe ser un entero");
        }
        
        try{
            numMant = Integer.parseInt(mant);
        } catch(NumberFormatException e){
            throw new Exception("El número de bits de la mantisa debe ser un entero");
        }
        
        if(numExpo < 1 || numExpo > 7){
            throw new Exception("El número de bits del exponente debe estar entre 1 y 7");
        }
        
        if(numMant < 1 || numMant > 23){
            throw new Exception("El número de bits de la mantisa debe estar entre 1 y 23");
        }
        
        machina.updateMachina(numExpo, numMant);
                
    }
    
    public String real2Machine(String real) throws Exception{
        double realNumber;
        real = real.replace(',', '.');
        int cont = 0;
        for(int i = 0; i < real.length(); i++){
            if(real.charAt(i) == '.'){
                cont++;
            }
        }
        
        if(cont > 1){
            throw new Exception("Ingrese solo un punto o una coma");
        }
        
        try{
            realNumber = Double.parseDouble(real);
        } catch(NumberFormatException e){
            throw new Exception("Ingrese un número real válido");
        }
        return machina.real2Machine(realNumber);
    }
    
    public double machine2Real(String machineNum) throws Exception{
        for(int i = 0; i < machineNum.length(); i++){
            if(machineNum.charAt(i) != '0' && machineNum.charAt(i) != '1'){
                throw new Exception("El número máquina debe contener solo ceros y unos");
            }
        }
        machina.validateMachineNumber(machineNum);
        return machina.machine2Real(machineNum);
    }
}
