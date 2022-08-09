package br.com.brunsoares.builders;

import br.com.brunsoares.locacao_filmes.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder(){}

    public static FilmeBuilder novoFilme(String nomeFilme, Double precoFilme){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome(nomeFilme);
        builder.filme.setEstoque(1);
        builder.filme.setPrecoLocacao(precoFilme);
        return builder;
    }

    public static FilmeBuilder novoFilmeSemEstoque(String nomeFilme, Double precoFilme){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome(nomeFilme);
        builder.filme.setEstoque(0);
        builder.filme.setPrecoLocacao(precoFilme);
        return builder;
    }

    public Filme retornarFilme(){
        return filme;
    }
}
