package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetSeenInfoCallback;
import bolona_pig.proj_imgapp.ObjectClass.SeenInfo;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class SeenInfoDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvSeenDate, tvSeenPlace, tvSeenDetail, tvSeenAdder, tvSeenPhone;
    ServerRequest serverRequest;
    SeenInfo seenInfo;
    ImageView imageView;
    ImageButton imbTel, imbMessage;
    LinearLayout gridUser;
    UserLocalStore userLocalStore;

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
        imbTel = (ImageButton) findViewById(R.id.imbTelephone);
        imbMessage = (ImageButton) findViewById(R.id.imbMessage);
        gridUser = (LinearLayout) findViewById(R.id.gridUser);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        imbTel.setOnClickListener(this);
        imbMessage.setOnClickListener(this);
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

        User user = userLocalStore.getLoggedInUser();
        if (!user.name.equals(info.seenAdder) || !user.telephone.equals(info.seenPhone)) {
            gridUser.setVisibility(View.VISIBLE);
        }
    }

    private void showError() {
        Toast.makeText(this, "ไม่สามารถดึงข้อมูลเบอะแสจากระบบ", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imbTelephone:
                String uri = "tel:" + tvSeenPhone.getText();
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.imbMessage:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", tvSeenPhone.getText());
                startActivity(intent);
                break;
        }
    }
}
