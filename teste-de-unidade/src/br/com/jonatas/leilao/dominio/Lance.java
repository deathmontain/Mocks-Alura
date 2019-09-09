package br.com.jonatas.leilao.dominio;


import lombok.Data;

@Data
public class Lance {

	private Usuario usuario;
	private double valor;
	private int id;
	
	public Lance(Usuario usuario, double valor) {
		this.usuario = usuario;
		this.valor = valor;
	}
}
