package br.com.brunsoares.servicos.locacao_filmes;

import br.com.brunsoares.exceptions.FilmeSemEstoqueException;
import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.locacao_filmes.servicos.LocacaoService;
import br.com.brunsoares.utils.DataUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

	private LocacaoService service;
	private Usuario usuario;
	private List<Filme> filmes = new ArrayList<Filme>();

	@Before
	public void initTests(){	// Ocorre antes de cada teste
		// Cenário
		service = new LocacaoService();
		usuario = new Usuario("BrunoSoares");
		filmes.add(new Filme("Monstros S.A", 2, 19.99));
		filmes.add(new Filme("Toy Story", 1, 20.60));

		System.out.println("Teste Iniciado!");
	}

	@After
	public void endTests(){		// Ocorre depois de cada teste
		System.out.println("Teste Finalizado!");
	}

	@Test
	public void testLocacaoService() throws Exception {
		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
		List<String> listaNomes = Arrays.asList("Monstros S.A", "Toy Story");

		// Verificação
		Assert.assertEquals(locacao.getValor(), new Double(40.59));    // Valores iguais
		Assert.assertTrue(locacao.getUsuario().getNome().equalsIgnoreCase("BrunoSoares"));    // Condição Lógica True
		Assert.assertThat(new Filme().getNomeFilmes(locacao.getFilmes()), is(listaNomes));	// Validando listas de valores
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacaoServiceSemEstoqueComExcecao() throws FilmeSemEstoqueException {    // Teste espera uma exceção
		filmes.add(new Filme("Monstros S.A", 0, 19.99));
		filmes.add(new Filme("Toy Story", 0, 20.60));

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testLocacaoServiceSemEstoqueSemExcecao() {    // Mais controle sobre o erro
		filmes.add(new Filme("Monstros S.A", 0, 19.99));
		filmes.add(new Filme("Toy Story", 0, 20.60));

		// Ação
		try {
			service.alugarFilme(usuario, filmes);
		} catch (Exception e) {
			Assert.assertThat(e.getMessage(), is("Filme sem estoque!"));
		}
	}


}