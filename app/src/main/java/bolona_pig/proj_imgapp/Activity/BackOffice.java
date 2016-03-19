package bolona_pig.proj_imgapp.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class BackOffice extends AppCompatActivity implements View.OnClickListener {

    public final int LOGIN_USER_MANAGEMENT = 1;
    public final int LOGIN_NOTICE_ADD = 2;
    public final int LOGIN_SEENINFO_ADD = 3;
    public final int NOTICE_ADD_GET_ID = 4;
    public final int SEENINFO_ADD_GET_ID = 5;
    public final int NOTIFY_ID = 999;

    Button btUserManagement, btNoticeAdd, btGoogle, btNoticeEdit, btSeenAdd, btSeenInfo, btPushNotify;
    UserLocalStore userLocalStore;

    EditText sec, info;

    android.support.v4.app.NotificationCompat.Builder notifyBuilder;
    NotificationManager notifyManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office);

        btUserManagement = (Button) findViewById(R.id.btUserManagement);
        btGoogle = (Button) findViewById(R.id.btGoogle);
        btNoticeAdd = (Button) findViewById(R.id.btNoticeAdd);
        btNoticeEdit = (Button) findViewById(R.id.btNoticeEdit);
        btSeenAdd = (Button) findViewById(R.id.btSeenAdd);
        btSeenInfo = (Button) findViewById(R.id.btSeenInfo);
        btPushNotify = (Button) findViewById(R.id.btPushNotify);
        sec = (EditText) findViewById(R.id.sec);
        info = (EditText) findViewById(R.id.infoId);

        userLocalStore = new UserLocalStore(this);

        btUserManagement.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
        btNoticeAdd.setOnClickListener(this);
        btNoticeEdit.setOnClickListener(this);
        btSeenAdd.setOnClickListener(this);
        btSeenInfo.setOnClickListener(this);
        btPushNotify.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btUserManagement:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, LOGIN_USER_MANAGEMENT);
                } else {
                    intent = new Intent(this, UserDetail.class);
                    startActivity(intent);
                }
                break;
            case R.id.btNoticeAdd:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, LOGIN_NOTICE_ADD);
                } else {
                    intent = new Intent(this, NoticeAdd.class);
                    startActivityForResult(intent, NOTICE_ADD_GET_ID);
                }
                break;
            case R.id.btNoticeEdit:
                intent = new Intent(this, NoticeDetail.class);
                intent.putExtra("noticeId", "1");
                startActivity(intent);
                break;
            case R.id.btSeenAdd:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, LOGIN_SEENINFO_ADD);
                } else {
                    intent = new Intent(this, ClueAdd.class);
                    startActivityForResult(intent, SEENINFO_ADD_GET_ID);
                }
                break;
            case R.id.btSeenInfo:
                intent = new Intent(this, ClueDetail.class);
                intent.putExtra("clueId", "1");
                startActivity(intent);
                break;
            case R.id.btGoogle:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btPushNotify:

                int seco = Integer.parseInt(sec.getText().toString());

                int millis = seco * 1000;

                Log.i("custom_check", "Timer start");

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Log.i("custom_check", "Timer stop");
                        NotifyPush();
                    }
                }, millis);
                break;
        }
    }

    private void NotifyPush() {
        Intent intent = new Intent(this, ClueDetail.class);
        intent.putExtra("seenId", info.getText().toString());
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_description_white_24dp);

        notifyBuilder = new NotificationCompat.Builder(this).setTicker("มีเบาะแสใหม่ !").setSmallIcon(R.drawable.ic_description_white_24dp).setLargeIcon(icon)
                .setContentTitle("มีเบาะแสใหม่แจ้งเข้าสู่ระบบ").setContentText("มีบุคคลแจ้งเบาะเเสเข้ามา และระบบคาดว่าจะจะเบาะแสเกี่ยวข้องกับประกาศของคุณ").setContentIntent(pIntent)
                .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND).setVibrate(new long[]{100, 3000, 500, 1000});

        Notification notification = notifyBuilder.build();

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notifyManager.notify(NOTIFY_ID, notification);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_USER_MANAGEMENT) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, UserDetail.class);
                startActivity(intent);
            }
        } else if (requestCode == LOGIN_NOTICE_ADD) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, NoticeAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == NOTICE_ADD_GET_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("ID");
                Intent intent = new Intent(this, NoticeDetail.class);
                intent.putExtra("noticeId", id);
                startActivity(intent);
            }
        } else if (requestCode == LOGIN_SEENINFO_ADD) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, ClueAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == SEENINFO_ADD_GET_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("seenId");
                Intent intent = new Intent(this, ClueDetail.class);
                intent.putExtra("seenId", id);
                startActivity(intent);
            }
        }
    }
}
