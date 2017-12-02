package dj.zendo.store.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dj.zendo.store.R;
import dj.zendo.store.model.Cart;
import dj.zendo.store.model.Product;
import dj.zendo.store.tasks.FetchSingleProductTask;
import dj.zendo.store.tasks.OnSingleProductFetchListener;
import dj.zendo.store.util.CurrencyFormatter;

public class ProductDetailsActivity extends AppCompatActivity implements OnSingleProductFetchListener {

    private Button addToCartButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        progressBar = (ProgressBar)findViewById(R.id.progressbar_productdetails_progressbar);

        Intent intent = getIntent();
        new FetchSingleProductTask(this, this).execute(intent.getIntExtra("id", 1));
    }

    @Override
    public void onSingleProductFetch(final Product product) {

        ImageView productImage = (ImageView)findViewById(R.id.image_productdetails_productimage);
        TextView productItemText = (TextView)findViewById(R.id.text_productdetails_productitem);
        TextView productNameText = (TextView)findViewById(R.id.text_productdetails_productname);
        TextView productPriceText = (TextView)findViewById(R.id.text_productdetails_productprice);

        productImage.setImageBitmap(product.getImage());
        productItemText.setText(product.getItem());
        productNameText.setText(product.getName());
        productPriceText.setText(CurrencyFormatter.format(product.getPrice()));

        progressBar.setVisibility(View.GONE);

        addToCartButton = (Button)findViewById(R.id.button_productdetails_addtocart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.items.add(product);

                view.setEnabled(false);
                Toast.makeText(ProductDetailsActivity.this, R.string.productdetails_itemadded, Toast.LENGTH_SHORT).show();

                Button b = (Button)view;
                b.setText("Item já adicionado!");

            }
        });

        for (Product p : Cart.items) {
            if (product.getId() == p.getId()) {
                addToCartButton.setEnabled(false);
                addToCartButton.setText("Item já adicionado!");
            }
        }

    }
}