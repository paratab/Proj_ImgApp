package bolona_pig.proj_imgapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import bolona_pig.proj_imgapp.ObjectClass.MidModule;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class UserDetail extends AppCompatActivity implements View.OnClickListener {

    public final int USER_EDIT = 1;
    ImageButton btLogout, btEditData, btnNoticeList, btnSeenList;
    TextView edtUsername, edtID, edtPassword, edtTelephone, edtName, edtEmail;
    UserLocalStore userLocalStore;
    ImageView imageView;
    MidModule MidModule;
    Boolean imageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        btLogout = (ImageButton) findViewById(R.id.btLogout);
        btEditData = (ImageButton) findViewById(R.id.btEditData);
        edtUsername = (TextView) findViewById(R.id.edtUsername);
        edtPassword = (TextView) findViewById(R.id.edtPassword);
        edtName = (TextView) findViewById(R.id.edtName);
        edtID = (TextView) findViewById(R.id.edtID);
        edtEmail = (TextView) findViewById(R.id.edtEmail);
        edtTelephone = (TextView) findViewById(R.id.edtPhone);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnNoticeList = (ImageButton) findViewById(R.id.btNoticeList);
        btnSeenList = (ImageButton) findViewById(R.id.btSeenList);

        btLogout.setOnClickListener(this);
        btEditData.setOnClickListener(this);
        btnSeenList.setOnClickListener(this);
        btnNoticeList.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        MidModule = new MidModule();
        imageChange = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = userLocalStore.getLoggedInUser();
        edtUsername.setText(user.username);
        edtName.setText(user.name);
        edtID.setText(user.nationId);
        edtEmail.setText(user.email);
        edtTelephone.setText(user.telephone);
        if (!imageChange) Picasso.with(this).load(user.imagePath).into(imageView);
        else
            Picasso.with(this).load(user.imagePath).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
        imageChange = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_EDIT && resultCode == RESULT_OK && data != null) {
            imageChange = data.getExtras().getBoolean("imageChange");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                finish();
                break;

            case R.id.btEditData:
                intent = new Intent(this, UserDetailEdit.class);
                startActivityForResult(intent, USER_EDIT);
                break;
            case R.id.btNoticeList:
                startActivity(new Intent(this, UserNoticeList.class));
                break;
            case R.id.btSeenList:
                startActivity(new Intent(this, UserClueList.class));
                break;
        }
    }

}
