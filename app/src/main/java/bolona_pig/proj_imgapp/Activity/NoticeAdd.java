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
import android.widget.TextView;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public final int SELECT_IMAGE = 1;
    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    Button btAddNotice;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    DateTime dateTime;
    ImageView imageView;
    EncCheckModule encCheckModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);

        edtLnName = (EditText) findViewById(R.id.edtLnName);
        edtLnBirthDate = (EditText) findViewById(R.id.edtLnBirthDate);
        edtLnPlace = (EditText) findViewById(R.id.edtLnPlace);
        edtLnLostDate = (EditText) findViewById(R.id.edtLnLostDate);
        edtLnDetail = (EditText) findViewById(R.id.edtLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btAddNotice = (Button) findViewById(R.id.btNoticeAdd);
        imageView = (ImageView) findViewById(R.id.imageView);

        btAddNotice.setOnClickListener(this);
        imageView.setOnClickListener(this);

        edtLnBirthDate.setOnFocusChangeListener(this);
        edtLnBirthDate.setOnClickListener(this);

        edtLnLostDate.setOnFocusChangeListener(this);
        edtLnLostDate.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        dateTime = new DateTime(this);
        encCheckModule = new EncCheckModule();
    }

    @Override
    public void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        tvLnAdder.setText(user.name);
        tvLnPhone.setText(user.telephone);
        edtLnLostDate.setText(dateTime.getCurrentDate());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btNoticeAdd:
                noticeAddMethod();
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
                Intent galleryAct = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryAct, SELECT_IMAGE);
                break;
        }
    }

    private void noticeAddMethod() {
        User user = userLocalStore.getLoggedInUser();

        String lnName = edtLnName.getText().toString();
        String lnBirthDate = edtLnBirthDate.getText().toString();
        String lnPlace = edtLnPlace.getText().toString();
        String lnDate = edtLnLostDate.getText().toString();
        String lnDetail = edtLnDetail.getText().toString();
        String lnAdder = user.username;
        String lnPhone = user.telephone;

        Bitmap image;
        String imageStr;

        try {
            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageStr = encCheckModule.bitmapToString(image);
        } catch (Exception e) {
            Log.e("custom_check", "Image is null, " + e.toString());
            Toast.makeText(this, "ยังไม่มีการเลือกรูปภาพ.", Toast.LENGTH_SHORT).show();
            return;
        }

        Notice notice = new Notice(-1, lnName, lnBirthDate, lnPlace, lnDate, lnDetail, lnAdder, lnPhone, imageStr);
        serverRequest.storeNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice) {
                if (returnNotice == null) {
                    showError();
                } else {
                    showResult(returnNotice);
                }
            }
        });
    }

    public void showError() {
        Toast.makeText(this, "ไม่สามารถเพิ่มข้อมูลประกาศได้", Toast.LENGTH_SHORT).show();
    }

    public void showResult(Notice notice) {
        Intent intent = new Intent();
        intent.putExtra("ID", notice.id + "");
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "สร้างประกาศเรียบร้อย", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
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
            }
        }
    }
}
