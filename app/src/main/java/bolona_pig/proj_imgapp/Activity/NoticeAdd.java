package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetDateCallback;
import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    Button btAddNotice;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;
    DateTime dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);

        edtLnName = (EditText) findViewById(R.id.edtLnName);
        edtLnBirthDate = (EditText) findViewById(R.id.edtLnBirthDate);
        edtLnPlace = (EditText) findViewById(R.id.edtLnPlace);
        edtLnLostDate = (EditText) findViewById(R.id.edtLnLostDate);
        edtLnDetail = (EditText) findViewById(R.id.edtLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btAddNotice = (Button) findViewById(R.id.btNoticeAdd);

        btAddNotice.setOnClickListener(this);

        edtLnBirthDate.setOnFocusChangeListener(this);
        edtLnBirthDate.setOnClickListener(this);

        edtLnLostDate.setOnFocusChangeListener(this);
        edtLnLostDate.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
        dateTime = new DateTime(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        tvLnAdder.setText(user.name);
        tvLnPhone.setText(user.telephone);
        edtLnLostDate.setText(dateTime.getCurrentDate());

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btNoticeAdd:
                noticeAddMethod();
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

    private void noticeAddMethod() {
        User user = userLocalStore.getLoggedInUser();

        String lnName = edtLnName.getText().toString();
        String lnBirthDate = edtLnBirthDate.getText().toString();
        String lnPlace = edtLnPlace.getText().toString();
        String lnDate = edtLnLostDate.getText().toString();
        String lnDetail = edtLnDetail.getText().toString();
        String lnAdder = user.username;
        String lnPhone = user.telephone;

        Notice notice = new Notice(lnName, lnBirthDate, lnPlace, lnDate, lnDetail, lnAdder, lnPhone);
        serverRequest.storeNoticeDataInBG(notice, new GetNoticeCallBack() {
            @Override
            public void done(Notice returnNotice) {
                if (returnNotice == null) {
                    showError();
                } else {
                    showResult(returnNotice);
                }
            }
        });
    }

    public void showError() {
        Toast.makeText(this, "Added Error, 3G not Working os Same Notice Detail", Toast.LENGTH_SHORT).show();
    }

    public void showResult(Notice notice) {
        Intent intent = new Intent();
        intent.putExtra("ID", notice.id + "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
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
                    break;
            }
        }
    }
}
