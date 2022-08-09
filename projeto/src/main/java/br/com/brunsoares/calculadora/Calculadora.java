package br.com.brunsoares.calculadora;

import br.com.brunsoares.exceptions.DivisaoPorZeroException;

public class Calculadora {

    public int somar(int x, int y){
        return x + y;
    }

    public int subtrair(int x, int y) {
        return x - y;
    }

    public int multiplicar(int x, int y) {
        return x * y;
    }

    public int dividir(int x, int y) throws DivisaoPorZeroException {
        if(y == 0){
            throw new DivisaoPorZeroException();
        }
        return x / y;
    }


}
