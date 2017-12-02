package dj.zendo.store.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dj.zendo.store.R;
import dj.zendo.store.model.Product;
import dj.zendo.store.util.CurrencyFormatter;

public class ProductAdapter extends ArrayAdapter<Product> {

    private List<Product> products;

    public ProductAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, R.layout.item_product, products);

        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        ImageView productImage = convertView.findViewById(R.id.image_product_image);
        TextView productNameText = convertView.findViewById(R.id.text_product_name);
        TextView productPriceText = convertView.findViewById(R.id.text_product_price);

        Product product = products.get(position);

        productImage.setImageBitmap(product.getImage());
        productNameText.setText(product.getItem() + " - " + product.getName());
        productPriceText.setText(CurrencyFormatter.format(product.getPrice()));

        return convertView;
    }
}