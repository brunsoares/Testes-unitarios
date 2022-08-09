package br.com.brunsoares.matchers;

import java.util.Calendar;

public class MatchersList {

    public static DiaSemanaMatcher caiNumaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DatasIguaisMatcher checarDataAtual(){
        return new DatasIguaisMatcher(0);
    }

    public static DatasIguaisMatcher checarDataComDiferencaDeDias(Integer diferencaDias){
        return new DatasIguaisMatcher(diferencaDias);
    }

}
