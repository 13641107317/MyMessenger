package com.example.mymessenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Messenger messenger;
    private Messenger replyMessenger = new Messenger(new MyReplyHanlder());

    private static class MyReplyHanlder extends Handler {
        private static final String TAG = MainActivity.class.getSimpleName();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.SERVICE:
                    Log.i(TAG, "handleMessage: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);

                    break;
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            Message msg = Message.obtain(null, MyConstants.CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello");
            msg.setData(bundle);
            msg.replyTo = replyMessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
