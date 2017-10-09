package no.hiof.larseknu.playingwithservices.services;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONException;
import org.json.JSONObject;

import no.hiof.larseknu.playingwithservices.Worker;

/**
 * Created by larseknu on 09/10/2017.
 */

public class MyScheduledService extends JobService {
    private static String LOGTAG = "ScheduleService";

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(LOGTAG, "ScheduleService Started");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Worker worker = new Worker(getApplicationContext());
                    Log.d(LOGTAG, "Worker Started");

                    Location location = worker.getLocation();
                    Log.d(LOGTAG, "Got location");

                    String address = worker.reverseGeocode(location);
                    Log.d(LOGTAG, "Got address");

                    JSONObject json = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
                    Log.d(LOGTAG, "Got JSON");

                    worker.saveToFile(location, address, json.getString("title"), "ScheduleServiceJob.txt");
                    Log.d(LOGTAG, "Saved file");

                    Log.d(LOGTAG, "ScheduleService Done");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
