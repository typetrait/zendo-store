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

public class FeaturedProductAdapter extends ArrayAdapter<Product> {

    private List<Product> products;

    public FeaturedProductAdapter(@NonNull Context context, @NonNull List<Product> products) {
        super(context, R.layout.item_featured, products);

        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_featured, parent, false);
        }

        ImageView productImage = convertView.findViewById(R.id.image_featured_productimage);
        TextView productItemText = convertView.findViewById(R.id.text_featured_item);
        TextView productNameText = convertView.findViewById(R.id.text_featured_name);

        Product product = products.get(position);

        productImage.setImageBitmap(product.getImage());
        productItemText.setText(product.getItem());
        productNameText.setText(product.getName());

        return convertView;
    }
}