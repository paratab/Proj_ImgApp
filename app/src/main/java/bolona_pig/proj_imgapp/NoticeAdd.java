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

    EditText edtLpName, edtLpBirthDate, edtLpLocation, edtLpDate, edtLpDetail;
    TextView tvCtName, tvCtPhone;
    Button btAddNotice;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content__notice_add);

        edtLpName = (EditText) findViewById(R.id.edtLpName);
        edtLpBirthDate = (EditText) findViewById(R.id.edtLpBirthDate);
        edtLpLocation = (EditText) findViewById(R.id.edtLpLocation);
        edtLpDate = (EditText) findViewById(R.id.edtLpDate);
        edtLpDetail = (EditText) findViewById(R.id.edtLpDetail);
        tvCtName = (TextView) findViewById(R.id.tvCtName);
        tvCtPhone = (TextView) findViewById(R.id.tvCtPhone);
        btAddNotice = (Button) findViewById(R.id.btNoticeAdd);

        btAddNotice.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!userLocalStore.getLoggedInStatus()) {
            Intent intent = new Intent(this, Login.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
        } else {
            User user = userLocalStore.getLoggedInUser();
            tvCtName.setText(user.name);
            tvCtPhone.setText(user.telephone);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btNoticeAdd:
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
