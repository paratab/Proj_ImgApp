package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btLogin;
    EditText edtUsername, edtPassword;
    TextView tvRegister;

    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        btLogin = (Button) findViewById(R.id.btLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        btLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btLogin:
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Username or Password is empty!.", Toast.LENGTH_SHORT).show();
                    break;
                }

                User user = new User(username, password);

                authenticate(user);

                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBG(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logInUser(returnedUser);
                }
            }
        });
    }

    private void logInUser(User user) {
        userLocalStore.storeUserData(user);
        userLocalStore.setUserLoggedIn(true);

        Intent intent = new Intent(this, UserManagement.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details.");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();
    }
}
