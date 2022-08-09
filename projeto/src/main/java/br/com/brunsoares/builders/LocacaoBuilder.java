package br.com.brunsoares.builders;

import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.utils.DataUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LocacaoBuilder {

    private Locacao locacao;

    private LocacaoBuilder(){}

    public static LocacaoBuilder umaLocacao(){
        LocacaoBuilder builder = new LocacaoBuilder();
        iniciarComDadosPadroes(builder);
        return builder;
    }

    private static void iniciarComDadosPadroes(LocacaoBuilder builder) {
        builder.locacao = new Locacao();
        Locacao elemento = builder.locacao;

        elemento.setUsuario(UsuarioBuilder.novoUsuario().retornarUsuario());
        elemento.setFilmes(Arrays.asList(FilmeBuilder.novoFilme("Toy Story", 20.00).retornarFilme()));
        elemento.setDataLocacao(new Date());
        elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
        elemento.setValor(20.00);
    }

    public LocacaoBuilder comUsuario(Usuario usuario){
        locacao.setUsuario(usuario);
        return this;
    }

    public LocacaoBuilder comListaFilmes(List<Filme> filmes){
        locacao.setFilmes(filmes);
        return this;
    }

    public LocacaoBuilder comDataLocacao(Date dataLocacao){
        locacao.setDataRetorno(dataLocacao);
        return this;
    }

    public LocacaoBuilder comDataRetorno(Date dataRetorno){
        locacao.setDataRetorno(dataRetorno);
        return this;
    }

    public LocacaoBuilder comValor(Double valor){
        locacao.setValor(valor);
        return this;
    }

    public Locacao retornarLocacao(){
        return locacao;
    }
}
