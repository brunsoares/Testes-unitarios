package br.com.brunsoares.entidades;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {

	private String nome;
	
	public Usuario() {}
	
	public Usuario(String nome) {
		this.nome = nome;
	}

}