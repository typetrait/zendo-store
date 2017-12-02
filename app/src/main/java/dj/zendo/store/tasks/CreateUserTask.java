package dj.zendo.store.tasks;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dj.zendo.store.model.User;

public class CreateUserTask extends AsyncTask<User, Void, Boolean> {

    //private final String API_ENDPOINT = "http://10.0.2.2:52144/api/user/";
    private final String API_ENDPOINT = "http://zendo.gear.host/api/user/";

    private OnCreateUserListener listener;

    public CreateUserTask(OnCreateUserListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(User... params) {

        User user = params[0];

        try {

            URL userUrl = new URL(API_ENDPOINT);
            HttpURLConnection httpConnection = (HttpURLConnection)userUrl.openConnection();

            try {

                httpConnection.setRequestMethod("POST");
                httpConnection.setReadTimeout(15000);
                httpConnection.setConnectTimeout(15000);
                httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                BufferedOutputStream out = new BufferedOutputStream(httpConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));

                writer.write("email=" + user.getEmail() + "&password=" + user.getPassword());

                writer.flush();
                writer.close();
                out.close();

                return httpConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED;

            } finally {

                httpConnection.disconnect();

            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean created) {
        super.onPostExecute(created);
        listener.onCreateUser(created);
    }
}