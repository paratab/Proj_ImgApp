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

import bolona_pig.proj_imgapp.CallBack.GetClueCallback;
import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.ObjectClass.Clue;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;


public class ClueAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    final int SELECT_IMAGE_GALLERY = 1;
    final int SELECT_IMAGE_CAMERA = 2;
    final int MAP_LOCATION_REQUEST = 3;

    ImageButton btClueAdd, location;
    EditText edtClueDate, edtCluePlace, edtClueDetail;
    TextView tvClueAdder, tvCluePhone;
    UserLocalStore userLocalStore;
    DateTime dateTime;
    User user;
    ServerRequest serverRequest;
    ImageView imageView;
    MidModule MidModule;
    Boolean isSexSelected = false;
    String sex;
    boolean isDirectMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_add);

        edtClueDate = (EditText) findViewById(R.id.edtClueDate);
        edtCluePlace = (EditText) findViewById(R.id.edtCluePlace);
        edtClueDetail = (EditText) findViewById(R.id.edtClueDetail);
        tvClueAdder = (TextView) findViewById(R.id.tvClueAdder);
        tvCluePhone = (TextView) findViewById(R.id.tvCluePhone);
        btClueAdd = (ImageButton) findViewById(R.id.btClueAdd);
        imageView = (ImageView) findViewById(R.id.imageView);
        location = (ImageButton) findViewById(R.id.location);

        edtClueDate.setOnClickListener(this);
        edtClueDate.setOnFocusChangeListener(this);
        btClueAdd.setOnClickListener(this);
        imageView.setOnClickListener(this);
        location.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);
        user = userLocalStore.getLoggedInUser();
        serverRequest = new ServerRequest(this);
        MidModule = new MidModule();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        edtClueDate.setText("21-Feb-2016");
//        edtCluePlace.setText("ใต้สพาน ตลาดสุวรรณภูมิ");
//        edtClueDetail.setText("พบเห็นมาถามทาง จึงขอถ่ายรูปมาช่วยตามหาผู้ปกครอง");
        isDirectMode = getIntent().getExtras().getBoolean("DirectMode", false);
        tvClueAdder.setText(user.name);
        tvCluePhone.setText(user.telephone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtClueDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtClueDate.setText(date);
                    }
                });
                break;
            case R.id.btClueAdd:
                ClueInfoAdd();
                break;
            case R.id.imageView:
                selectImage();
                break;
            case R.id.location:
                Intent intent = new Intent(this, MapsActivity.class);
                String temp = edtCluePlace.getText().toString();
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
        } else if (requestCode == SELECT_IMAGE_CAMERA && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (requestCode == MAP_LOCATION_REQUEST && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0.0);
            double lng = data.getDoubleExtra("lng", 0.0);
            String temp = "[Lat/Lng] : [" + lat + "," + lng + "]";
            edtCluePlace.setText(temp);
        }
    }

    private void ClueInfoAdd() {
        String date = edtClueDate.getText().toString();
        String place = edtCluePlace.getText().toString();
        String detail = edtClueDetail.getText().toString();

        Bitmap image;
        String imageStr;

        try {
            image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageStr = MidModule.bitmapToString(image);
        } catch (Exception e) {
            Log.e("custom_check", "Image is null, " + e.toString());
            Toast.makeText(this, "กรุณาเลือกรูปภาพ", Toast.LENGTH_SHORT).show();
            return;
        }

        Clue info = new Clue(-1, sex, date, place, detail, user.username, user.name, user.telephone, imageStr);
        serverRequest.storeClueDataInBG(info, new GetClueCallback() {
            @Override
            public void done(Clue returnInfo, String resultStr) {
                if (returnInfo == null) {
                    MidModule.showAlertDialog(resultStr, ClueAdd.this);
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
                case R.id.edtClueDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtClueDate.setText(date);
                        }
                    });
                    break;
            }
        }
    }

    public void showResult(Clue info) {
        Intent intent = new Intent();
        intent.putExtra("ClueId", info.id + "");
        setResult(RESULT_OK, intent);
        finish();
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
