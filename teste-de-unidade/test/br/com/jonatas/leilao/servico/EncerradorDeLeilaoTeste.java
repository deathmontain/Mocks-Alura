package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.builder.CriadorDeLeilao;
import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;
import br.com.jonatas.leilao.infra.dao.LeilaoDaoFalso;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

public class EncerradorDeLeilaoTeste {
    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV 4k").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Fogao elétrico").naData(antiga).constroi();

        LeilaoDaoFalso dao = new LeilaoDaoFalso();
        dao.salva(leilao1);
        dao.salva(leilao2);

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao();
        encerrador.encerra();

        List<Leilao> encerrados = dao.encerrados();

        Assert.assertEquals(2, encerrados.size());
        Assert.assertTrue(encerrados.get(0).isEncerrado());
        Assert.assertTrue(encerrados.get(1).isEncerrado());
    }

}
