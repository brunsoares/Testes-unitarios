package br.com.brunsoares.servicos.locacao_filmes;

import br.com.brunsoares.exceptions.FilmeSemEstoqueException;
import br.com.brunsoares.exceptions.LocacaoException;
import br.com.brunsoares.locacao_filmes.entidades.Filme;
import br.com.brunsoares.locacao_filmes.entidades.Locacao;
import br.com.brunsoares.locacao_filmes.entidades.Usuario;
import br.com.brunsoares.locacao_filmes.servicos.LocacaoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        usuario = new Usuario("BrunoSoares");
    }

    private static Filme filme1 = new Filme("Monstros S.A", 1, 10.00);
    private static Filme filme2 = new Filme("Toy Story", 1, 10.00);
    private static Filme filme3 = new Filme("Up Altas Aventuras", 1, 10.00);
    private static Filme filme4 = new Filme("Universidade Monstros", 1, 10.00);
    private static Filme filme5 = new Filme("Familia Mitchell e a Revolta das Máquinas", 1, 10.00);
    private static Filme filme6 = new Filme("Ta Dando Onda", 1, 10.00);

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
