package com.fxlc.trucker.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.R;


public class AboutusActivity extends BaseActivity implements View.OnClickListener {


    TextView versionTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        versionTx = (TextView) findViewById(R.id.version);

        versionTx.setText(getVersion());
        findViewById(R.id.call).setOnClickListener(this);
        TextView call = (TextView) findViewById(R.id.call);
        call.setText(Html.fromHtml("<font color = '#00afff'>400-0351-132</font>"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("关于我们");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-0351-132"));

                startActivity(intent);
                break;

        }
    }
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "版本号 " + version;
        } catch (Exception e) {
                e.printStackTrace();
        }
        return  "";
    }
}
