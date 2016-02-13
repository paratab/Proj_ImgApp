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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.EncCheckModule;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeEdit extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public final int SELECT_IMAGE = 1;
    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    Button btNoticeUpdate;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    Notice recentNotice;
    DateTime dateTime;
    ImageView imageView;
    Boolean imageChange;
    EncCheckModule encCheckModule;

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
        btNoticeUpdate = (Button) findViewById(R.id.btNoticeUpdate);
        imageView = (ImageView) findViewById(R.id.imageView);

        btNoticeUpdate.setOnClickListener(this);
        imageView.setOnClickListener(this);

        edtLnBirthDate.setOnClickListener(this);
        edtLnBirthDate.setOnFocusChangeListener(this);

        edtLnLostDate.setOnClickListener(this);
        edtLnLostDate.setOnFocusChangeListener(this);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        encCheckModule = new EncCheckModule();
        dateTime = new DateTime(this);
        imageChange = false;

        recentNotice = (Notice) getIntent().getSerializableExtra("notice");
    }

    protected void onStart() {
        super.onStart();
        edtLnName.setText(recentNotice.lnName);
        edtLnBirthDate.setText(recentNotice.lnBirthDate);
        edtLnPlace.setText(recentNotice.lnPlace);
        edtLnLostDate.setText(recentNotice.lnLostDate);
        edtLnDetail.setText(recentNotice.lnDetail);
        tvLnAdder.setText(recentNotice.lnAdder);
        tvLnPhone.setText(recentNotice.lnPhone);
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
                Intent galleryAct = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryAct, SELECT_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageChange = true;
            Intent intent = getIntent();
            intent.putExtra("imageChange", true);
            setResult(RESULT_OK, getIntent());
        }
    }

    private void upDateNoticeProcess() {
        User user = userLocalStore.getLoggedInUser();

        int id = recentNotice.id;
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
            Toast.makeText(this, "You not select any Picture yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        Notice notice = new Notice(id, lnName, lnBirthDate, lnPlace, lnDate, lnDetail, lnAdder, lnPhone, imageStr);
        serverRequest.updateNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice) {
                if (returnNotice == null) {
                    printError();
                } else {
                    showResult();
                }
            }
        });
    }

    public void showResult() {
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void printError() {
        Toast.makeText(this, "Cannot Update Notice, Make sure internet is working.", Toast.LENGTH_SHORT).show();
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
}
