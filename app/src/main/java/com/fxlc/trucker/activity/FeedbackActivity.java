package com.fxlc.trucker.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.SettingService;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;

import retrofit2.Call;
import retrofit2.Retrofit;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    TextView contentTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.action).setOnClickListener(this);
        contentTx = (TextView) findViewById(R.id.content);

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("意见反馈");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.action:


                feedBack();
                break;

        }
    }

    private void feedBack() {
        String feed = contentTx.getText().toString().trim();
        Retrofit retrofit = MyApplication.getRetrofit();

        SettingService service = retrofit.create(SettingService.class);

        Call<HttpResult> call = service.feedback(feed);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                toast("感谢您的反馈!");
                finish();
            }
        });


    }
}
