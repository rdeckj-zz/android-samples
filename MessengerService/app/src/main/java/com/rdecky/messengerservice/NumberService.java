package com.rdecky.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class NumberService extends Service {

    static final int MSG_GET_NUMBER = 1;
    static final int MSG_GET_FIB = 2;
    static final int MSG_REGISTER_CLIENT = 3;
    static final int MSG_RESPONSE = 4;

    private static final String TAG = "*" + NumberService.class.getSimpleName();

    boolean allowRebinding = true;

    private static ArrayList<Messenger> clients = new ArrayList<>();
    final Messenger messenger = new Messenger(new IncomingMessageHandler());

    static class IncomingMessageHandler extends Handler {
        private final Random random = new Random();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_NUMBER:
                    sendUniqueNumberToClients();
                    break;
                case MSG_GET_FIB:
                    sendFibToClients();
                    break;
                case MSG_REGISTER_CLIENT:
                    clients.add(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

        private void sendUniqueNumberToClients() {
            for (int i = clients.size() - 1; i >= 0; i--) {
                int randomNumber = random.nextInt(100);
                try {
                    clients.get(i).send(Message.obtain(null, MSG_RESPONSE, randomNumber, 0));
                } catch (RemoteException e) {
                    clients.remove(i);
                }
            }
        }

        private void sendFibToClients() {
            int fibNumber = calcFib(42);
            for (int i = clients.size() - 1; i >= 0; i--) {
                try {
                    clients.get(i).send(Message.obtain(null, MSG_RESPONSE, fibNumber, 0));
                } catch (RemoteException e) {
                    clients.remove(i);
                }
            }
        }

        int calcFib(int number) {
            if (number <= 1) {
                return number;
            }
            return calcFib(number - 2) + calcFib(number - 1);
        }
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind()");
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind()");
        return allowRebinding;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "onRebind()");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
    }

    @Override
    public void onTrimMemory(int level) {
        Log.v(TAG, "onTrimMemory()");
    }
}
