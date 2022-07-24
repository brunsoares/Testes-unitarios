package br.com.brunsoares.matchers;

import java.util.Calendar;

public class MatchersList {

    public static DiaSemanaMatcher caiNumaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }
}
