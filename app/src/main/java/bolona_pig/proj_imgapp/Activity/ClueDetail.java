package bolona_pig.proj_imgapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetBooleanCallBack;
import bolona_pig.proj_imgapp.CallBack.GetClueCallback;
import bolona_pig.proj_imgapp.ObjectClass.Clue;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class ClueDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvClueDate, tvCluePlace, tvClueDetail, tvClueAdder, tvCluePhone, tvSex;
    ServerRequest serverRequest;
    Clue clueInfo;
    ImageView imageView;
    Button imbTel, imbMessage, imbSave, imbDelete;
    ImageButton location;
    LinearLayout gridUser;
    UserLocalStore userLocalStore;
    int notice_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_detail);

        tvClueDate = (TextView) findViewById(R.id.tvClueDate);
        tvCluePlace = (TextView) findViewById(R.id.tvCluePlace);
        tvClueDetail = (TextView) findViewById(R.id.tvClueDetail);
        tvClueAdder = (TextView) findViewById(R.id.tvClueAdder);
        tvCluePhone = (TextView) findViewById(R.id.tvCluePhone);
        imageView = (ImageView) findViewById(R.id.imageView);
        imbTel = (Button) findViewById(R.id.imbTelephone);
        imbMessage = (Button) findViewById(R.id.imbMessage);
        gridUser = (LinearLayout) findViewById(R.id.gridUser);
        tvSex = (TextView) findViewById(R.id.tvSex);
        location = (ImageButton) findViewById(R.id.location);
        imbSave = (Button) findViewById(R.id.imbSave);
        imbDelete = (Button) findViewById(R.id.imbDelete);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        imbTel.setOnClickListener(this);
        imbMessage.setOnClickListener(this);
        location.setOnClickListener(this);
        imbSave.setOnClickListener(this);
        imbDelete.setOnClickListener(this);

        loadClueData();
    }

    //@Override
    protected void loadClueData() {
        super.onStart();
        int infoId = Integer.parseInt(getIntent().getStringExtra("clueId"));
        String menu = getIntent().getStringExtra("menu");

        if (menu.equals("save")) {
            notice_id = Integer.parseInt(getIntent().getStringExtra("noticeId"));
            imbSave.setVisibility(View.VISIBLE);
        } else if (menu.equals("delete")) {
            imbDelete.setVisibility(View.VISIBLE);
        }

        serverRequest.fetchClueDataInBG(infoId, new GetClueCallback() {
            @Override
            public void done(Clue returnInfo, String resultStr) {
                if (returnInfo == null) {
                    Toast.makeText(ClueDetail.this, resultStr, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showClueInfo(returnInfo);
                }
            }
        });

        if (clueInfo.seenPlace.startsWith("[พิกัด]")) {
            location.setVisibility(View.VISIBLE);
        }
    }

    private void showClueInfo(Clue info) {
        tvClueDate.setText(info.seenDate);
        tvCluePlace.setText(info.seenPlace);
        tvClueDetail.setText(info.detail);
        tvClueAdder.setText(info.adderName);
        tvCluePhone.setText(info.telephone);
        tvSex.setText(info.sex);

        Picasso.with(this).load(info.imagePath).fit().centerCrop().into(imageView);
        clueInfo = info;

        User user = userLocalStore.getLoggedInUser();
        if (!user.username.equals(clueInfo.adderUsername)) {
            gridUser.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imbTelephone:
                String uri = "tel:" + tvCluePhone.getText();
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.imbMessage:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", tvCluePhone.getText());
                startActivity(intent);
                break;
            case R.id.location:
                intent = new Intent(this, Maps2Activity.class);
                String temp = tvCluePlace.getText().toString();
                intent.putExtra("latlng", temp);
                startActivity(intent);
                break;
            case R.id.imbSave:
                updateClueStatus(userLocalStore.getLoggedInUser().username);
                break;
            case R.id.imbDelete:
                deleteClueStatus(userLocalStore.getLoggedInUser().username);
                break;
        }
    }

    private void deleteClueStatus(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ลบเบาะแส");
        builder.setMessage("คุณแน่ใจที่จะ \"ลบเบาะแส\" ออกจากรายการบันทึกหรือไม่");
        builder.setNegativeButton("ยกเลิก", null);
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serverRequest.updateClueStatusDelete(clueInfo.id, username, new GetBooleanCallBack() {
                    @Override
                    public void done(Boolean flag, String resultStr) {
                        if (flag != null && flag) {
                            Toast.makeText(ClueDetail.this, "ลบเบาะแสเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            MidModule midModule = new MidModule();
                            midModule.showAlertDialog(resultStr, ClueDetail.this);
                        }
                    }
                });
            }
        });
        builder.show();
    }

    private void updateClueStatus(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("จัดเก็บข้อมูล");
        builder.setMessage("คุณแน่ใจที่จะ\"จัดเก็บเบาะแส\"\n ไว้หรือไม่");
        builder.setNegativeButton("ยกเลิก", null);
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serverRequest.updateClueStatusSave(clueInfo.id, notice_id, username, new GetBooleanCallBack() {
                    @Override
                    public void done(Boolean flag, String resultStr) {
                        if (flag != null && flag) {
                            Toast.makeText(ClueDetail.this, "จัดเก็บเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            MidModule midModule = new MidModule();
                            midModule.showAlertDialog(resultStr, ClueDetail.this);
                        }
                    }
                });
            }
        });
        builder.show();
    }
}
