package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btLogin, tvRegister;
    EditText edtUsername, edtPassword;

    UserLocalStore userLocalStore;
    EncCheckModule encCheckModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = (Button) findViewById(R.id.btLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        tvRegister = (Button) findViewById(R.id.tvRegister);

        btLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        encCheckModule = new EncCheckModule();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btLogin:
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "ชื่อผู้ใช้งานหรือรหัสผ่านว่างเปล่า!", Toast.LENGTH_SHORT).show();
                    break;
                }

                password = encCheckModule.getSHA1Hash(password);

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
        Toast.makeText(this, "ลงชื่อเข้าใช้งานเรียบร้อย", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("ชื่อผู้ใช้งาน หรือ รหัสผ่าน ผิดพลาด");
        dialogBuilder.setPositiveButton("ตกลง", null);
        dialogBuilder.show();
    }
}
