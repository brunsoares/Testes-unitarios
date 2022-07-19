package br.com.brunsoares.utils;

import br.com.brunsoares.locacao_filmes.entidades.Filme;

public class CalculosDesconto {

    public Double descontoDe25PorcentoNoTerceiroFilme(Filme filme){
        Double valorTotal = filme.getPrecoLocacao();
        Double calculo = valorTotal * 0.25;
        return valorTotal - calculo;
    }

    public Double descontoDe50PorcentoNoQuartoFilme(Filme filme){
        Double valorTotal = filme.getPrecoLocacao();
        Double calculo = valorTotal * 0.50;
        return valorTotal - calculo;
    }

    public Double descontoDe75PorcentoNoQuintoFilme(Filme filme){
        Double valorTotal = filme.getPrecoLocacao();
        Double calculo = valorTotal * 0.75;
        return valorTotal - calculo;
    }

    public Double descontoDe100PorcentoNoSextoFilme(){
        return (double) 0;
    }

}
