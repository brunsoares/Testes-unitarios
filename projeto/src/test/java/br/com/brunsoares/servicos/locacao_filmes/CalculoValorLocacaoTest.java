package br.com.brunsoares.servicos.locacao_filmes;

import br.com.brunsoares.builders.UsuarioBuilder;
import br.com.brunsoares.daos.LocacaoDAO;
import br.com.brunsoares.exceptions.FilmeSemEstoqueException;
import br.com.brunsoares.exceptions.LocacaoException;
import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.locacao_filmes.servicos.LocacaoService;
import br.com.brunsoares.locacao_filmes.servicos.SPCService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.com.brunsoares.builders.FilmeBuilder.novoFilme;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;
    private Usuario usuario;
    @Parameterized.Parameter
    public List<Filme> filmes;
    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String nomeTeste;

    @Before
    public void initTests(){
        service = new LocacaoService();
        usuario = UsuarioBuilder.novoUsuario().retornarUsuario();
        // Usando mockito para implementar dependências para os testes
        LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
        service.setDao(dao);
        SPCService spcService = Mockito.mock(SPCService.class);
        service.setSpcService(spcService);
    }

    private static Filme filme1 = novoFilme("Monstros S.A",  10.00).retornarFilme();
    private static Filme filme2 = novoFilme("Toy Story", 10.00).retornarFilme();
    private static Filme filme3 = novoFilme("Up Altas Aventuras",  10.00).retornarFilme();
    private static Filme filme4 = novoFilme("Universidade Monstros",  10.00).retornarFilme();
    private static Filme filme5 = novoFilme("Familia Mitchell e a Revolta das Máquinas",  10.00).retornarFilme();
    private static Filme filme6 = novoFilme("Ta Dando Onda",  10.00).retornarFilme();

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros(){    // Primeiro Lista de filmes, em seguida valor da locação
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2, filme3), 27.5, "Desconto no 3º filme (25%)"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 32.5, "Desconto no 4º filme (50%)"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 35.0, "Desconto no 5º filme (75%)"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 35.0, "Desconto no 6º filme (100%)"},
        });
    }

    @Test
    public void deveCalcularValorDaLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocacaoException {

        Locacao resultado = service.alugarFilme(usuario, filmes);

        Assert.assertEquals(valorLocacao, resultado.getValor());
    }
}
