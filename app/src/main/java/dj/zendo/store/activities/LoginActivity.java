package dj.zendo.store.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import dj.zendo.store.R;
import dj.zendo.store.model.User;
import dj.zendo.store.tasks.AuthenticateUserTask;
import dj.zendo.store.tasks.OnUserAuthenticateListener;

public class LoginActivity extends AppCompatActivity implements OnUserAuthenticateListener{

    private static final String TAG = "Login";

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Button registerButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.button_login_login);
        registerButton = (Button)findViewById(R.id.button_login_register);

        emailText = (EditText)findViewById(R.id.text_login_email);
        passwordText = (EditText)findViewById(R.id.text_login_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setMessage(this.getResources().getString(R.string.login_authenticating));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setEmail(emailText.getText().toString());
                user.setPassword(passwordText.getText().toString());

                progressDialog.show();

                AuthenticateUserTask authenticateUserTask = new AuthenticateUserTask(LoginActivity.this, LoginActivity.this);
                authenticateUserTask.execute(user);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        setupHttpCache(10);
    }

    @Override
    protected void onStop() {
        super.onStop();
        flushHttpCache();
    }

    @Override
    public void onUserAuthenticate(boolean authenticated) {
        progressDialog.dismiss();

        if (authenticated) {

            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);

        } else {

            Toast.makeText(this, R.string.login_incorrect, Toast.LENGTH_SHORT).show();

        }
    }

    private void setupHttpCache(int sizeInMb) {
        try {

            File httpCacheDir = new File(this.getCacheDir(), "http");
            long httpCacheSize = sizeInMb * 1024 * 1024; // 10M
            HttpResponseCache.install(httpCacheDir, httpCacheSize);

        } catch (IOException e) {

            Log.i(TAG, "Failed to install HTTP response cache:" + e);

        }
    }

    private void flushHttpCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
}