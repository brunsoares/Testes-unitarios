package br.com.brunsoares.locacao_filmes.servicos;

import br.com.brunsoares.daos.LocacaoDAO;
import br.com.brunsoares.exceptions.FilmeSemEstoqueException;
import br.com.brunsoares.exceptions.LocacaoException;
import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.utils.CalculosDesconto;
import br.com.brunsoares.utils.DataUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.brunsoares.utils.DataUtils.adicionarDias;

@Getter
@Setter
public class LocacaoService {

	private CalculosDesconto calculosDesconto = new CalculosDesconto();
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		validaUsuario(usuario);
		validaFilmes(filmes);
		checarEstoque(filmes);
		checarUsuarioNegativadoSPC(usuario);

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotalFilmes = getValorTotalFilmes(filmes);
		locacao.setValor(valorTotalFilmes);

		// Entrega no dia seguinte - exceto domingo
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		// Salvar a Locação
		dao.salvar(locacao);
		
		return locacao;
	}

	public void notificarUsuariosComAtraso(){
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for(Locacao locacao: locacoes){
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}

	private Double getValorTotalFilmes(List<Filme> filmes) {
		Double soma = (double) 0;
		for(int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i){
				case 2:
					valorFilme = calculosDesconto.descontoDe25PorcentoNoTerceiroFilme(filme);
					break;
				case 3:
					valorFilme = calculosDesconto.descontoDe50PorcentoNoQuartoFilme(filme);
					break;
				case 4:
					valorFilme = calculosDesconto.descontoDe75PorcentoNoQuintoFilme(filme);
					break;
				case 5:
					valorFilme = calculosDesconto.descontoDe100PorcentoNoSextoFilme();
					break;
				default:
			}
			soma += valorFilme;
		}
		return soma;
	}

	private void checarEstoque(List<Filme> filmes) throws FilmeSemEstoqueException {
		for(Filme filme: filmes) {
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque!");
			}
		}
	}

	private void validaFilmes(List<Filme> filmes) throws LocacaoException {
		if(filmes == null || filmes.isEmpty()) {
			throw new LocacaoException("Filme vazio!");
		}
	}

	private void validaUsuario(Usuario usuario) throws LocacaoException {
		if(usuario == null) {
			throw new LocacaoException("Usuário vazio!");
		}
	}

	private void checarUsuarioNegativadoSPC(Usuario usuario) throws Exception {
		boolean usuarioNegativado;
		try {
			usuarioNegativado = spcService.usuarioNegativado(usuario);
		} catch (Exception e){
			throw new LocacaoException("Problemas com SPC, favor tentar novamente!");
		}
		if(usuarioNegativado){
			throw new LocacaoException("Usuário negativado!");
		}
	}

	public void prorrogarLocacao(Locacao locacao, int dias){
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}
}