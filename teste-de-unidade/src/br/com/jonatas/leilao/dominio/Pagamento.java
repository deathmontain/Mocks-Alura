package br.com.jonatas.leilao.dominio;

import lombok.Data;

import java.util.Calendar;

@Data
public class Pagamento {

	private double valor;
	private Calendar data;
}
