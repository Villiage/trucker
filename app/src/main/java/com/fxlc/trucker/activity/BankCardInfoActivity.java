package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.bean.BankCards;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.SimpleCallback;
import com.fxlc.trucker.util.DisplayUtil;

import retrofit2.Call;
import retrofit2.Retrofit;

public class BankCardInfoActivity extends BaseActivity implements View.OnClickListener {
    BankCards.BankCard card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_info);
        card = (BankCards.BankCard) getIntent().getSerializableExtra("card");
        findView();
        setValue();
        findViewById(R.id.del).setOnClickListener(this);
        initDialog();
    }
    TextView bankNameTx, cardNoTx;
    private void findView(){
        bankNameTx = (TextView) findViewById(R.id.bankname);
        cardNoTx = (TextView) findViewById(R.id.cardno);


    }
    private void setValue(){
        bankNameTx.setText(card.getBankType());
        cardNoTx.setText(card.getBankNo());

    }
    @Override
    protected void onResume() {
        super.onResume();
        title("银行卡");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.del:

                payDialog.show();
                break;

        }
    }

    private void verifyPwd() {

        UserService service = MyApplication.getRetrofit().create(UserService.class);
        String pwd = sb.toString();
        Call call = service.verifypwd(pwd);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                if (result.isSuccess()) {
                    del();
                }
            }
        });

        sb = new StringBuffer();
        for (TextView textView : pswTxtArr) {
            textView.getEditableText().clear();
        }
    }

    TextView[] pswTxtArr = new TextView[6];
    StringBuffer sb = new StringBuffer();
    Dialog payDialog;
    private void initDialog() {
        final String[] nums = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", ""};
        payDialog = new Dialog(ctx, R.style.dialog_alert);
        payDialog.setContentView(R.layout.dialog_pay);

        ViewGroup group = (ViewGroup) payDialog.findViewById(R.id.pwdGroup);
        for (int i = 0; i < pswTxtArr.length; i++) {
            pswTxtArr[i] = (TextView) group.getChildAt(i * 2);
        }
        GridView grid = (GridView) payDialog.findViewById(R.id.number);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = nums[i];
                if (i == nums.length - 1) {
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        pswTxtArr[sb.length()].getEditableText().clear();
                    }
                } else if (i != 9) {
                    sb.append(s);
                    pswTxtArr[sb.length() - 1].setText(s);
                    if (sb.length() == 6) {
                        payDialog.dismiss();
                        verifyPwd();
                    }
                }


            }
        });
        grid.setAdapter(new NumberAdapter(nums));
        Window win = payDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    class NumberAdapter extends BaseAdapter {
        String[] nums;
        int pading;
        View v;

        public NumberAdapter(String[] nums) {
            this.nums = nums;
            pading = DisplayUtil.dp2px(ctx, 15);
        }

        @Override
        public int getCount() {
            return nums.length;
        }

        @Override
        public String getItem(int i) {
            return nums[i];
        }

        @Override
        public long getItemId(int i) {

            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (i == nums.length - 1) {
                int h =  v.getMeasuredHeight();
                ImageView img = new ImageView(view.getContext());
                img.setImageResource(R.mipmap.nine_del);
                img.setLayoutParams(new AbsListView.LayoutParams(-1,h));
                img.setScaleType(ImageView.ScaleType.CENTER );
                return img;
            } else {
                TextView text = new TextView(viewGroup.getContext());
                text.setGravity(Gravity.CENTER);

                text.setPadding(0, pading, 0, pading);
                text.setTextColor(getResources().getColor(R.color.text_primary));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                text.setText(nums[i]);
                if (i != 9) text.setBackgroundResource(R.drawable.bg_click);
                v = view;
                return text;
            }



        }
    }

  private void del(){
      proDialog.show();
      Retrofit retrofit = MyApplication.getRetrofit();
      AccountService service =  retrofit.create(AccountService.class);

      Call<HttpResult>  call = service.delBank(card.getId());
      call.enqueue(new SimpleCallback() {
          @Override
          public void onSuccess(HttpResult result) {
              proDialog.dismiss();
              toast("删除成功");
              finish();
          }
      });

  }

}
