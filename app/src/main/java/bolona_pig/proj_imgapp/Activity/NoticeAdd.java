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

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;
import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public final int SELECT_IMAGE_GALLERY = 1;
    public final int SELECT_IMAGE_CAMERA = 2;
    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    ImageButton btAddNotice;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    DateTime dateTime;
    ImageView imageView;
    MidModule MidModule;
    Boolean isSexSelected = false;
    String sex;

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
        btAddNotice = (ImageButton) findViewById(R.id.btNoticeAdd);
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
        MidModule = new MidModule();
    }

    @Override
    public void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        serverRequest.checkUserNoticeNumberInBG(user.username, new GetBooleanCallBack() {
            @Override
            public void done(Boolean flag, String resultStr) {
                if (flag == null || !flag) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoticeAdd.this);
                    dialogBuilder.setTitle("ข้อผิดพลาด");
                    dialogBuilder.setMessage(resultStr);
                    dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                    dialogBuilder.setNegativeButton("รับทราบ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dialogBuilder.show();
                }
            }
        });
//        edtLnName.setText("Test Lost Data1");
//        edtLnBirthDate.setText("19-May-1995");
//        edtLnPlace.setText("ECC KMITL");
//        edtLnLostDate.setText("20-Feb-2016");
//        edtLnDetail.setText("เป็นชาย สูง ท้วม หายออกจากตึก ecc kmitl");
        tvLnAdder.setText(user.name);
        tvLnPhone.setText(user.telephone);
        edtLnLostDate.setText(dateTime.getCurrentDate());
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

    @Override
    public void onClick(View v) {
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
                selectImage();
                break;
        }
    }

    private void noticeAddMethod() {
        User user = userLocalStore.getLoggedInUser();

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
            Toast.makeText(this, "กรุณาเลือกรูปภาพ.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isSexSelected) {
            Toast.makeText(NoticeAdd.this, "กรุณาเลือกเพศของผู้สูญหาย", Toast.LENGTH_SHORT).show();
            return;
        }

        Notice notice = new Notice(-1, name, sex, birthDate, lostPlace, lostDate, detail, user.username, user.name, user.telephone, imageStr);
        serverRequest.storeNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice, String resultStr) {
                if (returnNotice == null) {
                    MidModule.showAlertDialog(resultStr, NoticeAdd.this);
                } else {
                    showResult(returnNotice);
                }
            }
        });
    }

    public void showResult(Notice notice) {
        Intent intent = new Intent();
        intent.putExtra("noticeId", notice.id + "");
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "สร้างประกาศเสร็จสิ้น", Toast.LENGTH_SHORT).show();
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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        isSexSelected = checked;
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
