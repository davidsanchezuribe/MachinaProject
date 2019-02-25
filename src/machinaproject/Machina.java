package machinaproject;

/**
 *
 * @author
 */
public class Machina {

    private int numBitsPotencia;
    private int numBitsMantisa;

    public Machina() {
        this(4, 8);
    }

    public Machina(int numBitsPotencia, int numBitsMantisa) {
        this.numBitsPotencia = numBitsPotencia;
        this.numBitsMantisa = numBitsMantisa;
    }

    public int getNumBitsPotencia() {
        return numBitsPotencia;
    }

    public int getNumBitsMantisa() {
        return numBitsMantisa;
    }

    public double getOverflow() {
        String overflow = "11";
        for (int i = 0; i < numBitsPotencia + numBitsMantisa; i++) {
            overflow += "1";
        }
        return machine2Real(overflow);
    }

    public double getEpsilon() {
        String underflow = "10";
        for(int i = 0; i < numBitsMantisa; i++){
            underflow += "0";
        }
        
        int camposMantisa = numBitsMantisa - 1;
        String exponente = "";
        while (camposMantisa > 1) {
            if (camposMantisa % 2 == 0) {
                exponente = "0" + exponente;
            } else {
                exponente = "1" + exponente;
            }
            camposMantisa = (int) (camposMantisa / 2);
        }
        if (camposMantisa == 1) {
            exponente = "1" + exponente;
        }
        for(int i = 0; i < numBitsPotencia - exponente.length(); i++){
            exponente = "0" + exponente;
        }
        
        System.out.println("exponente" + exponente);
        return machine2Real(underflow + exponente);
    }

    public double getUnderflow() {
        String underflow = "10";
        for(int i = 0; i < numBitsMantisa; i++){
            underflow += "0";
        }
        for(int i = 0; i < numBitsPotencia; i++){
            underflow += "1";
        }
        return machine2Real(underflow);
    }

    public void updateMachina(int numBitsPotencia, int numBitsMantisa) {
        this.numBitsPotencia = numBitsPotencia;
        this.numBitsMantisa = numBitsMantisa;
    }

    public String real2Machine(double real) {
        int parteEntera = (int) real;
        String signoMantisa;
        if (real < 0) {
            signoMantisa = "0";
        } else {
            signoMantisa = "1";
        }
        parteEntera = Math.abs(parteEntera);

        double parteDecimal = real % 1;
        String parteEnteraBinaria = "";

        while (parteEntera > 1) {
            if (parteEntera % 2 == 0) {
                parteEnteraBinaria = "0" + parteEnteraBinaria;
            } else {
                parteEnteraBinaria = "1" + parteEnteraBinaria;
            }
            parteEntera = (int) (parteEntera / 2);
        }
        if (parteEntera == 1) {
            parteEnteraBinaria = "1" + parteEnteraBinaria;
        }
        String parteDecimalBinaria = "";
        if (parteEnteraBinaria.length() > 0) {
            for (int i = 0; i < numBitsMantisa - parteEnteraBinaria.length() + 1; i++) {
                parteDecimal *= 2;
                if ((int) parteDecimal == 0) {
                    parteDecimalBinaria += "0";
                } else {
                    parteDecimalBinaria += "1";
                }
                parteDecimal %= 1;
            }
        } else {
            int cont = 0;
            boolean incrementar = false;
            while (cont < numBitsMantisa + 1) {
                parteDecimal *= 2;
                if ((int) parteDecimal == 0) {
                    parteDecimalBinaria += "0";
                } else {
                    parteDecimalBinaria += "1";
                    incrementar = true;
                }
                parteDecimal %= 1;
                if (incrementar) {
                    cont++;
                }

            }
        }

        int exponenteDecimal = 0;
        if (parteEnteraBinaria.length() > 0) {

            exponenteDecimal = numBitsMantisa + 1;

            exponenteDecimal = parteEnteraBinaria.length();

        } else {
            exponenteDecimal = -1 * parteDecimalBinaria.indexOf("1");
        }
        if (parteEnteraBinaria.length() > 0) {
            parteEnteraBinaria = parteEnteraBinaria.substring(1);
        } else {
            parteDecimalBinaria = (parteDecimalBinaria.substring(parteDecimalBinaria.indexOf("1") + 1));
        }
        String mantisa = parteEnteraBinaria + parteDecimalBinaria;
        if (mantisa.length() > numBitsMantisa) {
            mantisa = mantisa.substring(0, numBitsMantisa);
        }
        String signoExponente;
        if (exponenteDecimal < 0) {
            signoExponente = "0";
        } else {
            signoExponente = "1";
        }
        exponenteDecimal = Math.abs(exponenteDecimal);
        String exponente = "";
        while (exponenteDecimal > 1) {
            if (exponenteDecimal % 2 == 0) {
                exponente = "0" + exponente;
            } else {
                exponente = "1" + exponente;
            }
            exponenteDecimal = (int) (exponenteDecimal / 2);
        }
        if (exponenteDecimal == 1) {
            exponente = "1" + exponente;
        }
        while (exponente.length() < this.numBitsPotencia) {
            exponente = "0" + exponente;
        }
        if (exponente.length() > this.numBitsPotencia) {
            exponente = exponente.substring(exponente.length() - numBitsPotencia);
        }

        return signoMantisa + signoExponente + mantisa + exponente;
    }

    public double machine2Real(String machineNumber) {
        // Encontrar la potencia
        int potencia = 0;
        for (int i = 2 + numBitsMantisa; i < 2 + numBitsMantisa + numBitsPotencia; i++) {
            if (machineNumber.charAt(i) == '1') {
                potencia += Math.pow(2, machineNumber.length() - i - 1);
            }
        }
        if (machineNumber.charAt(1) == '0') {
            potencia *= -1;
        }

        // Encontrar el valor de la mantisa potenciada
        double result = Math.pow(2, potencia - 1);
        for (int i = 2; i < 2 + numBitsMantisa; i++) {
            if (machineNumber.charAt(i) == '1') {
                result += Math.pow(2, potencia - i);
            }
        }

        // Signo de la mantisa
        if (machineNumber.charAt(0) == '0') {
            result *= -1;
        }
        return result;
    }

    public void validateMachineNumber(String machineNumber) throws Exception {
        int numChar = numBitsPotencia + numBitsMantisa + 2;
        if (machineNumber.length() != numChar) {
            throw new Exception("El número de caracteres en el número máquina debe ser: " + numChar);
        }
    }
}
