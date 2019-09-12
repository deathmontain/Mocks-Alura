package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.builder.CriadorDeLeilao;
import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.dominio.Pagamento;
import br.com.jonatas.leilao.dominio.Usuario;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import br.com.jonatas.leilao.infra.dao.RepositorioDePagamentos;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class GeradorDePagamentoTeste {
    @Test
    public void deveGerarPagamentoParaUmLeilaoEncerrado(){
        LeilaoDao leiloes = mock(LeilaoDao.class);
        RepositorioDePagamentos pagamentos = mock(RepositorioDePagamentos.class);
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

        Assert.assertEquals(2500.0, pagamentoGerado.getValor(), 0.00001);
    }
}