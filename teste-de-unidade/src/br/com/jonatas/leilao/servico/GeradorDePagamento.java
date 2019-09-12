package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.dominio.Pagamento;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import br.com.jonatas.leilao.infra.dao.PagamentosDao;
import br.com.jonatas.leilao.infra.relogio.Relogio;
import br.com.jonatas.leilao.infra.relogio.RelogioDoSistema;

import java.util.Calendar;
import java.util.List;

public class GeradorDePagamento {
    private final LeilaoDao leiloes;
    private final Avaliador avaliador;
    private final PagamentosDao pagamentos;
    private final Relogio relogio;

    public GeradorDePagamento(LeilaoDao leiloes, PagamentosDao pagamentos, Avaliador avaliador, Relogio relogio) {
        this.leiloes = leiloes;
        this.avaliador = avaliador;
        this.pagamentos = pagamentos;
        this.relogio = relogio;
    }

    public GeradorDePagamento(LeilaoDao leiloes, PagamentosDao pagamentos, Avaliador avaliador) {
        this(leiloes, pagamentos, avaliador, new RelogioDoSistema());
    }

    public void gera(){
        List<Leilao> leiloesEncerrados = this
                .leiloes.encerrados();

        for (Leilao leilao : leiloesEncerrados) {
            this.avaliador.avalia(leilao);

            Pagamento novoPagamento = new Pagamento(avaliador.getMaiorLance(), primeiroDiaUtil());
            this.pagamentos.salva(novoPagamento);
        }
    }

    private Calendar primeiroDiaUtil() {
        Calendar data = Calendar.getInstance();
        int diaSemana = data.get(Calendar.DAY_OF_WEEK);

        if(diaSemana == Calendar.SATURDAY) data.add(Calendar.DAY_OF_MONTH, 2);
        else if(diaSemana == Calendar.SUNDAY) data.add(Calendar.DAY_OF_MONTH, 1);

        return data;
    }
}