package bolona_pig.proj_imgapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeEdit extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    Button btNoticeUpdate;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    Notice recentNotice;
    DateTime dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);

        edtLnName = (EditText) findViewById(R.id.edtLnName);
        edtLnBirthDate = (EditText) findViewById(R.id.edtLnBirthDate);
        edtLnPlace = (EditText) findViewById(R.id.edtLnPlace);
        edtLnLostDate = (EditText) findViewById(R.id.edtLnLostDate);
        edtLnDetail = (EditText) findViewById(R.id.edtLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btNoticeUpdate = (Button) findViewById(R.id.btNoticeUpdate);

        btNoticeUpdate.setOnClickListener(this);

        edtLnBirthDate.setOnClickListener(this);
        edtLnBirthDate.setOnFocusChangeListener(this);

        edtLnLostDate.setOnClickListener(this);
        edtLnLostDate.setOnFocusChangeListener(this);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        dateTime = new DateTime(this);
    }

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
        recentNotice = notice;
        edtLnName.setText(notice.lnName);
        edtLnBirthDate.setText(notice.lnBirthDate);
        edtLnPlace.setText(notice.lnPlace);
        edtLnLostDate.setText(notice.lnLostDate);
        edtLnDetail.setText(notice.lnDetail);
        tvLnAdder.setText(notice.lnAdder);
        tvLnPhone.setText(notice.lnPhone);

        User user = userLocalStore.getLoggedInUser();
        if (!user.name.equals(notice.lnAdder)) {
            View v = findViewById(R.id.btNoticeEdit);
            v.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNoticeUpdate:
                upDateNoticeProcess();
                break;
            case R.id.edtLnBirthDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtLnBirthDate.setText(date);
                    }
                });
                break;
            case R.id.edtLnLostDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtLnLostDate.setText(date);
                    }
                });
        }
    }

    private void upDateNoticeProcess() {
        User user = userLocalStore.getLoggedInUser();

        int id = recentNotice.id;
        String lnName = edtLnName.getText().toString();
        String lnBirthDate = edtLnBirthDate.getText().toString();
        String lnPlace = edtLnPlace.getText().toString();
        String lnDate = edtLnLostDate.getText().toString();
        String lnDetail = edtLnDetail.getText().toString();
        String lnAdder = user.username;
        String lnPhone = user.telephone;

        Notice notice = new Notice(id, lnName, lnBirthDate, lnPlace, lnDate, lnDetail, lnAdder, lnPhone);
        serverRequest.updateNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice) {
                if (returnNotice == null) {
                    printError();
                } else {
                    showResult();
                }
            }
        });
    }

    public void showResult() {
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.btNoticeUpdate:
                    upDateNoticeProcess();
                    break;
                case R.id.edtLnBirthDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtLnBirthDate.setText(date);
                        }
                    });
                    break;
                case R.id.edtLnLostDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtLnLostDate.setText(date);
                        }
                    });
            }
        }

    }
}
