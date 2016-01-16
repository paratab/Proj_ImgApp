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

public class UserPasswordChange extends AppCompatActivity implements View.OnClickListener {

    EditText edtPassword, edtNewPassword, edtReplyNewPassword;
    Button btSavePassword;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_password_change);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtReplyNewPassword = (EditText) findViewById(R.id.edtReplyNewPassword);
        btSavePassword = (Button) findViewById(R.id.btSavePassword);

        btSavePassword.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSavePassword:
                String password = edtPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String replyNewPassword = edtReplyNewPassword.getText().toString();

                User user = userLocalStore.getLoggedInUser();
                if (!password.equals(user.password)) {
                    Toast.makeText(this, "Current password is not correct.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!newPassword.equals(replyNewPassword)) {
                    Toast.makeText(this, "Reply password is no same as New Password.", Toast.LENGTH_SHORT).show();
                    break;
                }

                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.updateUserPasswordInBG(user, newPassword, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        userLocalStore.storeUserData(returnedUser);
                        Intent intent = new Intent(UserPasswordChange.this, UserManagement.class);
                        Toast.makeText(UserPasswordChange.this, "Password Changed.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                });


                break;
        }
    }
}
