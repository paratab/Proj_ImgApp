package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.ObjectClass.mixMidModule;
import bolona_pig.proj_imgapp.R;

public class UserPassChange extends AppCompatActivity implements View.OnClickListener {

    EditText edtPassword, edtNewPassword, edtReplyNewPassword;
    ImageButton btSavePassword;
    UserLocalStore userLocalStore;
    mixMidModule mixMidModule;

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
        mixMidModule = new mixMidModule();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSavePassword:
                String password = edtPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String replyNewPassword = edtReplyNewPassword.getText().toString();

                password = mixMidModule.getSHA1Hash(password);
                newPassword = mixMidModule.getSHA1Hash(newPassword);
                replyNewPassword = mixMidModule.getSHA1Hash(replyNewPassword);

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
                    public void done(User returnedUser, String resultStr) {
                        if (returnedUser != null) {
                            userLocalStore.storeUserData(returnedUser);
                            Toast.makeText(UserPassChange.this, "เปลี่ยนรหัสผ่านเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            mixMidModule.showAlertDialog(resultStr, UserPassChange.this);
                        }
                    }
                });

                break;
        }
    }
}
