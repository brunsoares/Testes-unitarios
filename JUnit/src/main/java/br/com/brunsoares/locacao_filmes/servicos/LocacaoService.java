package br.com.brunsoares.locacao_filmes.servicos;

import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;

import java.util.Date;
import java.util.List;

import static br.com.brunsoares.utils.DataUtils.adicionarDias;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		checarEstoque(filmes);

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotalFilmes = getValorTotalFilmes(filmes);
		locacao.setValor(valorTotalFilmes);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		return locacao;
	}

	private Double getValorTotalFilmes(List<Filme> filmes) {
		Double soma = (double) 0;
		for(Filme filme: filmes) {
			soma += filme.getPrecoLocacao();
		}
		return soma;
	}

	private void checarEstoque(List<Filme> filmes) throws Exception {
		for(Filme filme: filmes) {
			if (filme.getEstoque() == 0) {
				throw new Exception("Filme sem estoque!");
			}
		}
	}

}