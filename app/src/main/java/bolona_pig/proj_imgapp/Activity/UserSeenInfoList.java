package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.ObjectClass.GridItem;
import bolona_pig.proj_imgapp.ObjectClass.SeenListAdapter;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class UserSeenInfoList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<GridItem> listItem = new ArrayList<>();
    ServerRequest serverRequest;
    SeenListAdapter seenListAdapter;
    UserLocalStore userLocalStore;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_seen_info_list);
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        seenListAdapter = new SeenListAdapter(this, listItem);
        listView.setAdapter(seenListAdapter);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();

        serverRequest.fetchUserSeenInfoListInBG(user, new GetItemCallback() {
            @Override
            public void done(ArrayList<GridItem> item) {
                if (item.size() > 0) {
                    listItem = item;
                    seenListAdapter.setListData(listItem);
                } else {
                    Toast.makeText(UserSeenInfoList.this, "ไม่สามารถดึงข้อมูลจากระบบได้ หรือ ไม่มีข้อมูลการแจ้งเบอะแสในระบบ", Toast.LENGTH_SHORT).show();
                    finish();
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
        Intent intent = new Intent(this, SeenInfoDetail.class);
        intent.putExtra("seenId", item.id + "");
        startActivity(intent);
    }
}