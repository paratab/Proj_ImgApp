package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetUserCallBack;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class UserDetailEdit extends AppCompatActivity implements View.OnClickListener {

    Button btUpdate, btChangePW;
    TextView edtUsername, edtID, edtPassword;
    EditText edtName, edtEmail, edtTelephone;
    UserLocalStore userLocalStore;
    EncCheckModule encCheckModule;
    ImageView imageView;
    boolean imageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_edit);

        btUpdate = (Button) findViewById(R.id.btUpdate);
        btChangePW = (Button) findViewById(R.id.btChangePW);
        edtUsername = (TextView) findViewById(R.id.edtUsername);
        edtPassword = (TextView) findViewById(R.id.edtPassword);
        edtName = (EditText) findViewById(R.id.edtName);
        edtID = (TextView) findViewById(R.id.edtID);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtTelephone = (EditText) findViewById(R.id.edtPhone);
        imageView = (ImageView) findViewById(R.id.imageView);

        btUpdate.setOnClickListener(this);
        btChangePW.setOnClickListener(this);
        imageView.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        encCheckModule = new EncCheckModule();
        imageChange = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        edtUsername.setText(user.username);
        edtName.setText(user.name);
        edtID.setText(user.nationId);
        edtEmail.setText(user.email);
        edtTelephone.setText(user.telephone);
        if (!imageChange) Picasso.with(this).load(user.imagePath).into(imageView);
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
                    text = "กรุณากรอกอีเมล";
                    encCheckModule.printError(this, text);
                    break;
                }
                if (!encCheckModule.isValidEmail(email)) {
                    text = "รุปแบบอีเมลผิดพลาด";
                    encCheckModule.printError(this, text);
                    break;
                }
                if (telephone.isEmpty()) {
                    text = "กรุณากรอกหมายเลขโทรศัพท์";
                    encCheckModule.printError(this, text);
                    break;
                }

                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                String imageStr = encCheckModule.bitmapToString(image);

                User user = new User(username, password, name, nationId, email, telephone, imageStr);

                ServerRequest serverRequest = new ServerRequest(this);
                serverRequest.updateUserDataInBG(user, new GetUserCallBack() {
                    @Override
                    public void done(User returnedUser) {
                        if (returnedUser != null) {
                            userLocalStore.storeUserData(returnedUser);
                            Toast.makeText(UserDetailEdit.this, "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UserDetailEdit.this, "ไม่สามารถบันทึกข้อมูลได้", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btChangePW:
                intent = new Intent(UserDetailEdit.this, UserPassChange.class);
                startActivityForResult(intent, 111);
                break;
            case R.id.imageView:
                Intent galleryAct = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryAct, 222);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else if (requestCode == 222 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageChange = true;
            getIntent().putExtra("imageChange", true);
            setResult(RESULT_OK, getIntent());
        }
    }
}
