package dj.zendo.store.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public static String format(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        //format.setCurrency(Currency.getInstance("BRL"));
        return format.format(value);
    }

}