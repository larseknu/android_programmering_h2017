package no.hiof.larseknu.playingwithservices.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import no.hiof.larseknu.playingwithservices.Worker;

public class MyStartedService extends Service {
    private static final String LOGTAG = "MyStartedService";

    private Worker worker;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onCreate() {
        Log.i(LOGTAG, "MyStartedService.onCreate Thread: " + Thread.currentThread().getName());
        worker = new Worker(this);
        worker.monitorGpsInBackground();
        executorService = Executors.newSingleThreadExecutor();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGTAG, "MyStartedService.onStartCommand Thread: " + Thread.currentThread().getName());

        ServiceRunnable runnable = new ServiceRunnable(this, startId);

        executorService.execute(runnable);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        worker.stopGpsMonitoring();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ServiceRunnable implements Runnable {
        MyStartedService myStartedService;
        int startId;

        public ServiceRunnable(MyStartedService myStartedService, int startId) {
            this.myStartedService = myStartedService;
            this.startId = startId;
        }

        @Override
        public void run() {
            Log.i(LOGTAG, "MyStartedService.ServiceRunnable Thread: " + Thread.currentThread().getName());
            try {
                Location location = worker.getLocation();

                String address = worker.reverseGeocode(location);

                JSONObject jsonObject = worker.getJSONObjectFromURL("");

                worker.saveToFile(location, address, jsonObject.getString("title"), "MyStartedService.out");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DelayedStopRequest stopRequest = new DelayedStopRequest(myStartedService, startId);

            myStartedService.scheduledExecutorService.schedule(stopRequest, 10, TimeUnit.SECONDS);
        }
    }

    class DelayedStopRequest implements Runnable {
        MyStartedService myStartedService;
        int startId;

        public DelayedStopRequest(MyStartedService myStartedService, int startId) {
            this.myStartedService = myStartedService;
            this.startId = startId;
        }

        @Override
        public void run() {
            Log.i(LOGTAG, "MyStartedService.DelayedStopRequest Thread: " + Thread.currentThread().getName());

            boolean stopping = myStartedService.stopSelfResult(startId);

            Log.i("LOGTAG", "Service with startid: " + startId + " stopping: " + stopping);
        }
    }
}
