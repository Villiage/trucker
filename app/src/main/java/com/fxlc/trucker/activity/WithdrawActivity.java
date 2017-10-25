package com.fxlc.trucker.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.trucker.BaseActivity;
import com.fxlc.trucker.MyApplication;
import com.fxlc.trucker.R;
import com.fxlc.trucker.api.AccountService;
import com.fxlc.trucker.api.UserService;
import com.fxlc.trucker.bean.BankCards;
import com.fxlc.trucker.bean.Withdraw;
import com.fxlc.trucker.bean.WithdrawInfo;
import com.fxlc.trucker.net.HttpCallback;
import com.fxlc.trucker.net.HttpResult;
import com.fxlc.trucker.net.MyThrowable;
import com.fxlc.trucker.net.SimpleCallback;
import com.fxlc.trucker.util.DisplayUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener {

    TextView bankNameTx, cardnoTx;
    TextView amountTx, yueTx;
    String yue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yue = getIntent().getStringExtra("yue");
        setContentView(R.layout.activity_withdraw);
        findViewById(R.id.card).setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);
        amountTx = (TextView) findViewById(R.id.amount);
        bankNameTx = (TextView) findViewById(R.id.bankname);
        cardnoTx = (TextView) findViewById(R.id.cardno);
        yueTx = (TextView) findViewById(R.id.yue);
        String s = "可用余额<font color = '#fe7208'>%s</font>元";
        yueTx.setText(Html.fromHtml(String.format(s, yue)));
//
        amountTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                findViewById(R.id.action).setEnabled(editable.length() > 0);
            }
        });

//        bankList = ((BankCards) getIntent().getSerializableExtra("bankcards")).getBankcard();\
        pwdDialog = new AlertDialog.Builder(this).create();
        initDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("提现");
        getMyCards();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.card:
                if (bankList != null && bankList.size() > 0) {
                    d.show();
                }
                break;
            case R.id.action:
                isPayPass();

                break;
            case R.id.cancel:
                d.dismiss();
                break;
            case R.id.add:
                d.dismiss();
                it.setClass(ctx, AddBankcardActivity.class);
                startActivity(it);

                break;


        }
    }

    private Withdraw withdraw;

    private void withDraw() {

        AccountService service = MyApplication.getRetrofit().create(AccountService.class);

        Map<String, String> map = new HashMap<>();
        map.put("bankCardId", card.getId());
        map.put("money", amountTx.getText().toString());
        Call<HttpResult<Withdraw>> call = service.withdraw(map);
        call.enqueue(new HttpCallback<Withdraw>() {
            @Override
            public void onSuccess(Withdraw withdraw) {
                toast("提现申请成功");


                it.setClass(ctx, WithdrawInfoActivity.class);
                it.putExtra("withdraw", withdraw);
                startActivity(it);
                finish();

            }
        });

    }

    private List<BankCards.BankCard> bankList;
    private BankCards.BankCard card;

    public void getMyCards() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        AccountService service = retrofit.create(AccountService.class);
        final Call<HttpResult<BankCards>> call = service.listBankCard();
        call.enqueue(new HttpCallback<BankCards>() {
            @Override
            public void onSuccess(BankCards bankCards) {
                proDialog.dismiss();
                bankList = bankCards.getBankcard();
                card = bankList.get(0);
                bankNameTx.setText(card.getBankType());
                cardnoTx.setText("尾号" + card.getBankNo().substring(card.getBankNo().length() - 4));
                createDialog();

            }
        });
    }

    Dialog payDialog;
    AlertDialog pwdDialog;

    private void isPayPass() {

        UserService service = MyApplication.getRetrofit().create(UserService.class);

        Call<HttpResult> call = service.isPaypass();
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {

                payDialog.show();

            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable throwable) {
                super.onFailure(call, throwable);
                if (throwable instanceof MyThrowable) {
                    pwdDialog.setMessage("请先设置交易密码");
                    pwdDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    pwdDialog.setButton(AlertDialog.BUTTON_POSITIVE, "去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            it.setClass(ctx, ResetPayPwdActivity.class);
                            it.putExtra("title", "设置交易密码");
                            startActivity(it);
                        }
                    });
                    pwdDialog.setCanceledOnTouchOutside(false);
                    pwdDialog.show();
                }
            }
        });

    }


    Dialog d;

    private void createDialog() {

        d = new Dialog(ctx, R.style.dialog_alert);


        d.setContentView(R.layout.dialog_option);
        d.findViewById(R.id.cancel).setOnClickListener(this);
        d.findViewById(R.id.add).setOnClickListener(this);
        ListView listview = d.findViewById(R.id.list);
        listview.setAdapter(new MListAdapter());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d.dismiss();
                card = (BankCards.BankCard) adapterView.getItemAtPosition(i);
                bankNameTx.setText(card.getBankType());
                cardnoTx.setText("尾号" + card.getBankNo().substring(card.getBankNo().length() - 4));

            }
        });
        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);


    }

    private void verifyPwd() {

        UserService service = MyApplication.getRetrofit().create(UserService.class);
        String pwd = sb.toString();
        Call call = service.verifypwd(pwd);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                if (result.isSuccess()) {
                    withDraw();
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

    class MListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return bankList == null ? 0 : bankList.size();
        }

        @Override
        public BankCards.BankCard getItem(int i) {
            return bankList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView text;
            if (view == null) {

                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dialog_cardsel, null);
            }
            text = (TextView) view;
            String s = getItem(i).getBankNo().substring(getItem(i).getBankNo().length() - 4);
            String name = getItem(i).getName();
            text.setText( name + " " + getItem(i).getBankType() + "(" + s + ") " );
            return view;
        }
    }

}
