package dj.zendo.store.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import dj.zendo.store.model.Product;

public class SendPurchaseEmailTask extends AsyncTask<List<Product>, Void, Boolean> {

    //private final String API_ENDPOINT = "http://10.0.2.2:52144/api/email/";
    private final String API_ENDPOINT = "http://zendo.gear.host/api/email/";

    private WeakReference<Context> context;
    private OnSendPurchaseEmailListener listener;

    public SendPurchaseEmailTask(Context context, OnSendPurchaseEmailListener listener) {
        this.context = new WeakReference<>(context);
        this.listener = listener;
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(List<Product>... products) {

        List<Product> productList = products[0];

        try {

            URL mailUrl = new URL(API_ENDPOINT);
            HttpURLConnection httpConnection = (HttpURLConnection) mailUrl.openConnection();

            try {

                httpConnection.setRequestMethod("POST");
                httpConnection.setReadTimeout(15000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestProperty("Content-Type", "application/json");

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.get());
                String userEmail = settings.getString("email", null);
                String accessToken = settings.getString("access_token", null);

                httpConnection.setRequestProperty("Email", userEmail);
                httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                BufferedOutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                JSONArray jsonProducts = new JSONArray();

                for (Product p : productList) {
                    JSONObject jsonProduct = new JSONObject();

                    jsonProduct.put("Id", p.getId());
                    jsonProduct.put("Name", p.getName());
                    jsonProduct.put("Item", p.getItem());
                    jsonProduct.put("Category", p.getCategory());
                    jsonProduct.put("Price", p.getPrice());
                    jsonProduct.put("ImagePath", p.getImagePath());

                    jsonProducts.put(jsonProduct);
                }

                writer.write(jsonProducts.toString());

                writer.flush();
                writer.close();
                out.close();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                }

            } finally {
                httpConnection.disconnect();
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        listener.onSendPurchaseEmail(success);
    }
}