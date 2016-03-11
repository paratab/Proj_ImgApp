package bolona_pig.proj_imgapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.ObjectClass.GridItem;
import bolona_pig.proj_imgapp.ObjectClass.NoticeListAdapter;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class UserNoticeList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<GridItem> listItem = new ArrayList<>();
    ServerRequest serverRequest;
    NoticeListAdapter noticeListAdapter;
    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notice_list);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        noticeListAdapter = new NoticeListAdapter(this, listItem);
        listView.setAdapter(noticeListAdapter);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        serverRequest.fetchUserNoticeListInBG(user, new GetItemCallback() {
            @Override
            public void done(ArrayList<GridItem> item, String resultStr) {
                if (item.size() > 0) {
                    listItem = item;
                    noticeListAdapter.setListData(listItem);
                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserNoticeList.this);
                    dialogBuilder.setTitle("ข้อผิดพลาด");
                    dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    });
                    dialogBuilder.setMessage(resultStr);
                    dialogBuilder.setNegativeButton("ออก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dialogBuilder.show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GridItem item = (GridItem) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, NoticeDetail.class);
        intent.putExtra("noticeId", item.id + "");
        startActivity(intent);
    }
}
