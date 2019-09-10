package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.builder.CriadorDeLeilao;
import br.com.jonatas.leilao.dominio.Leilao;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class EncerradorDeLeilaoTeste {
    @Test
    public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes(){
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Fogao elétrico").naData(antiga).constroi();

        EncerradorDeLeilao encerrador = new EncerradorDeLeilao();
        encerrador.encerra();

        Assert.assertTrue(leilao1.isEncerrado());
        Assert.assertTrue(leilao2.isEncerrado());
    }

}
