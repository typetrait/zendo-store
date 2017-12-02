package dj.zendo.store.tasks;

import dj.zendo.store.model.Product;

public interface OnSingleProductFetchListener {
    void onSingleProductFetch(Product product);
}