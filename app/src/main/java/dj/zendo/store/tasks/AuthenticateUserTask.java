package dj.zendo.store.tasks;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dj.zendo.store.model.User;

public class AuthenticateUserTask extends AsyncTask<User,Void,Boolean> {

    //private final String API_ENDPOINT = "http://10.0.2.2:52144/token";
    private final String API_ENDPOINT = "http://zendo.gear.host/token";

    private WeakReference<Activity> activity;
    private OnUserAuthenticateListener listener;

    public AuthenticateUserTask(Activity context, OnUserAuthenticateListener listener) {
        this.activity = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(User... params) {

        User user = params[0];

        try {

            URL productUrl = new URL(API_ENDPOINT);
            HttpURLConnection httpConnection = (HttpURLConnection)productUrl.openConnection();

            try {

                httpConnection.setRequestMethod("POST");
                httpConnection.setReadTimeout(15000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                BufferedOutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                writer.write("username=" + user.getEmail() + "&password=" + user.getPassword() + "&grant_type=password");

                writer.flush();
                writer.close();
                out.close();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    String response = sb.toString();

                    JSONObject jsonResponse = new JSONObject(response);
                    String accessToken = jsonResponse.getString("access_token");

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.get());
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("email", user.getEmail());
                    editor.putString("access_token", accessToken);
                    editor.apply();

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
    protected void onPostExecute(Boolean authenticated) {
        super.onPostExecute(authenticated);
        listener.onUserAuthenticate(authenticated);
    }
}