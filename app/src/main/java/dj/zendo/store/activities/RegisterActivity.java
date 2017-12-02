package dj.zendo.store.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dj.zendo.store.R;
import dj.zendo.store.model.User;
import dj.zendo.store.tasks.CreateUserTask;
import dj.zendo.store.tasks.OnCreateUserListener;

public class RegisterActivity extends AppCompatActivity implements OnCreateUserListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailText = (EditText) findViewById(R.id.text_register_email);
        final EditText passwordText = (EditText) findViewById(R.id.text_register_password);

        Button registerButton = (Button)findViewById(R.id.button_register_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passwordText.getText().toString().trim().length() < 8) {
                    passwordText.setError(getResources().getString(R.string.register_passwordminimum));
                    return;
                }

                User user = new User();
                user.setEmail(emailText.getText().toString());
                user.setPassword(passwordText.getText().toString());

                new CreateUserTask(RegisterActivity.this).execute(user);
            }
        });
    }

    @Override
    public void onCreateUser(Boolean created) {

        if (created) {

            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
            finish();

        } else {

            Toast.makeText(this, R.string.register_fail, Toast.LENGTH_SHORT).show();

        }
    }
}