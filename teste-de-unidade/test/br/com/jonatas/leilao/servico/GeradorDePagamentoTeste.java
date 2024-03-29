package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.builder.CriadorDeLeilao;
import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.dominio.Pagamento;
import br.com.jonatas.leilao.dominio.Usuario;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import br.com.jonatas.leilao.infra.dao.PagamentosDao;
import br.com.jonatas.leilao.infra.relogio.Relogio;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Calendar;

import static org.mockito.Mockito.*;

public class GeradorDePagamentoTeste {
    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado(){
        LeilaoDao leiloes = mock(LeilaoDao.class);
        PagamentosDao pagamentos = mock(PagamentosDao.class);
        Avaliador avaliador = mock(Avaliador.class);

        Leilao leilao = new CriadorDeLeilao().para("Nintendo")
                .lance(new Usuario("Joaozinho"), 2000.0)
                .lance(new Usuario("Luluzinha"), 2500.0)
                .constroi();

        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));
        when(avaliador.getMaiorLance()).thenReturn(2500.0);

        GeradorDePagamento geradorDePagamento = new GeradorDePagamento(leiloes, pagamentos, avaliador);
        geradorDePagamento.gera();

        ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());

        Pagamento pagamentoGerado = argumento.getValue();

        assertEquals(2500.0, pagamentoGerado.getValor(), 0.00001);
    }

    @Test
    public void deveEmpurrarParaOProximoDiaUtil(){
        LeilaoDao leiloes = mock(LeilaoDao.class);
        PagamentosDao pagamentos = mock(PagamentosDao.class);
        Relogio relogio = mock(Relogio.class);

        Leilao leilao = new CriadorDeLeilao().para("Nintendo Switch")
                .lance(new Usuario("Joaozinho"), 2000.0)
                .lance(new Usuario("Luluzinha"), 2500.0)
                .constroi();

        when(leiloes.encerrados()).thenReturn(Arrays.asList(leilao));

        Calendar sabado = Calendar.getInstance();
        sabado.set(2012, Calendar.APRIL, 7);

        when(relogio.hoje()).thenReturn(sabado);

        GeradorDePagamento geradorDePagamento = new GeradorDePagamento(leiloes, pagamentos, new Avaliador(), relogio);
        geradorDePagamento.gera();

        ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
        verify(pagamentos).salva(argumento.capture());
        Pagamento pagamentoGerado = argumento.getValue();

        assertEquals(Calendar.MONDAY, pagamentoGerado.getData().get(Calendar.DAY_OF_WEEK));
        assertEquals(9, pagamentoGerado.getData().get(Calendar.DAY_OF_MONTH));
    }
}