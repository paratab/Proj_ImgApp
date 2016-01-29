package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeAdd extends AppCompatActivity implements View.OnClickListener {

    EditText edtLnName, edtLnBirthDate, edtLnPlace, edtLnLostDate, edtLnDetail;
    TextView tvLnAdder, tvLnPhone;
    Button btAddNotice;
    UserLocalStore userLocalStore;
    ServerRequest serverRequest;

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
        userLocalStore = new UserLocalStore(this);
        serverRequest = new ServerRequest(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        tvLnAdder.setText(user.name);
        tvLnPhone.setText(user.telephone);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btNoticeAdd:

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
                                
                break;
        }
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
}