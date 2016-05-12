package bolona_pig.proj_imgapp.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bolona_pig.proj_imgapp.CallBack.GetItemCallback;
import bolona_pig.proj_imgapp.ObjectClass.GridItem;
import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.NoticeGridAdapter;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;
import bolona_pig.proj_imgapp.Service.GcmRegisterService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public final int LOGIN = 1;
    public final int LOGIN_NOTICE_ADD = 2;
    public final int LOGIN_CLUE_ADD = 3;
    public final int NOTICE_ADD_GET_ID = 4;
    public final int CLUE_ADD_GET_ID = 5;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    GridView gridView;
    ProgressBar progressBar;
    NoticeGridAdapter noticeGridAdapter;
    FloatingActionButton fabNotice, fabSeenInfo, fabMainPage2;
    FloatingActionMenu floatingActionMenu;
    NumberPicker minAge, maxAge;
    int minValue, maxValue;
    String sex;
    boolean isSexSelected = false, isCustomSearch = false;
    AlertDialog dialog;
    TextView customSearch;
    ArrayList<GridItem> arrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<GridItem> itemData = new ArrayList<>();
    private boolean isReceiverRegistered;
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.notice_list_title);

        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);

        noticeGridAdapter = new NoticeGridAdapter(this, itemData);
        gridView.setAdapter(noticeGridAdapter);

        progressBar.setVisibility(View.VISIBLE);

        customSearch = (TextView) findViewById(R.id.customSearchBar);

        fabNotice = (FloatingActionButton) findViewById(R.id.fabNotice);
        fabSeenInfo = (FloatingActionButton) findViewById(R.id.fabSeenInfo);
        fabMainPage2 = (FloatingActionButton) findViewById(R.id.fabMain2);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabNotice.setOnClickListener(this);
        fabSeenInfo.setOnClickListener(this);
        fabMainPage2.setOnClickListener(this);
        floatingActionMenu.setClosedOnTouchOutside(true);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeMain);

        //fabMainPage2.setVisibility(View.GONE);

        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (gridView == null || gridView.getChildCount() == 0) ?
                                0 : gridView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registerReceiver();

        if (checkPlayServices() && userLocalStore.getLoggedInStatus())
            registerGCM();
        Toast.makeText(this, "Mark", Toast.LENGTH_SHORT).show();

        loadNoticeList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (floatingActionMenu != null && floatingActionMenu.isOpened()) {
            floatingActionMenu.close(true);
        } else if (isCustomSearch) {
            //recreate();
            noticeGridAdapter.setGridData(arrayList);
            isCustomSearch = false;
            customSearch.setVisibility(View.GONE);
            getSupportActionBar().setTitle(R.string.notice_list_title);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            View dView = getLayoutInflater().inflate(R.layout.custom_search, null);
            minAge = (NumberPicker) dView.findViewById(R.id.min_age);
            minAge.setMinValue(0);
            minAge.setMaxValue(100);
            minAge.setWrapSelectorWheel(true);
            minAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    minValue = newVal;
                }
            });
            maxAge = (NumberPicker) dView.findViewById(R.id.max_age);
            maxAge.setMinValue(0);
            maxAge.setMaxValue(100);
            maxAge.setWrapSelectorWheel(true);
            maxAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    maxValue = newVal;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ตัวเลือกการค้นหา");
            builder.setView(dView);
            builder.setPositiveButton("ค้นหา", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isSexSelected = false;
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSexSelected) {
                        Toast.makeText(MainActivity.this, "กรุณาเลือกเพศ", Toast.LENGTH_SHORT).show();
                    } else if (sex.equals(getResources().getString(R.string.sexBoth)) && minValue == 0 && maxValue == 0) {
                        Toast.makeText(MainActivity.this, "กรุณาเลือกช่วงอายุ", Toast.LENGTH_SHORT).show();
                    } else if (maxValue < minValue) {
                        Toast.makeText(MainActivity.this, "ช่วงอายุไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "[" + sex + "," + minValue + "," + maxValue + "]", Toast.LENGTH_SHORT).show();
                        serverRequest.customSearchInBG(sex, minValue, maxValue, new GetItemCallback() {
                            @Override
                            public void done(ArrayList<GridItem> item, String resultStr) {
                                if (item.size() > 0) {
                                    itemData = item;
                                    getSupportActionBar().setTitle(R.string.custom_search_Bar);
                                    noticeGridAdapter.setGridData(itemData);
                                    String temp;
                                    if (minValue == 0 && maxValue == 0)
                                        temp = "ตัวเลือกการค้นหา เพศ : " + sex + ", ช่วงอายุ : ไม่ระบุ";
                                    else
                                        temp = "ตัวเลือกการค้นหา เพศ : " + sex + ", ช่วงอายุ : " + minValue + " - " + maxValue + " ปี";
                                    customSearch.setText(temp);
                                    customSearch.setVisibility(View.VISIBLE);
                                    isCustomSearch = true;
                                } else {
                                    MidModule midModule = new MidModule();
                                    midModule.showAlertDialog(resultStr, MainActivity.this);
                                }
                                minValue = 0;
                                maxValue = 0;
                            }
                        });
                        dialog.dismiss();
                        isSexSelected = false;
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    protected void loadNoticeList() {
        if (!isCustomSearch) {
            serverRequest.fetchNoticeItemGridInBG(0, new GetItemCallback() {
                @Override
                public void done(ArrayList<GridItem> item, String resultStr) {
                    if (item.size() > 0) {
                        itemData = item;
                        noticeGridAdapter.setGridData(itemData);
                        arrayList = itemData;
                    } else {
                        Toast.makeText(MainActivity.this, resultStr, Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        fabMainPage2.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setNavigationView();
    }

    protected void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        if (userLocalStore.getLoggedInStatus()) {
            navigationView.getMenu().findItem(R.id.nav_user_detail).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_user_notice).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_user_clue).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_user_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_user_login).setVisible(false);
            User user = userLocalStore.getLoggedInUser();
            TextView textView = (TextView) header.findViewById(R.id.nav_text1);
            textView.setText(user.name);
            textView = (TextView) header.findViewById(R.id.nav_text2);
            textView.setText("id:" + user.username);
            ImageView imageView = (ImageView) header.findViewById(R.id.imageView);
            Picasso.with(this).load(user.imagePath).fit().centerCrop().into(imageView);
        } else {
            navigationView.getMenu().findItem(R.id.nav_user_login).setVisible(true);
            ImageView imageView = (ImageView) header.findViewById(R.id.imageView);
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_user_login) {
            intent = new Intent(this, Login.class);
            startActivityForResult(intent, LOGIN);
        } else if (id == R.id.nav_user_logout) {
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            Toast.makeText(this, "ออกชื่อออกจากระบบเรียบร้อย", Toast.LENGTH_SHORT).show();
            recreate();
        } else if (id == R.id.nav_user_detail) {
            intent = new Intent(this, UserDetail.class);
            startActivity(intent);
        } else if (id == R.id.nav_user_notice) {
            intent = new Intent(this, UserNoticeList.class);
            startActivity(intent);
        } else if (id == R.id.nav_user_clue) {
            intent = new Intent(this, UserClueList.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GridItem item = (GridItem) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, NoticeDetail.class);
        intent.putExtra("noticeId", item.id + "");
        startActivity(intent);
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
                    startActivityForResult(intent, LOGIN_CLUE_ADD);
                } else {
                    intent = new Intent(this, ClueAdd.class);
                    startActivityForResult(intent, CLUE_ADD_GET_ID);
                }
                break;
            case R.id.fabMain2:
//                intent = new Intent(this, BackOffice.class);
//                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_NOTICE_ADD) {
            if (resultCode == RESULT_OK) {
                registerGCM();
                Intent intent = new Intent(this, NoticeAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == NOTICE_ADD_GET_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("noticeId");
                Intent intent = new Intent(this, NoticeDetail.class);
                intent.putExtra("noticeId", id);
                startActivity(intent);
            }
        } else if (requestCode == LOGIN_CLUE_ADD) {
            if (resultCode == RESULT_OK) {
                registerGCM();
                Intent intent = new Intent(this, ClueAdd.class);
                startActivity(intent);
            }
        } else if (requestCode == CLUE_ADD_GET_ID) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("clueId");
                Intent intent = new Intent(this, ClueDetail.class);
                intent.putExtra("clueId", id);
                intent.putExtra("menu", "null");
                startActivity(intent);
            }
        } else if (requestCode == LOGIN) {
            if (resultCode == RESULT_OK) {
                registerGCM();
            }
        }
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
            case R.id.sexBoth:
                if (checked)
                    sex = "ไม่ระบุ";
                break;
        }
    }

    private void registerGCM() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        loadNoticeList();
    }
}
