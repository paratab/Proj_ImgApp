package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailEdit extends AppCompatActivity implements View.OnClickListener {

    Button btUpdate, btChangePW;
    TextView edtUsername, edtID, edtPassword;
    EditText edtName, edtEmail, edtTelephone;
    UserLocalStore userLocalStore;
    SecureModule secureModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_detail_edit);

        btUpdate = (Button) findViewById(R.id.btUpdate);
        btChangePW = (Button) findViewById(R.id.btChangePW);
        edtUsername = (TextView) findViewById(R.id.edtUsername);
        edtPassword = (TextView) findViewById(R.id.edtPassword);
        edtName = (EditText) findViewById(R.id.edtName);
        edtID = (TextView) findViewById(R.id.edtID);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelephone = (EditText) findViewById(R.id.edtPhone);

        btUpdate.setOnClickListener(this);
        btChangePW.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        secureModule = new SecureModule();

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUserDetail();
    }

    private void displayUserDetail() {
        User user = userLocalStore.getLoggedInUser();
        edtUsername.setText(user.username);
        edtPassword.setText(user.password);
        edtName.setText(user.name);
        edtID.setText(user.nationId);
        edtEmail.setText(user.email);
        edtTelephone.setText(user.telephone);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        CharSequence text;
        switch (v.getId()) {
            case R.id.btUpdate:

                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String name = edtName.getText().toString();
                String nationId = edtID.getText().toString();
                String email = edtEmail.getText().toString();
                String telephone = edtTelephone.getText().toString();

                if (email.isEmpty()) {
                    text = "Email cannot be empty.";
                    secureModule.printError(this, text);
                    break;
                }
                if (!secureModule.isValidEmail(email)) {
                    text = "Email is not valid";
                    secureModule.printError(this, text);
                    break;
                }
                if (telephone.isEmpty()) {
                    text = "Telephone cannot be empty.";
                    secureModule.printError(this, text);
                    break;
                }

                User user = new User(username, password, name, nationId, email, telephone);

                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.updateUserDataInBG(user, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser != null) {
                            userLocalStore.storeUserData(returnedUser);
                            Toast.makeText(UserDetailEdit.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserDetailEdit.this, UserManagement.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserDetailEdit.this, "Error on Update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btChangePW:
                intent = new Intent(UserDetailEdit.this, UserPasswordChange.class);
                startActivity(intent);
                break;
        }
    }

}
