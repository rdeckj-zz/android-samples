package com.rdecky.messengerservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "*" + MainActivity.class.getSimpleName();

    private Context context;
    private boolean numberServiceBound;
    Messenger numberService;
    private static TextView displayArea;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            numberService = new Messenger(service);
            numberServiceBound = true;

            try {
                Message msg = Message.obtain(null, NumberService.MSG_REGISTER_CLIENT);
                msg.replyTo = messenger;
                numberService.send(msg);

            } catch (RemoteException e) {
                //The service isn't there
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(TAG, "onServiceDisconnected");
            numberServiceBound = false;
        }
    };

    static class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NumberService.MSG_RESPONSE) {
                displayArea.append(msg.arg1 + "\n");
            } else {
                super.handleMessage(msg);
            }
        }
    }

    final Messenger messenger = new Messenger(new IncomingMessageHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        Button bindService = findViewById(R.id.bindService);
        setBindServiceListener(bindService);

        Button unbindService = findViewById(R.id.unbindService);
        setUnbindServiceListener(unbindService);

        Button getRandomNumber = findViewById(R.id.getRandomNumber);
        setRandomNumberListener(getRandomNumber);

        Button ANR = findViewById(R.id.anr);
        setANRListener(ANR);

        displayArea = findViewById(R.id.displayArea);

        Button clearDisplay = findViewById(R.id.clearDisplay);
        setClearDisplayListener(clearDisplay);
    }

    private void setANRListener(Button anr) {
        anr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberServiceBound) {
                    Message message = Message.obtain(null, NumberService.MSG_GET_FIB, 0, 0);
                    try {
                        numberService.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setClearDisplayListener(Button clearDisplay) {
        clearDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayArea.setText("");
            }
        });
    }

    private void setRandomNumberListener(Button getRandomNumber) {
        getRandomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberServiceBound) {
                    Message message = Message.obtain(null, NumberService.MSG_GET_NUMBER, 0, 0);
                    try {
                        numberService.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setBindServiceListener(final Button bindService) {
        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NumberService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    private void setUnbindServiceListener(final Button unbindService) {
        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(connection);
                numberServiceBound = false;
            }
        });
    }
}
