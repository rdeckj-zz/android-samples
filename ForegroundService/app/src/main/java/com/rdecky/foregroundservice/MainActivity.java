package com.rdecky.foregroundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "*" + MainActivity.class.getSimpleName();

    private Context context;
    private boolean golfServiceBound;
    GolfService golfService;
    private TextView displayArea;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG, "onServiceConnected");
            GolfService.GolfBinder binder = (GolfService.GolfBinder) service;
            golfService = binder.getService();
            golfServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.v(TAG, "onServiceDisconnected");
            golfServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        Button startService = findViewById(R.id.startService);
        setStartServiceListener(startService);

        Button bindService = findViewById(R.id.bindService);
        setBindServiceListener(bindService);

        Button unbindService = findViewById(R.id.unbindService);
        setUnbindServiceListener(unbindService);

        Button stopService = findViewById(R.id.stopService);
        setStopServiceListener(stopService);

        Button getGolfClub = findViewById(R.id.getGolfClub);
        setGetGolfClubListener(getGolfClub);

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
                if(golfServiceBound) {
                    displayArea.append("Long calculation: " + golfService.calcFib(50) + "\n");
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

    private void setGetGolfClubListener(Button getGolfClub) {
        getGolfClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(golfServiceBound) {
                    displayArea.append(golfService.getGolfClub() + "\n");
                }
            }
        });
    }

    private void setStopServiceListener(Button stopService) {
        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GolfService.class);
                stopService(intent);
            }
        });
    }

    private void setStartServiceListener(Button startService) {
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GolfService.class);
                startService(intent);
            }
        });
    }

    private void setBindServiceListener(final Button bindService) {
        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GolfService.class);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    private void setUnbindServiceListener(final Button unbindService) {
        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(connection);
                golfServiceBound = false;
            }
        });
    }
}
