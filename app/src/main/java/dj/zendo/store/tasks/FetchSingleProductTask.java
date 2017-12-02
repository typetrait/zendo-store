package dj.zendo.store.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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

import dj.zendo.store.model.Product;
import dj.zendo.store.util.BitmapDownloader;

public class FetchSingleProductTask extends AsyncTask<Integer, Void, Product> {

    //private final String API_ENDPOINT = "http://10.0.2.2:52144/api/product/";
    private final String API_ENDPOINT = "http://zendo.gear.host/api/product/";

    private WeakReference<Context> context;
    private OnSingleProductFetchListener listener;

    public FetchSingleProductTask(Context context, OnSingleProductFetchListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected Product doInBackground(Integer... params) {

        Product product = null;

        try {

            URL productUrl = new URL(API_ENDPOINT + params[0]);
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

                JSONObject jsonProduct = new JSONObject(response);

                product = new Product();
                product.setId(jsonProduct.getInt("Id"));
                product.setName(jsonProduct.getString("Name"));
                product.setItem(jsonProduct.getString("Item"));
                product.setCategory(jsonProduct.getString("Category"));
                product.setPrice(new BigDecimal(jsonProduct.getDouble("Price")).setScale(2, BigDecimal.ROUND_HALF_UP));
                product.setImage(BitmapDownloader.downloadBitmap("https://zendo.elitecommunity.com.br/app"
                        + jsonProduct.getString("ImagePath")));

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

        return product;
    }

    @Override
    protected void onPostExecute(Product product) {
        super.onPostExecute(product);
        listener.onSingleProductFetch(product);
    }
}