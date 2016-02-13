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
import bolona_pig.proj_imgapp.ObjectClass.GridItem;
import bolona_pig.proj_imgapp.ObjectClass.HttpRequest;
import bolona_pig.proj_imgapp.ObjectClass.NoticeAdapter;
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
    ServerRequest serverRequest;
    FloatingActionMenu floatingActionMenu;
    HttpRequest httpRequest;
    GridView gridView;
    ProgressBar progressBar;
    NoticeAdapter noticeAdapter;
    private ArrayList<GridItem> itemData = new ArrayList<>();
    private String FEED_URL = "http://www.surawit-sj.xyz/FetchNoticeItemGrid.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabNotice = (FloatingActionButton) findViewById(R.id.fabNotice);
        fabSeenInfo = (FloatingActionButton) findViewById(R.id.fabSeenInfo);
        fabMainPage2 = (FloatingActionButton) findViewById(R.id.fabMain2);
        fabLogin = (FloatingActionButton) findViewById(R.id.fabLogin);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);

        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        httpRequest = new HttpRequest();

        noticeAdapter = new NoticeAdapter(this, itemData);
        gridView.setAdapter(noticeAdapter);

        fabNotice.setOnClickListener(this);
        fabSeenInfo.setOnClickListener(this);
        fabMainPage2.setOnClickListener(this);
        fabLogin.setOnClickListener(this);
        floatingActionMenu.setClosedOnTouchOutside(true);

        serverRequest.fetchNoticeItemGridInBG(0, new GetItemCallback() {
            @Override
            public void done(ArrayList<GridItem> item) {
                if (item.size() > 0) {
                    itemData = item;
                    noticeAdapter.setGridData(itemData);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);

        gridView.setOnItemClickListener(this);
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
        GridItem item = (GridItem) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, NoticeManagement.class);
        intent.putExtra("noticeId", item.id + "");
        startActivity(intent);
    }
}
