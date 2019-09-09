package br.com.jonatas.leilao.dominio;

import lombok.Data;

@Data
public class Usuario {

	private int id;
	private String nome;
	
	public Usuario(String nome) {
		this(0, nome);
	}

	public Usuario(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
}
