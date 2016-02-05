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
import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;


public class SeenInfoAdd extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    Button btSeenAdd;
    EditText edtSeenDate, edtSeenPlace, edtSeenDetail;
    TextView tvSeenAdder, tvSeenPhone;
    UserLocalStore userLocalStore;
    DateTime dateTime;
    User user;
    ServerRequest serverRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeninfo_add);

        edtSeenDate = (EditText) findViewById(R.id.edtSeenDate);
        edtSeenPlace = (EditText) findViewById(R.id.edtSeenPlace);
        edtSeenDetail = (EditText) findViewById(R.id.edtSeenDetail);
        tvSeenAdder = (TextView) findViewById(R.id.tvSeenAdder);
        tvSeenPhone = (TextView) findViewById(R.id.tvSeenPhone);
        btSeenAdd = (Button) findViewById(R.id.btSeenAdd);

        edtSeenDate.setOnClickListener(this);
        edtSeenDate.setOnFocusChangeListener(this);
        btSeenAdd.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);
        user = userLocalStore.getLoggedInUser();
        serverRequest = new ServerRequest(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        tvSeenAdder.setText(user.name);
        tvSeenPhone.setText(user.telephone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtSeenDate:
                dateTime.showDatePickup(new GetDateCallback() {
                    @Override
                    public void done(String date) {
                        edtSeenDate.setText(date);
                    }
                });
                break;
            case R.id.btSeenAdd:
                seenInfoAdd();
                break;
        }
    }

    private void seenInfoAdd() {
        String seenDate = edtSeenDate.getText().toString();
        String seenPlace = edtSeenPlace.getText().toString();
        String seenDetail = edtSeenDetail.getText().toString();
        String seenAdder = user.username;
        String seenPhone = user.telephone;

        SeenInfo info = new SeenInfo(-1, seenDate, seenPlace, seenDetail, seenAdder, seenPhone);
        serverRequest.storeSeenInfoDataInBG(info, new GetSeenInfoCallback() {
            @Override
            public void done(SeenInfo returnInfo) {
                if (returnInfo == null) {
                    showError();
                } else {
                    showResult(returnInfo);
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.edtSeenDate:
                    dateTime.showDatePickup(new GetDateCallback() {
                        @Override
                        public void done(String date) {
                            edtSeenDate.setText(date);
                        }
                    });
                    break;
            }
        }
    }

    public void showError() {
        Toast.makeText(this, "Added Error, 3G not Working os Same Notice Detail", Toast.LENGTH_SHORT).show();
    }

    public void showResult(SeenInfo info) {
        Intent intent = new Intent();
        intent.putExtra("seenId", info.seenId + "");
        setResult(RESULT_OK, intent);
        finish();
    }
}
