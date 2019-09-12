package br.com.jonatas.leilao.infra.dao;

import br.com.jonatas.leilao.dominio.Leilao;

public interface Carteiro {
    void envia(Leilao leilao);
}
