package no.hiof.larseknu.playingwithservices.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import no.hiof.larseknu.playingwithservices.Worker;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private static final String LOGTAG = MyIntentService.class.getSimpleName();

    private static final String ACTION_RETREIVE_AND_SAVE_ADDRESS = "no.hiof.larseknu.playingwithservices.action.FOO";

    private static final String EXTRA_FILENAME = "no.hiof.larseknu.playingwithservices.extra.FILENAME";
    private static final String EXTRA_RESULT_RECEIVER = "no.hiof.larseknu.playingwithservices.extra.RESULT_RECEIVER";

    private ResultReceiver resultReceiver;


    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionRetreiveAndSaveAddress(Context context, String fileName, ResultReceiver resultReceiver) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_RETREIVE_AND_SAVE_ADDRESS);
        intent.putExtra(EXTRA_FILENAME, fileName);
        intent.putExtra(EXTRA_RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOGTAG, "Intent received + Thread:" + Thread.currentThread().getName());

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RETREIVE_AND_SAVE_ADDRESS.equals(action)) {
                final String fileName = intent.getStringExtra(EXTRA_FILENAME);
                resultReceiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
                handleActionFoo(fileName);
            }
        }
    }

    private void handleActionFoo(String fileName) {
        try {
            Worker worker = new Worker(getApplicationContext());
            Log.d(LOGTAG, "Worker Started");

            Location location = worker.getLocation();
            Log.d(LOGTAG, "Got location");

            String address = worker.reverseGeocode(location);
            Log.d(LOGTAG, "Got address");

            JSONObject json = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
            Log.d(LOGTAG, "Got JSON");

            worker.saveToFile(location, address, json.getString("title"), fileName);
            Log.d(LOGTAG, "Saved file");

            Log.d(LOGTAG, "ScheduleService Done");

            Bundle bundle = new Bundle();
            bundle.putString("resultIntentService", "IntentService Done");

            resultReceiver.send(2, bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
