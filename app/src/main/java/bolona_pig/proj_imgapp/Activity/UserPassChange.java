package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class UserPassChange extends AppCompatActivity implements View.OnClickListener {

    EditText edtPassword, edtNewPassword, edtReplyNewPassword;
    ImageButton btSavePassword;
    UserLocalStore userLocalStore;
    EncCheckModule encCheckModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass_change);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        edtReplyNewPassword = (EditText) findViewById(R.id.edtReplyNewPassword);
        btSavePassword = (ImageButton) findViewById(R.id.btSavePassword);

        btSavePassword.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        encCheckModule = new EncCheckModule();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSavePassword:
                String password = edtPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String replyNewPassword = edtReplyNewPassword.getText().toString();

                password = encCheckModule.getSHA1Hash(password);
                newPassword = encCheckModule.getSHA1Hash(newPassword);
                replyNewPassword = encCheckModule.getSHA1Hash(replyNewPassword);

                User user = userLocalStore.getLoggedInUser();
                if (!password.equals(user.password)) {
                    Toast.makeText(this, "รหัสผ่านปัจจุบัน", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!newPassword.equals(replyNewPassword)) {
                    Toast.makeText(this, "รหัสผ่านใหม่กับยืนยันรหัสผ่านไม่ตรงกัน", Toast.LENGTH_SHORT).show();
                    break;
                }

                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.updateUserPasswordInBG(user, newPassword, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser != null) {
                            userLocalStore.storeUserData(returnedUser);
                            Toast.makeText(UserPassChange.this, "เปลี่ยนรหัสผ่านเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(UserPassChange.this, "ไม่สามารถเปลี่ยนรหัสผ่าน", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;
        }
    }
}
