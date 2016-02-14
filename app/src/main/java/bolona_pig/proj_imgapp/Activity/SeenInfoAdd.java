package bolona_pig.proj_imgapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;


public class SeenInfoAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public final int SELECT_IMAGE_GALLERY = 1;
    public final int SELECT_IMAGE_CAMERA = 2;
    Button btSeenAdd;
    EditText edtSeenDate, edtSeenPlace, edtSeenDetail;
    TextView tvSeenAdder, tvSeenPhone;
    UserLocalStore userLocalStore;
    DateTime dateTime;
    User user;
    ServerRequest serverRequest;
    ImageView imageView;
    EncCheckModule encCheckModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeninfo_add);

        edtSeenDate = (EditText) findViewById(R.id.edtSeenDate);
        edtSeenPlace = (EditText) findViewById(R.id.edtSeenPlace);
        edtSeenDetail = (EditText) findViewById(R.id.edtSeenDetail);
        tvSeenAdder = (TextView) findViewById(R.id.tvSeenAdder);
        tvSeenPhone = (TextView) findViewById(R.id.tvSeenPhone);
        btSeenAdd = (Button) findViewById(R.id.btSeenAdd);
        imageView = (ImageView) findViewById(R.id.imageView);

        edtSeenDate.setOnClickListener(this);
        edtSeenDate.setOnFocusChangeListener(this);
        btSeenAdd.setOnClickListener(this);
        imageView.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);
        user = userLocalStore.getLoggedInUser();
        serverRequest = new ServerRequest(this);
        encCheckModule = new EncCheckModule();

    }

    @Override
    protected void onStart() {
        super.onStart();
        tvSeenAdder.setText(user.name);
        tvSeenPhone.setText(user.telephone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtSeenDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtSeenDate.setText(date);
                    }
                });
                break;
            case R.id.btSeenAdd:
                seenInfoAdd();
                break;
            case R.id.imageView:
                selectImage();
        }
    }

    public void selectImage() {
        final CharSequence[] items = {"ถ่ายรูป", "เลือกจากคลังภาพ", "ยกเลิก"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("เลือกรูปภาพ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("ถ่ายรูป")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, SELECT_IMAGE_CAMERA);
                } else if (items[which].equals("เลือกจากคลังภาพ")) {
                    Intent galleryAct = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryAct, SELECT_IMAGE_GALLERY);
                } else if (items[which].equals("ยกเลิก")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (requestCode == SELECT_IMAGE_CAMERA && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void seenInfoAdd() {
        String seenDate = edtSeenDate.getText().toString();
        String seenPlace = edtSeenPlace.getText().toString();
        String seenDetail = edtSeenDetail.getText().toString();
        String seenAdder = user.username;
        String seenPhone = user.telephone;

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

        SeenInfo info = new SeenInfo(-1, seenDate, seenPlace, seenDetail, seenAdder, seenPhone, imageStr);
        serverRequest.storeSeenInfoDataInBG(info, new GetSeenInfoCallback() {
            @Override
            public void done(SeenInfo returnInfo) {
                if (returnInfo == null) {
                    showError();
                } else {
                    showResult(returnInfo);
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edtSeenDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtSeenDate.setText(date);
                        }
                    });
                    break;
            }
        }
    }

    public void showError() {
        Toast.makeText(this, "ไม่สามารถเพิ่มข้อมูลเบอะแสเข้าสู่ระบบได้", Toast.LENGTH_SHORT).show();
    }

    public void showResult(SeenInfo info) {
        Intent intent = new Intent();
        intent.putExtra("seenId", info.seenId + "");
        setResult(RESULT_OK, intent);
        finish();
    }
}
