package br.com.jonatas.leilao.servico;

import br.com.jonatas.leilao.dominio.Leilao;
import br.com.jonatas.leilao.infra.dao.Carteiro;
import br.com.jonatas.leilao.infra.dao.LeilaoDao;

import java.util.Calendar;
import java.util.List;

public class EncerradorDeLeilao {

	private int total = 0;
	private final LeilaoDao dao;
	private final Carteiro carteiro;

	public EncerradorDeLeilao(LeilaoDao dao, Carteiro carteiro){
		this.carteiro = carteiro;
		this.dao = dao;
	}

	public void encerra() {
		List<Leilao> todosLeiloesCorrentes = dao.correntes();

		for (Leilao leilao : todosLeiloesCorrentes) {
			try {
				if (comecouSemanaPassada(leilao)) {
					leilao.encerra();
					total++;
					dao.atualiza(leilao);
					carteiro.envia(leilao);
				}
			} catch (Exception e) {
//				Loga e a exceção continua
			}
		}
	}

	private boolean comecouSemanaPassada(Leilao leilao) {
		return diasEntre(leilao.getData(), Calendar.getInstance()) >= 7;
	}

	private int diasEntre(Calendar inicio, Calendar  fim) {
		Calendar data = (Calendar) inicio.clone();
		int diasNoIntervalo = 0;
		while (data.before(fim)) {
			data.add(Calendar.DAY_OF_MONTH, 1);
			diasNoIntervalo++;
		}

		return diasNoIntervalo;
	}

	public int getTotalEncerrados() {
		return total;
	}
}