package com.example.mymessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WangPeng on 2018/6/22.
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyConstants.CLIENT:
                    Log.i(TAG, "handleMessage: "+msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null,MyConstants.SERVICE);
                    Bundle args = new Bundle();
                    args.putString("reply","你的消息已经收到,稍后回复你");
                    replyMessage.setData(args);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                        super.handleMessage(msg);
            }
            super.handleMessage(msg);
        }
    }
    private final Messenger messenger = new Messenger(new MyHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
