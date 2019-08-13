package com.rdecky.foregroundservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        final Button startService = findViewById(R.id.startService);
        setStartServiceListener(startService);

        Button bindService = findViewById(R.id.bindService);
        Button unbindService = findViewById(R.id.unbindService);

        final Button stopService = findViewById(R.id.stopService);
        setStopServiceListener(stopService);
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
}
