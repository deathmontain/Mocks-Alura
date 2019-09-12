package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.dominio.Pagamento;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import br.com.jonatas.leilao.infra.dao.RepositorioDePagamentos;

import java.util.Calendar;
import java.util.List;

public class GeradorDePagamento {
    private final LeilaoDao leiloes;
    private final Avaliador avaliador;
    private final RepositorioDePagamentos pagamentos;

    public GeradorDePagamento(LeilaoDao leiloes, RepositorioDePagamentos pagamentos, Avaliador avaliador) {
        this.leiloes = leiloes;
        this.avaliador = avaliador;
        this.pagamentos = pagamentos;
    }

    public void gera(){
        List<Leilao> leiloesEncerrados = this
                .leiloes.encerrados();

        for (Leilao leilao : leiloesEncerrados) {
            this.avaliador.avalia(leilao);

            Pagamento novoPagamento = new Pagamento(avaliador.getMaiorLance(), Calendar.getInstance());
            this.pagamentos.salva(novoPagamento);
        }
    }
}