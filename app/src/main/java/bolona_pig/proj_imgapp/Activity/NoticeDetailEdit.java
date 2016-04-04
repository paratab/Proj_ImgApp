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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeDetailEdit extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    final int SELECT_IMAGE_GALLERY = 1;
    final int SELECT_IMAGE_CAMERA = 2;
    final int MAP_LOCATION_REQUEST = 3;
    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    ImageButton btNoticeUpdate, location;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    Notice recentNotice;
    DateTime dateTime;
    ImageView imageView;
    Boolean imageChange;
    MidModule MidModule;
    String sex;
    RadioButton radioMale, radioFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);

        edtLnName = (EditText) findViewById(R.id.edtLnName);
        edtLnBirthDate = (EditText) findViewById(R.id.edtLnBirthDate);
        edtLnPlace = (EditText) findViewById(R.id.edtLnPlace);
        edtLnLostDate = (EditText) findViewById(R.id.edtLnLostDate);
        edtLnDetail = (EditText) findViewById(R.id.edtLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btNoticeUpdate = (ImageButton) findViewById(R.id.btNoticeUpdate);
        imageView = (ImageView) findViewById(R.id.imageView);
        radioMale = (RadioButton) findViewById(R.id.sexMale);
        radioFemale = (RadioButton) findViewById(R.id.sexFemale);
        location = (ImageButton) findViewById(R.id.location);

        btNoticeUpdate.setOnClickListener(this);
        imageView.setOnClickListener(this);

        edtLnBirthDate.setOnClickListener(this);
        edtLnBirthDate.setOnFocusChangeListener(this);

        edtLnLostDate.setOnClickListener(this);
        edtLnLostDate.setOnFocusChangeListener(this);

        location.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        MidModule = new MidModule();
        dateTime = new DateTime(this);
        imageChange = false;

        recentNotice = (Notice) getIntent().getSerializableExtra("notice");
    }

    protected void onStart() {
        super.onStart();
        edtLnName.setText(recentNotice.name);
        edtLnBirthDate.setText(recentNotice.birthDate);
        edtLnPlace.setText(recentNotice.lostPlace);
        edtLnLostDate.setText(recentNotice.lostDate);
        edtLnDetail.setText(recentNotice.detail);
        tvLnAdder.setText(recentNotice.adderName);
        tvLnPhone.setText(recentNotice.telephone);
        sex = recentNotice.sex;

        if (sex.equals("ชาย")) radioMale.setChecked(true);
        else radioFemale.setChecked(true);

        if (!imageChange)
            Picasso.with(this).load(recentNotice.imagePath).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNoticeUpdate:
                upDateNoticeProcess();
                break;
            case R.id.edtLnBirthDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtLnBirthDate.setText(date);
                    }
                });
                break;
            case R.id.edtLnLostDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtLnLostDate.setText(date);
                    }
                });
                break;
            case R.id.imageView:
                selectImage();
                break;
            case R.id.location:
                Intent intent = new Intent(this, MapsActivity.class);
                String temp = edtLnPlace.getText().toString();
                intent.putExtra("latlng", temp);
                startActivityForResult(intent, MAP_LOCATION_REQUEST);
                break;
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
            imageChange = true;
            Intent intent = getIntent();
            intent.putExtra("imageChange", true);
            setResult(RESULT_OK, getIntent());
        } else if (requestCode == SELECT_IMAGE_CAMERA && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageChange = true;
            Intent intent = getIntent();
            intent.putExtra("imageChange", true);
            setResult(RESULT_OK, getIntent());
        } else if (requestCode == MAP_LOCATION_REQUEST && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0.0);
            double lng = data.getDoubleExtra("lng", 0.0);
            String temp = "[Lat/Lng] : [" + lat + "," + lng + "]";
            edtLnPlace.setText(temp);
        }
    }

    private void upDateNoticeProcess() {
        User user = userLocalStore.getLoggedInUser();

        int id = recentNotice.id;
        String name = edtLnName.getText().toString();
        String birthDate = edtLnBirthDate.getText().toString();
        String lostPlace = edtLnPlace.getText().toString();
        String lostDate = edtLnLostDate.getText().toString();
        String detail = edtLnDetail.getText().toString();

        Bitmap image;
        String imageStr;

        try {
            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageStr = MidModule.bitmapToString(image);
        } catch (Exception e) {
            Log.e("custom_check", "Image is null, " + e.toString());
            Toast.makeText(this, "ยังไม่มีการเลือกรูปภาพ", Toast.LENGTH_SHORT).show();
            return;
        }

        Notice notice = new Notice(id, name, sex, birthDate, lostPlace, lostDate, detail, user.username, user.name, user.telephone, imageStr);
        serverRequest.updateNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice, String resultStr) {
                if (returnNotice == null) {
                    MidModule.showAlertDialog(resultStr, NoticeDetailEdit.this);
                } else {
                    showResult();
                }
            }
        });
    }

    public void showResult() {
        Toast.makeText(this, "แก้ไขข้อมูลเรียบร้อย", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void printError() {
        Toast.makeText(this, "ไม่สามารถแก้ไขข้อมูลประกาศได้", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.btNoticeUpdate:
                    upDateNoticeProcess();
                    break;
                case R.id.edtLnBirthDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtLnBirthDate.setText(date);
                        }
                    });
                    break;
                case R.id.edtLnLostDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtLnLostDate.setText(date);
                        }
                    });
            }
        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.sexMale:
                if (checked)
                    sex = "ชาย";
                break;
            case R.id.sexFemale:
                if (checked)
                    sex = "หญิง";
                break;
        }
    }
}
