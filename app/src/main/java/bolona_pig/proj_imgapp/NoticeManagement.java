package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeManagement extends AppCompatActivity implements View.OnClickListener {

    TextView tvLnName, tvLnBirthDate, tvLnPlace, tvLnLostDate, tvLnDetail, tvLnAdder, tvLnPhone;
    Button btnEdtNotice;
    ServerRequest serverRequest;
    UserLocalStore userLocalStore;
    Notice recentNotice;
    DateTime dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_management);

        tvLnName = (TextView) findViewById(R.id.tvLnName);
        tvLnBirthDate = (TextView) findViewById(R.id.tvLnBirthDate);
        tvLnPlace = (TextView) findViewById(R.id.tvLnPlace);
        tvLnLostDate = (TextView) findViewById(R.id.tvLnLostDate);
        tvLnDetail = (TextView) findViewById(R.id.tvLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btnEdtNotice = (Button) findViewById(R.id.btNoticeEdit);
        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);

        btnEdtNotice.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int noticeId = Integer.parseInt(getIntent().getExtras().getString("noticeId"));
        serverRequest.fetchNoticeDataInBG(noticeId, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice) {
                if (returnNotice == null) {
                    printError();
                } else {
                    showNotice(returnNotice);
                }
            }
        });
    }

    public void printError() {
        Toast.makeText(this, "Unable to get Notice Data, Make sure you have internet connection", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void showNotice(Notice notice) {
        tvLnName.setText(notice.lnName);
        tvLnBirthDate.setText(dateTime.getAge(notice.lnBirthDate));
        tvLnPlace.setText(notice.lnPlace);
        tvLnLostDate.setText(notice.lnLostDate);
        tvLnDetail.setText(notice.lnDetail);
        tvLnAdder.setText(notice.lnAdder);
        tvLnPhone.setText(notice.lnPhone);
        recentNotice = notice;

        User user = userLocalStore.getLoggedInUser();
        if (user.name.equals(notice.lnAdder) && user.telephone.equals(notice.lnPhone)) {
            View v = findViewById(R.id.btNoticeEdit);
            v.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNoticeEdit:
                Intent intent = new Intent(this, NoticeEdit.class);
                intent.putExtra("noticeId", recentNotice.id + "");
                startActivity(intent);
                break;
        }
    }
}
