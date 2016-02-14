package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.R;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button btRegister;
    EditText edtUsername, edtPassword, edtReplyPassword, edtName, edtID, edtEmail, edtTelephone;
    EncCheckModule encCheckModule;
    ImageView imageView;


    @Override
    protected void onStart() {
        super.onStart();
    }

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
        imageView = (ImageView) findViewById(R.id.imageUpload);

        encCheckModule = new EncCheckModule();

        btRegister.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
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

                Bitmap image;
                String imageStr;

                try {
                    image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    imageStr = encCheckModule.bitmapToString(image);
                } catch (Exception e) {
                    Log.e("custom_check", "Image is null, " + e.toString());
                    Toast.makeText(this, "ยังไม่มีการเลือกรูปภาพ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkInput(username, password, replyPassword, nationId, email, telephone)) {
                    break;
                }

                password = encCheckModule.getSHA1Hash(password);

                User user = new User(username, password, name, nationId, email, telephone, imageStr);

                registerUser(user);

                break;
            case R.id.imageUpload:
                Intent galleryAct = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryAct, 111);
                break;
        }
    }

    private void registerUser(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBG(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser != null) {
                    Toast.makeText(Register.this, "ลงทะเบียนเรียบร้อย", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Register.this, "ลงทะเบียนผิดพลาด", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkInput(String username, String password, String replyPassword, String nationId, String email, String telephone) {

        CharSequence text;
        if (username.isEmpty()) {
            text = "กรุญากรอกชื่อผู้ใช้งาน";
            encCheckModule.printError(this, text);
            return false;
        }
        if (!encCheckModule.isValidUsername(username)) {
            text = "ชื่อผู้ใช้งานไม่ถูกต้อง ควรมี 3-20 ตัว เป็นภาษาอังกฤษหรือตัวเลข";
            encCheckModule.printError(this, text);
            return false;
        }
        if (password.isEmpty()) {
            text = "กรุณากรอกรหัสผ่าน";
            encCheckModule.printError(this, text);
            return false;
        }
        if (!password.equals(replyPassword)) {
            text = "รหัสผ่านกับยืนยันรหัสผ่านไม่ตรงกัน";
            encCheckModule.printError(this, text);
            return false;
        }
        if (nationId.length() != 13) {
            text = "เลขประจำตัวประชาชนต้องมี 13 ตัว";
            encCheckModule.printError(this, text);
            return false;
        }
        if (email.isEmpty()) {
            text = "กรุณากรอกอีเมล";
            encCheckModule.printError(this, text);
            return false;
        }
        if (!encCheckModule.isValidEmail(email)) {
            text = "รุปแบบอีเมลผิดพลาด";
            encCheckModule.printError(this, text);
            return false;
        }
        if (telephone.isEmpty()) {
            text = "กรุณากรอกหมายเลขโทรศัพท์";
            encCheckModule.printError(this, text);
            return false;
        }

        return true;
    }
}
