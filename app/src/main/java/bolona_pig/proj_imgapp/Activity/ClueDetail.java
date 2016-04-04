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

import bolona_pig.proj_imgapp.CallBack.GetClueCallback;
import bolona_pig.proj_imgapp.ObjectClass.Clue;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class ClueDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvClueDate, tvCluePlace, tvClueDetail, tvClueAdder, tvCluePhone, tvSex;
    ServerRequest serverRequest;
    Clue clueInfo;
    ImageView imageView;
    ImageButton imbTel, imbMessage, location;
    LinearLayout gridUser;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_detail);

        tvClueDate = (TextView) findViewById(R.id.tvClueDate);
        tvCluePlace = (TextView) findViewById(R.id.tvCluePlace);
        tvClueDetail = (TextView) findViewById(R.id.tvClueDetail);
        tvClueAdder = (TextView) findViewById(R.id.tvClueAdder);
        tvCluePhone = (TextView) findViewById(R.id.tvCluePhone);
        imageView = (ImageView) findViewById(R.id.imageView);
        imbTel = (ImageButton) findViewById(R.id.imbTelephone);
        imbMessage = (ImageButton) findViewById(R.id.imbMessage);
        gridUser = (LinearLayout) findViewById(R.id.gridUser);
        tvSex = (TextView) findViewById(R.id.tvSex);
        location = (ImageButton) findViewById(R.id.location);

        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        imbTel.setOnClickListener(this);
        imbMessage.setOnClickListener(this);
        location.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int infoId = Integer.parseInt(getIntent().getExtras().getString("clueId"));
        serverRequest.fetchClueDataInBG(infoId, new GetClueCallback() {
            @Override
            public void done(Clue returnInfo, String resultStr) {
                if (returnInfo == null) {
                    Toast.makeText(ClueDetail.this, resultStr, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showClueInfo(returnInfo);
                }
            }
        });
    }

    private void showClueInfo(Clue info) {
        tvClueDate.setText(info.seenDate);
        tvCluePlace.setText(info.seenPlace);
        tvClueDetail.setText(info.detail);
        tvClueAdder.setText(info.adderName);
        tvCluePhone.setText(info.telephone);
        tvSex.setText(info.sex);

        Picasso.with(this).load(info.imagePath).into(imageView);
        clueInfo = info;

        User user = userLocalStore.getLoggedInUser();
        if (!user.username.equals(clueInfo.adderUsername)) {
            gridUser.setVisibility(View.VISIBLE);
        }

        String temp = info.seenPlace;
        if (temp.startsWith("[Lat/Lng]")) {
            location.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imbTelephone:
                String uri = "tel:" + tvCluePhone.getText();
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
                break;
            case R.id.imbMessage:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.putExtra("address", tvCluePhone.getText());
                startActivity(intent);
                break;
            case R.id.location:
                intent = new Intent(this, Maps2Activity.class);
                String temp = tvCluePlace.getText().toString();
                intent.putExtra("latlng", temp);
                startActivity(intent);
                break;
        }
    }
}
