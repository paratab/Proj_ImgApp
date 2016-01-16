package bolona_pig.proj_imgapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btUserManagement, btGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        btUserManagement = (Button) findViewById(R.id.btUserManagement);
        btGoogle = (Button) findViewById(R.id.btGoogle);

        btUserManagement.setOnClickListener(this);
        btGoogle.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btUserManagement:
                intent = new Intent(this, UserManagement.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btGoogle:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
        }
    }

}
