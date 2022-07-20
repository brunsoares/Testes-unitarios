package br.com.brunsoares.matchers;

import br.com.brunsoares.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatasIguaisMatcher extends TypeSafeMatcher<Date> {

    private Integer diferencaDias;

    public DatasIguaisMatcher(Integer diferencaDias){
        this.diferencaDias = diferencaDias;
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diferencaDias));
    }

    @Override
    public void describeTo(Description description) {
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        description.appendText(formatoData.format(new Date()));
    }
}
