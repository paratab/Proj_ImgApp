package bolona_pig.proj_imgapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvLnName, tvLnBirthDate, tvLnPlace, tvLnLostDate, tvLnDetail, tvLnAdder, tvLnPhone, tvLnSex;
    ServerRequest serverRequest;
    UserLocalStore userLocalStore;
    Notice recentNotice;
    DateTime dateTime;
    ImageView imageView;
    boolean imageChange;
    ImageButton imbTel, ImbMessage, imbMarkFin, imbEdit, imbDel, location;
    GridLayout gridUser, gridOwner, gridAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        tvLnName = (TextView) findViewById(R.id.tvLnName);
        tvLnBirthDate = (TextView) findViewById(R.id.tvLnBirthDate);
        tvLnPlace = (TextView) findViewById(R.id.tvLnPlace);
        tvLnLostDate = (TextView) findViewById(R.id.tvLnLostDate);
        tvLnDetail = (TextView) findViewById(R.id.tvLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        tvLnSex = (TextView) findViewById(R.id.tvLnSex);
        imageView = (ImageView) findViewById(R.id.imageView);
        imbTel = (ImageButton) findViewById(R.id.imbTelephone);
        ImbMessage = (ImageButton) findViewById(R.id.imbMessage);
        imbMarkFin = (ImageButton) findViewById(R.id.imbOwnMark);
        imbEdit = (ImageButton) findViewById(R.id.imbOwnEdit);
        imbDel = (ImageButton) findViewById(R.id.imbAdminDelete);
        gridUser = (GridLayout) findViewById(R.id.gridUser);
        gridOwner = (GridLayout) findViewById(R.id.gridOwner);
        gridAdmin = (GridLayout) findViewById(R.id.gridAdmin);
        location = (ImageButton) findViewById(R.id.location);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);

        imbTel.setOnClickListener(this);
        ImbMessage.setOnClickListener(this);
        imbMarkFin.setOnClickListener(this);
        imbEdit.setOnClickListener(this);
        imbDel.setOnClickListener(this);
        location.setOnClickListener(this);
        imageChange = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        int noticeId = Integer.parseInt(getIntent().getExtras().getString("noticeId"));
        serverRequest.fetchNoticeDataInBG(noticeId, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice, String resultStr) {
                if (returnNotice == null) {
                    Toast.makeText(NoticeDetail.this, resultStr, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showNotice(returnNotice);
                }
            }
        });

    }

    public void showNotice(Notice notice) {
        tvLnName.setText(notice.name);
        tvLnSex.setText(notice.sex);
        tvLnBirthDate.setText(notice.birthDate);
        tvLnPlace.setText(notice.lostPlace);
        tvLnLostDate.setText(notice.lostDate);
        tvLnDetail.setText(notice.detail);
        tvLnAdder.setText(notice.adderName);
        tvLnPhone.setText(notice.telephone);
        recentNotice = notice;

        if (!imageChange) Picasso.with(this).load(notice.imagePath).into(imageView);
        else
            Picasso.with(this).load(recentNotice.imagePath).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);

        imageChange = false;

        User user = userLocalStore.getLoggedInUser();
        if (user.username.equals(notice.adderUsername)) {
            gridOwner.setVisibility(View.VISIBLE);
        } else {
            gridUser.setVisibility(View.VISIBLE);
        }
        if (user.isAdmin()) gridAdmin.setVisibility(View.VISIBLE);

        String temp = notice.lostPlace;
        if (temp.startsWith("[Lat/Lng]")) {
            location.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imbTelephone:
                String uri = "tel:" + tvLnPhone.getText();
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.imbMessage:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", tvLnPhone.getText());
                startActivity(intent);
                break;
            case R.id.imbOwnMark:
                updateNoticeStatus(userLocalStore.getLoggedInUser().username);
                break;
            case R.id.imbOwnEdit:
                intent = new Intent(this, NoticeDetailEdit.class);
                intent.putExtra("notice", recentNotice);
                startActivityForResult(intent, 111);
                break;
            case R.id.imbAdminDelete:
                updateNoticeStatus(recentNotice.adderUsername);
                break;
            case R.id.location:
                intent = new Intent(this, Maps2Activity.class);
                String temp = tvLnPlace.getText().toString();
                intent.putExtra("latlng", temp);
                startActivity(intent);
                break;
        }
    }

    private void updateNoticeStatus(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันข้อมูล");
        builder.setMessage("คุณแน่ใจที่จะ\"ปรับสถานะเป็นพบตัวแล้ว\"\nหรือไม่ ");
        builder.setNegativeButton("ยกเลิก", null);
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serverRequest.updateNoticeStatus(recentNotice.id, username, new GetBooleanCallBack() {
                    @Override
                    public void done(Boolean flag, String resultStr) {
                        if (flag != null && flag) {
                            Toast.makeText(NoticeDetail.this, "แก้ไขสถานะเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            MidModule midModule = new MidModule();
                            midModule.showAlertDialog(resultStr, NoticeDetail.this);
                        }
                    }
                });
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            imageChange = data.getExtras().getBoolean("imageChange");
        }
    }
}
