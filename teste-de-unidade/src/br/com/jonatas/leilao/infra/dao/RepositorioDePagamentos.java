package br.com.jonatas.leilao.infra.dao;

import br.com.jonatas.leilao.dominio.Pagamento;

public interface RepositorioDePagamentos {
    void salva(Pagamento pagamento);
}
