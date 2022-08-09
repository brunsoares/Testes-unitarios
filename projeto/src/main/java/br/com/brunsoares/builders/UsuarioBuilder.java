package br.com.brunsoares.builders;

import br.com.brunsoares.locacao_filmes.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder(){}

    public static UsuarioBuilder novoUsuario(){
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("Bruno Soares");
        return builder;
    }

    public UsuarioBuilder comNome(String nome){
        usuario.setNome(nome);
        return this;
    }

    public Usuario retornarUsuario(){
        return usuario;
    }
}
