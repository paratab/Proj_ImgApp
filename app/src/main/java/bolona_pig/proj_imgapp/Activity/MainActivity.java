package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.ObjectClass.NoticeGridAdapter;
import bolona_pig.proj_imgapp.ObjectClass.NoticeItem;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final int LOGIN_USER_MANAGEMENT = 1;
    public final int LOGIN_NOTICE_ADD = 2;
    public final int LOGIN_SEENINFO_ADD = 3;
    public final int NOTICE_ADD_GET_ID = 4;
    public final int SEENINFO_ADD_GET_ID = 5;
    FloatingActionButton fabNotice, fabSeenInfo, fabMainPage2, fabLogin;
    UserLocalStore userLocalStore;
    GridView gridView;
    ProgressBar progressBar;
    NoticeGridAdapter noticeGridAdapter;
    ServerRequest serverRequest;
    FloatingActionMenu floatingActionMenu;
    private ArrayList<NoticeItem> itemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabNotice = (FloatingActionButton) findViewById(R.id.fabNotice);
        fabSeenInfo = (FloatingActionButton) findViewById(R.id.fabSeenInfo);
        fabMainPage2 = (FloatingActionButton) findViewById(R.id.fabMain2);
        fabLogin = (FloatingActionButton) findViewById(R.id.fabLogin);
        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

        userLocalStore = new UserLocalStore(this);
        itemData = new ArrayList<>();
        noticeGridAdapter = new NoticeGridAdapter(this, R.layout.notice_list_item, itemData);
        gridView.setAdapter(noticeGridAdapter);
        serverRequest = new ServerRequest(this);

        gridView.setOnItemClickListener(this);
        fabNotice.setOnClickListener(this);
        fabSeenInfo.setOnClickListener(this);
        fabMainPage2.setOnClickListener(this);
        fabLogin.setOnClickListener(this);
        floatingActionMenu.setClosedOnTouchOutside(true);

        progressBar.setVisibility(View.VISIBLE);
        serverRequest.fetchNoticeItemGridInBG(0, new GetItemCallback() {
            @Override
            public void done(ArrayList<NoticeItem> item) {
                progressBar.setVisibility(View.GONE);
                noticeGridAdapter.setGridData(item);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userLocalStore.getLoggedInStatus()) {
            fabLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        floatingActionMenu.close(true);
        switch (v.getId()) {
            case R.id.fabNotice:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, LOGIN_NOTICE_ADD);
                } else {
                    intent = new Intent(this, NoticeAdd.class);
                    startActivityForResult(intent, NOTICE_ADD_GET_ID);
                }
                break;
            case R.id.fabSeenInfo:
                if (!userLocalStore.getLoggedInStatus()) {
                    intent = new Intent(this, Login.class);
                    startActivityForResult(intent, LOGIN_SEENINFO_ADD);
                } else {
                    intent = new Intent(this, SeenInfoAdd.class);
                    startActivityForResult(intent, SEENINFO_ADD_GET_ID);
                }
                break;
            case R.id.fabLogin:
                intent = new Intent(this, Login.class);
                startActivityForResult(intent, LOGIN_USER_MANAGEMENT);
                break;
            case R.id.fabMain2:
                intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_USER_MANAGEMENT) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, UserManagement.class);
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
                Intent intent = new Intent(this, NoticeManagement.class);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
