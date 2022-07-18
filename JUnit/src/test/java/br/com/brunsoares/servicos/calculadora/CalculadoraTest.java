package br.com.brunsoares.servicos.calculadora;

import br.com.brunsoares.calculadora.Calculadora;
import br.com.brunsoares.exceptions.DivisaoPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private  Calculadora calculadora;

    @Before
    public void iniciarTests(){
       calculadora = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        int x = 10;
        int y = 25;
        int resultado = calculadora.somar(x, y);

        Assert.assertEquals(35, resultado);
    }

    @Test
    public void deveSubtrairDoisValores(){
        int x = 45;
        int y = 25;
        int resultado = calculadora.subtrair(x, y);

        Assert.assertEquals(20, resultado);
    }

    @Test
    public void deveMultiplicarDoisValores(){
        int x = 10;
        int y = 50;
        int resultado = calculadora.multiplicar(x, y);

        Assert.assertEquals(500, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws DivisaoPorZeroException {
        int x = 10;
        int y = 5;
        int resultado = calculadora.dividir(x, y);

        Assert.assertEquals(2, resultado);
    }

    @Test(expected = DivisaoPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws DivisaoPorZeroException {
        int x = 10;
        int y = 0;
        calculadora.dividir(10, 0);
    }

}
