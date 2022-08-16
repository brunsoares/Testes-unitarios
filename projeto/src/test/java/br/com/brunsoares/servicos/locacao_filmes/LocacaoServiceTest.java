package br.com.brunsoares.servicos.locacao_filmes;

import br.com.brunsoares.builders.FilmeBuilder;
import br.com.brunsoares.builders.LocacaoBuilder;
import br.com.brunsoares.builders.UsuarioBuilder;
import br.com.brunsoares.daos.LocacaoDAO;
import br.com.brunsoares.exceptions.FilmeSemEstoqueException;
import br.com.brunsoares.exceptions.LocacaoException;
import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.locacao_filmes.servicos.EmailService;
import br.com.brunsoares.locacao_filmes.servicos.LocacaoService;
import br.com.brunsoares.locacao_filmes.servicos.SPCService;
import br.com.brunsoares.matchers.MatchersList;
import br.com.brunsoares.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static br.com.brunsoares.builders.FilmeBuilder.novoFilme;
import static br.com.brunsoares.builders.FilmeBuilder.novoFilmeSemEstoque;
import static org.hamcrest.CoreMatchers.is;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class, DataUtils.class})
public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario usuario;
	private List<Filme> filmes;
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void initTests(){	// Ocorre antes de cada teste
		// Cenário
		service = new LocacaoService();
		usuario = UsuarioBuilder.novoUsuario().retornarUsuario();
		filmes = new ArrayList<Filme>();

		// Usando mockito para implementar dependências para os testes
		dao = Mockito.mock(LocacaoDAO.class);
		service.setDao(dao);

		spcService = Mockito.mock(SPCService.class);
		service.setSpcService(spcService);

		emailService = Mockito.mock(EmailService.class);
		service.setEmailService(emailService);

		System.out.println("Teste Iniciado!");
	}

	@After
	public void endTests(){		// Ocorre depois de cada teste
		System.out.println("Teste Finalizado!");
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(22, 2, 2022));

		// Ação
		filmes.add(novoFilme("Monstros S.A", 19.99).retornarFilme());
		filmes.add(novoFilme("Toy Story", 20.60).retornarFilme());
		Locacao locacao = service.alugarFilme(usuario, filmes);
		List<String> listaNomes = Arrays.asList("Monstros S.A", "Toy Story");

		// Verificação
		Assert.assertEquals(locacao.getValor(), new Double(40.59));    // Valores iguais
		Assert.assertTrue(locacao.getUsuario().getNome().equalsIgnoreCase("Bruno Soares"));    // Condição Lógica True
		Assert.assertThat(new Filme().getNomeFilmes(locacao.getFilmes()), is(listaNomes));	// Validando listas de valores
		// Matchers próprios
		Assert.assertThat(locacao.getDataLocacao(), MatchersList.checarDataAtual());
		Assert.assertThat(locacao.getDataRetorno(), MatchersList.checarDataComDiferencaDeDias(1));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(23, 2, 2022)), is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(22, 2, 2022)), is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoDeFilmeSemEstoque() throws Exception {    // Teste espera uma exceção
		filmes.add(novoFilmeSemEstoque("Monstros S.A", 19.99).retornarFilme());
		filmes.add(novoFilmeSemEstoque("Toy Story", 20.60).retornarFilme());

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void naoDevePermitirAlugarFilmeSemEstoque() {    // Mais controle sobre o erro
		filmes.add(novoFilmeSemEstoque("Monstros S.A", 19.99).retornarFilme());
		filmes.add(novoFilmeSemEstoque("Toy Story", 20.60).retornarFilme());

		// Ação
		try {
			service.alugarFilme(usuario, filmes);
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme sem estoque!"));
		}
	}

	@Test
	public void naoDevePermitirAlugarSemUsuario() throws Exception {
		filmes.add(novoFilme("Monstros S.A", 19.99).retornarFilme());
		exception.expect(LocacaoException.class);
		exception.expectMessage("Usuário vazio!");
		service.alugarFilme(null, filmes);
	}

	@Test
	public void naoDevePermitirAlugarSemListaDeFilmes() throws Exception {
		exception.expect(LocacaoException.class);
		exception.expectMessage("Filme vazio!");
		service.alugarFilme(usuario, null);
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(19, 2, 2022));

		filmes.add(novoFilme("Up Altas Aventuras",  10.00).retornarFilme());
		Locacao retorno = service.alugarFilme(usuario, filmes);

		Assert.assertThat(retorno.getDataRetorno(), MatchersList.caiNumaSegunda());
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadosSPC() throws Exception {
		filmes.add(novoFilme("Monstros S.A", 19.99).retornarFilme());
		// Alterando fluxo de um mock para retornar true
		Mockito.when(spcService.usuarioNegativado(usuario)).thenReturn(true);

		exception.expect(LocacaoException.class);
		exception.expectMessage("Usuário negativado!");

		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveEnviarEmailParaUsuarioAtrasados(){
		Usuario usuarioAtrasado = UsuarioBuilder.novoUsuario().retornarUsuario();
		Usuario usuarioNormal = UsuarioBuilder.novoUsuario().comNome("Nelson").retornarUsuario();
		List<Locacao> locacoesPendentes = Arrays.asList(
				LocacaoBuilder.umaLocacao()
							  .comUsuario(usuarioAtrasado)
							  .comDatasAtrasadas()
							  .retornarLocacao(),
				LocacaoBuilder.umaLocacao()
						      .comUsuario(usuarioNormal)
							  .retornarLocacao()
				);

		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoesPendentes);
		service.notificarUsuariosComAtraso();
		// Usando mockito para verificar casos
		Mockito.verify(emailService).notificarAtraso(usuarioAtrasado);
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuarioNormal);
		Mockito.verifyNoMoreInteractions(emailService);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		filmes.add(FilmeBuilder.novoFilme("Sonic", 20.0).retornarFilme());
		Mockito.when(spcService.usuarioNegativado(usuario)).thenThrow(new Exception("Falha no SPC!"));

		exception.expect(LocacaoException.class);
		exception.expectMessage("Problemas com SPC, favor tentar novamente!");

		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacaoPelosDias(){
		Locacao locacao = LocacaoBuilder.umaLocacao().retornarLocacao();
		service.prorrogarLocacao(locacao, 4);
		ArgumentCaptor<Locacao> argumentoCapturado = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argumentoCapturado.capture());
		Locacao locacaoProrrogada = argumentoCapturado.getValue();

		Assert.assertThat(locacaoProrrogada.getValor(), is(80.0));
		Assert.assertThat(locacaoProrrogada.getDataLocacao(), MatchersList.checarDataAtual());
		Assert.assertThat(locacaoProrrogada.getDataRetorno(), MatchersList.checarDataComDiferencaDeDias(4));
	}

	/*
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
	 */

}