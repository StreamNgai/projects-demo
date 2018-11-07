package dev.ngai.fantastic.business.appabout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import dev.ngai.fantastic.BaseActivity;
import dev.ngai.fantastic.R;

public class AppAboutsActivity extends BaseActivity {

    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_abouts);
        versionText = (TextView) findViewById(R.id.versionText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("应用基本信息");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            String vN = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            int vC = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionText.setText("Version " + vN + "(" + vC + ")");
        } catch (Exception e) {

        }

    }
}
