package br.com.brunsoares.servicos.suites;

import br.com.brunsoares.servicos.calculadora.CalculadoraTest;
import br.com.brunsoares.servicos.locacao_filmes.CalculoValorLocacaoTest;
import br.com.brunsoares.servicos.locacao_filmes.LocacaoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class,
        CalculadoraTest.class
})
public class SuitesExecucao {
}
