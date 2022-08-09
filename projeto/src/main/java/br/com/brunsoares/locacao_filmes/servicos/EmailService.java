package br.com.brunsoares.locacao_filmes.servicos;

import br.com.brunsoares.locacao_filmes.entidades.Usuario;

public interface EmailService {

    void notificarAtraso(Usuario usuario);
}
