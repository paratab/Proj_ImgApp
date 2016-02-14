package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.CallBack.GetNoticeCallBack;
import bolona_pig.proj_imgapp.ObjectClass.DateTime;
import bolona_pig.proj_imgapp.ObjectClass.Notice;
import bolona_pig.proj_imgapp.ObjectClass.ServerRequest;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class NoticeDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvLnName, tvLnBirthDate, tvLnPlace, tvLnLostDate, tvLnDetail, tvLnAdder, tvLnPhone;
    Button btnEdtNotice;
    ServerRequest serverRequest;
    UserLocalStore userLocalStore;
    Notice recentNotice;
    DateTime dateTime;
    ImageView imageView;
    boolean imageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        tvLnName = (TextView) findViewById(R.id.tvLnName);
        tvLnBirthDate = (TextView) findViewById(R.id.tvLnBirthDate);
        tvLnPlace = (TextView) findViewById(R.id.tvLnPlace);
        tvLnLostDate = (TextView) findViewById(R.id.tvLnLostDate);
        tvLnDetail = (TextView) findViewById(R.id.tvLnDetail);
        tvLnAdder = (TextView) findViewById(R.id.tvLnAdder);
        tvLnPhone = (TextView) findViewById(R.id.tvLnPhone);
        btnEdtNotice = (Button) findViewById(R.id.btNoticeEdit);
        imageView = (ImageView) findViewById(R.id.imageView);
        serverRequest = new ServerRequest(this);
        userLocalStore = new UserLocalStore(this);
        dateTime = new DateTime(this);

        btnEdtNotice.setOnClickListener(this);
        imageChange = false;
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
        Toast.makeText(this, "ไม่สามารถดึงข้อมูลของประกาศได้", Toast.LENGTH_SHORT).show();
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

        if (!imageChange) Picasso.with(this).load(notice.imagePath).into(imageView);
        else
            Picasso.with(this).load(recentNotice.imagePath).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);

        imageChange = false;

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
                Intent intent = new Intent(this, NoticeDetailEdit.class);
                intent.putExtra("notice", recentNotice);
                startActivityForResult(intent, 111);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            imageChange = data.getExtras().getBoolean("imageChange");
        }
    }
}
