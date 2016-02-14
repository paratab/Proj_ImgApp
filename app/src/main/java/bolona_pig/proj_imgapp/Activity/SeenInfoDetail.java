package bolona_pig.proj_imgapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.R;

public class SeenInfoDetail extends AppCompatActivity {

    TextView tvSeenDate, tvSeenPlace, tvSeenDetail, tvSeenAdder, tvSeenPhone;
    ServerRequest serverRequest;
    SeenInfo seenInfo;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeninfo_detail);

        tvSeenDate = (TextView) findViewById(R.id.tvSeenDate);
        tvSeenPlace = (TextView) findViewById(R.id.tvSeenPlace);
        tvSeenDetail = (TextView) findViewById(R.id.tvSeenDetail);
        tvSeenAdder = (TextView) findViewById(R.id.tvSeenAdder);
        tvSeenPhone = (TextView) findViewById(R.id.tvSeenPhone);
        imageView = (ImageView) findViewById(R.id.imageView);

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
        Picasso.with(this).load(info.imagePath).into(imageView);
        seenInfo = info;
    }

    private void showError() {
        Toast.makeText(this, "ไม่สามารถดึงข้อมูลเบอะแสจากระบบ", Toast.LENGTH_SHORT).show();
        finish();
    }
}
