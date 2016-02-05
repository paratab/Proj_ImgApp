package bolona_pig.proj_imgapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.R;

public class SeenInfoDetail extends AppCompatActivity {

    TextView tvSeenDate, tvSeenPlace, tvSeenDetail, tvSeenAdder, tvSeenPhone;
    ServerRequest serverRequest;
    SeenInfo seenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeninfo_detail);

        tvSeenDate = (TextView) findViewById(R.id.tvSeenDate);
        tvSeenPlace = (TextView) findViewById(R.id.tvSeenPlace);
        tvSeenDetail = (TextView) findViewById(R.id.tvSeenDetail);
        tvSeenAdder = (TextView) findViewById(R.id.tvSeenAdder);
        tvSeenPhone = (TextView) findViewById(R.id.tvSeenPhone);

        serverRequest = new ServerRequest(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int infoId = Integer.parseInt(getIntent().getExtras().getString("seenId"));
        serverRequest.fetchSeenInfoDataInBG(infoId, new GetSeenInfoCallback() {
            @Override
            public void done(SeenInfo returnInfo) {
                if (returnInfo == null) {
                    showError();
                } else {
                    showSeenInfo(returnInfo);
                }
            }
        });
    }

    private void showSeenInfo(SeenInfo info) {
        tvSeenDate.setText(info.seenDate);
        tvSeenPlace.setText(info.seenPlace);
        tvSeenDetail.setText(info.seenDetail);
        tvSeenAdder.setText(info.seenAdder);
        tvSeenPhone.setText(info.seenPhone);
        seenInfo = info;
    }

    private void showError() {
        Toast.makeText(this, "Unable to get Seen Info Data, Make sure you have internet connection", Toast.LENGTH_SHORT).show();
        finish();
    }
}
