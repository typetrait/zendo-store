package dj.zendo.store.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dj.zendo.store.model.Product;
import dj.zendo.store.util.BitmapDownloader;

public class FetchProductsTask extends AsyncTask<Void, Void, List<Product>> {

    //private final String API_ENDPOINT = "http://10.0.2.2:52144/api/product/";
    private final String API_ENDPOINT = "http://zendo.gear.host/api/product/";

    private WeakReference<Context> context;
    private OnProductsFetchListener listener;

    public FetchProductsTask(Context context, OnProductsFetchListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {

        ArrayList<Product> products = new ArrayList<>();

        try {

            URL productUrl = new URL(API_ENDPOINT);
            HttpURLConnection httpConnection = (HttpURLConnection)productUrl.openConnection();

            try {

                httpConnection.setRequestMethod("GET");
                httpConnection.setReadTimeout(15000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestProperty("Content-Type", "application/json;odata=verbose");
                httpConnection.setRequestProperty("Accept", "application/json;odata=verbose");

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.get());
                String accessToken = settings.getString("access_token", null);

                httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                InputStream inputStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String response = sb.toString();

                JSONArray jsonProducts = new JSONArray(response);
                for (int i = 0; i < jsonProducts.length(); i++) {
                    JSONObject jsonProduct = jsonProducts.getJSONObject(i);

                    Product product = new Product();
                    product.setId(jsonProduct.getInt("Id"));
                    product.setName(jsonProduct.getString("Name"));
                    product.setItem(jsonProduct.getString("Item"));
                    product.setCategory(jsonProduct.getString("Category"));
                    product.setPrice(new BigDecimal(jsonProduct.getDouble("Price")).setScale(2, BigDecimal.ROUND_HALF_UP));
                    product.setImage(BitmapDownloader.downloadBitmap("https://zendo.elitecommunity.com.br/app"
                            + jsonProduct.getString("ImagePath")));

                    products.add(product);
                }

            } finally {

                httpConnection.disconnect();

            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();

        }

        return products;
    }

    @Override
    protected void onPostExecute(final List<Product> products) {
        super.onPostExecute(products);
        listener.onProductsFetch(products);
    }
}