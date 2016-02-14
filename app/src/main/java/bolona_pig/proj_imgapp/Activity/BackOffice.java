package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class BackOffice extends AppCompatActivity implements View.OnClickListener {

    public final int LOGIN_USER_MANAGEMENT = 1;
    public final int LOGIN_NOTICE_ADD = 2;
    public final int LOGIN_SEENINFO_ADD = 3;
    public final int NOTICE_ADD_GET_ID = 4;
    public final int SEENINFO_ADD_GET_ID = 5;
    Button btUserManagement, btNoticeAdd, btGoogle, btNoticeEdit, btSeenAdd, btSeenInfo;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btUserManagement = (Button) findViewById(R.id.btUserManagement);
        btGoogle = (Button) findViewById(R.id.btGoogle);
        btNoticeAdd = (Button) findViewById(R.id.btNoticeAdd);
        btNoticeEdit = (Button) findViewById(R.id.btNoticeEdit);
        btSeenAdd = (Button) findViewById(R.id.btSeenAdd);
        btSeenInfo = (Button) findViewById(R.id.btSeenInfo);

        userLocalStore = new UserLocalStore(this);

        btUserManagement.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
        btNoticeAdd.setOnClickListener(this);
        btNoticeEdit.setOnClickListener(this);
        btSeenAdd.setOnClickListener(this);
        btSeenInfo.setOnClickListener(this);
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
                    intent = new Intent(this, SeenInfoAdd.class);
                    startActivityForResult(intent, SEENINFO_ADD_GET_ID);
                }
                break;
            case R.id.btSeenInfo:
                intent = new Intent(this, SeenInfoDetail.class);
                intent.putExtra("seenId", "1");
                startActivity(intent);
                break;
            case R.id.btGoogle:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
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
                Intent intent = new Intent(this, SeenInfoAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == SEENINFO_ADD_GET_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("seenId");
                Intent intent = new Intent(this, SeenInfoDetail.class);
                intent.putExtra("seenId", id);
                startActivity(intent);
            }
        }
    }
}
