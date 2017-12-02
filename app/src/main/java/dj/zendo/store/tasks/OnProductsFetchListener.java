package dj.zendo.store.tasks;

import java.util.List;

import dj.zendo.store.model.Product;

public interface OnProductsFetchListener {
    void onProductsFetch(List<Product> product);
}