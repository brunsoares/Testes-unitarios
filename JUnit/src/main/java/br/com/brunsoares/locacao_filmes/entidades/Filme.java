package br.com.brunsoares.locacao_filmes.entidades;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Filme {

	private String nome;
	private Integer estoque;
	private Double precoLocacao;  
	
	public Filme() {}
	
	public Filme(String nome, Integer estoque, Double precoLocacao) {
		this.nome = nome;
		this.estoque = estoque;
		this.precoLocacao = precoLocacao;
	}

	public List<String> getNomeFilmes(List<Filme> filmes){
		List<String> listaNomeFilmes = new ArrayList<String>();
		for(Filme filme: filmes){
			listaNomeFilmes.add(filme.getNome());
		}
		return listaNomeFilmes;
	}

}