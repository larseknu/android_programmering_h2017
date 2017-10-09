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

import no.hiof.larseknu.playingwithservices.Worker;

public class MyStartedService extends Service {
    private static final String LOGTAG = MyStartedService.class.getSimpleName();
    private ResultReceiver resultReceiver;

    @Override
    public void onCreate() {
        Log.i(LOGTAG, "Service Created + Thread:" + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGTAG, "Service Started + Thread:" + Thread.currentThread().getName());

        resultReceiver = intent.getParcelableExtra("receiver");

        new MyAsyncTask().execute();

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOGTAG, "Service OnBind ran + Thread:" + Thread.currentThread().getName());

        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(LOGTAG, "Service Destroyed + Thread:" + Thread.currentThread().getName());

        super.onDestroy();
    }

    class MyAsyncTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Worker worker = new Worker(getApplicationContext());
                publishProgress("Worker Started");
                Log.i(LOGTAG, "Worker Started + Thread:" + Thread.currentThread().getName());

                Location location = worker.getLocation();
                publishProgress("Got location");

                String address = worker.reverseGeocode(location);
                publishProgress("Got address");

                JSONObject json = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
                publishProgress("Got JSON");

                worker.saveToFile(location, address, json.getString("title"), "ScheduleServiceJob.txt");
                publishProgress("Saved file");

                publishProgress("MyService Done");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(MyStartedService.this, values[0], Toast.LENGTH_LONG).show();

            Log.i(LOGTAG, values[0] + " + Thread:" + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(LOGTAG, "MyService PostExecute + Thread:" + Thread.currentThread().getName());

            Bundle bundle = new Bundle();
            bundle.putString("resultStartedService", "Job Done");
            resultReceiver.send(1, bundle);

            stopSelf();
        }
    }
}
