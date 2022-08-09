package br.com.brunsoares.daos;

import br.com.brunsoares.locacao_filmes.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
