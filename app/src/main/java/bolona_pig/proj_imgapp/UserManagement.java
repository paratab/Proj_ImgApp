package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserManagement extends AppCompatActivity implements View.OnClickListener {

    Button btLogout, btEditData;
    TextView edtUsername, edtID, edtPassword, edtTelephone, edtName, edtEmail;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        btLogout = (Button) findViewById(R.id.btLogout);
        btEditData = (Button) findViewById(R.id.btEditData);
        edtUsername = (TextView) findViewById(R.id.edtUsername);
        edtPassword = (TextView) findViewById(R.id.edtPassword);
        edtName = (TextView) findViewById(R.id.edtName);
        edtID = (TextView) findViewById(R.id.edtID);
        edtEmail = (TextView) findViewById(R.id.edtEmail);
        edtTelephone = (TextView) findViewById(R.id.edtPhone);

        btLogout.setOnClickListener(this);
        btEditData.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

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
        switch (v.getId()) {
            case R.id.btLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                finish();
                break;

            case R.id.btEditData:
                intent = new Intent(this, UserDetailEdit.class);
                startActivity(intent);
                break;
        }
    }

}
