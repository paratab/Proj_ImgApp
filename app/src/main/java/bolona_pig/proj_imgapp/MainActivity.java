package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btUserManagement, btNoticeAdd, btGoogle, btNoticeEdit;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btUserManagement = (Button) findViewById(R.id.btUserManagement);
        btGoogle = (Button) findViewById(R.id.btGoogle);
        btNoticeAdd = (Button) findViewById(R.id.btNoticeAdd);
        btNoticeEdit = (Button) findViewById(R.id.btNoticeEdit);
        userLocalStore = new UserLocalStore(this);

        btUserManagement.setOnClickListener(this);
        btGoogle.setOnClickListener(this);
        btNoticeAdd.setOnClickListener(this);
        btNoticeEdit.setOnClickListener(this);


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
                    startActivityForResult(intent, 111);
                } else {
                    intent = new Intent(this, UserManagement.class);
                    startActivity(intent);
                }
                break;
            case R.id.btNoticeAdd:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, 222);
                } else {
                    intent = new Intent(this, NoticeAdd.class);
                    startActivityForResult(intent, 333);
                }
                break;
            case R.id.btNoticeEdit:
                intent = new Intent(this, NoticeManagement.class);
                intent.putExtra("noticeId", "1");
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
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, UserManagement.class);
                startActivity(intent);
            }
        } else if (requestCode == 222) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, NoticeAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == 333) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("ID");
                Intent intent = new Intent(this, NoticeManagement.class);
                intent.putExtra("noticeId", id);
                startActivity(intent);
            }
        }
    }
}
