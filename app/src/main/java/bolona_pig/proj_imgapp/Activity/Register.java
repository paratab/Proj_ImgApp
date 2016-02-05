package bolona_pig.proj_imgapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bolona_pig.proj_imgapp.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.SecureModule;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.R;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btRegister;
    EditText edtUsername, edtPassword, edtReplyPassword, edtName, edtID, edtEmail, edtTelephone;
    SecureModule secureModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btRegister = (Button) findViewById(R.id.btRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtReplyPassword = (EditText) findViewById(R.id.edtReplyPassword);
        edtName = (EditText) findViewById(R.id.edtName);
        edtID = (EditText) findViewById(R.id.edtID);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelephone = (EditText) findViewById(R.id.edtPhone);

        secureModule = new SecureModule();

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

                password = secureModule.getSHA1Hash(password);

                User user = new User(username, password, name, nationId, email, telephone);

                registerUser(user);

                break;
        }
    }

    private void registerUser(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBG(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser != null) {
                    Toast.makeText(Register.this, "Register Completed", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Register.this, "Register not successful. Make sure you enable internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkInput(String username, String password, String replyPassword, String nationId, String email, String telephone) {

        CharSequence text;
        if (username.isEmpty()) {
            text = "Username cannot be empty.";
            secureModule.printError(this, text);
            return false;
        }
        if (!secureModule.isValidUsername(username)) {
            text = "Username invalid. Username must be more than 3 char and contain only [A-Z,a-z,0-9,_,-]";
            secureModule.printError(this, text);
            return false;
        }
        if (password.isEmpty()) {
            text = "Password cannot be empty";
            secureModule.printError(this, text);
            return false;
        }
        if (!password.equals(replyPassword)) {
            text = "Password and Reply Password is not equal.";
            secureModule.printError(this, text);
            return false;
        }
        if (nationId.length() != 13) {
            text = "National ID must has 13 digits";
            secureModule.printError(this, text);
            return false;
        }
        if (email.isEmpty()) {
            text = "Email cannot be empty.";
            secureModule.printError(this, text);
            return false;
        }
        if (!secureModule.isValidEmail(email)) {
            text = "Email is not valid";
            secureModule.printError(this, text);
            return false;
        }
        if (telephone.isEmpty()) {
            text = "Telephone cannot be empty.";
            secureModule.printError(this, text);
            return false;
        }

        return true;
    }
}
