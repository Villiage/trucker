package com.fxlc.trucker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fxlc.trucker.activity.BillInfoActivity;
import com.fxlc.trucker.activity.MsgActivity;
import com.fxlc.trucker.activity.WithdrawInfoActivity;
import com.fxlc.trucker.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by cyd on 2017/9/18.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPUSH";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Logger.d(TAG, "[MyReceiver] 接收到推送下来的附加内容: " + extra);
//           打开自定义的Activity
            String type = "";
            String id = "";
            try {
                JSONObject obj = new JSONObject(extra);
                type = obj.getString("type");
                id = obj.getString("messageId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i = new Intent();
            if (type.equals("1")) {
                i.setClass(context, WithdrawInfoActivity.class);
                i.putExtra("id", id);
            } else if (type.equals("2")) {
                i.setClass(context, BillInfoActivity.class);
                i.putExtra("orderId", id);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);


        }
    }
}
