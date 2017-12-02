package dj.zendo.store.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    public static List<Product> items = new ArrayList<>();

    public static BigDecimal getTotal() {

        BigDecimal total = new BigDecimal(0);

        for (Product p : items) {
            total = total.add(p.getPrice());
        }

        return total;
    }

}