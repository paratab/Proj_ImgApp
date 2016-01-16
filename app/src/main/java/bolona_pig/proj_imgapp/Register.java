package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btRegister;
    EditText edtUsername, edtPassword, edtReplyPassword, edtName, edtID, edtEmail, edtTelephone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        btRegister = (Button) findViewById(R.id.btRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtReplyPassword = (EditText) findViewById(R.id.edtReplyPassword);
        edtName = (EditText) findViewById(R.id.edtName);
        edtID = (EditText) findViewById(R.id.edtID);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelephone = (EditText) findViewById(R.id.edtPhone);

        btRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRegister:

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String replyPassword = edtReplyPassword.getText().toString();
                String name = edtName.getText().toString();
                String nationId = edtID.getText().toString();
                String email = edtEmail.getText().toString();
                String telephone = edtTelephone.getText().toString();

                if (!checkInput(username, password, replyPassword, nationId, email, telephone)) {
                    break;
                }


                User user = new User(username, password, name, nationId, email, telephone);

                registerUser(user);

                /*Intent intent = new Intent(this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
                break;
        }
    }

    private void registerUser(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBG(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                Log.i("custom_check", "Register Complete");
                Intent intent = new Intent(Register.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkInput(String username, String password, String replyPassword, String nationId, String email, String telephone) {

        CharSequence text;
        if (username.isEmpty()) {
            text = "Username cannot be empty.";
            printError(text);
            return false;
        }
        if (isValidUsername(username)) {
            text = "Username invalid. Username must be more than 3 char and contain only [A-Z,a-z,0-9,_,-]";
            printError(text);
            return false;
        }
        if (password.isEmpty()) {
            text = "Password cannot be empty";
            printError(text);
            return false;
        }
        if (!password.equals(replyPassword)) {
            text = "Password and Reply Password is not equal.";
            printError(text);
            return false;
        }
        if (nationId.length() != 13) {
            text = "National ID must has 13 digits";
            printError(text);
            return false;
        }
        if (email.isEmpty()) {
            text = "Email cannot be empty.";
            printError(text);
            return false;
        }
        if (isValidEmail(email)) {
            text = "Email is not valid";
            printError(text);
            return false;
        }
        if (telephone.isEmpty()) {
            text = "Telephone cannot be empty.";
            printError(text);
            return false;
        }

        return true;
    }

    private boolean isValidUsername(String username) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
        Matcher m = emailPattern.matcher(username);
        return !m.matches();
    }

    private boolean isValidEmail(String email) {
       /* Pattern emailPattern = Pattern.compile("[A-Z]+[a-zA-Z_]+@\b([a-zA-Z]+.){2}\b?.[a-zA-Z]+");
        Matcher m = emailPattern.matcher(email);
        return !m.matches();*/
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void printError(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
}
