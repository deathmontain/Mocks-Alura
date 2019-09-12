package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.builder.CriadorDeLeilao;
import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.infra.dao.Carteiro;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.*;

public class EncerradorDeLeilaoTeste {
    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV 4k").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Fogao elétrico").naData(antiga).constroi();
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

        LeilaoDao daoFalso = mock(LeilaoDao.class);
        Carteiro carteiroFalso = mock(Carteiro.class);
        when(daoFalso.correntes()).thenReturn(leiloesAntigos);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        Assert.assertEquals(2, encerrador.getTotalEncerrados());
        Assert.assertTrue(leilao1.isEncerrado());
        Assert.assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void deveAtualizarLeiloesEncerrados(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("Tv de plasma").naData(antiga).constroi();

        LeilaoDao daoFalso = mock(LeilaoDao.class);
        Carteiro carteiroFalso = mock(Carteiro.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        verify(daoFalso, times(1)).atualiza(leilao1);
    }

    @Test
    public void deveContinuarAExecucaoMesmoQUandoDaoFalha(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV 4k").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Fogao elétrico").naData(antiga).constroi();
        Leilao leilao3 = new CriadorDeLeilao().para("Playstatiob").naData(antiga).constroi();
        Leilao leilao4 = new CriadorDeLeilao().para("Cama box King").naData(antiga).constroi();

        LeilaoDao daoFalso = mock(LeilaoDao.class);
        Carteiro carteiroFalso = mock(Carteiro.class);
        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2, leilao3, leilao4));
        doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
        encerrador.encerra();

        verify(daoFalso).atualiza(leilao2);
        verify(carteiroFalso).envia(leilao2);
        verify(carteiroFalso, never()).envia(any(Leilao.class));
    }
}