package com.example.larseknu.playingwiththreads;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class WithoutPermissionsActivity extends AppCompatActivity {

    private TextView statusText;
    private String tempText;
    private Thread workerThread;
    private AsyncTaskWorker asyncTaskWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        getSupportActionBar().setTitle("WithoutPermissions");

        statusText = findViewById(R.id.status_text);
        statusText.setText("Ready");

        //StrictMode.enableDefaults();
    }

    public void doAsyncWork(View view) {
        asyncTaskWorker = new AsyncTaskWorker();
        asyncTaskWorker.execute(statusText);
    }


    public void doWork(View view) {
        workerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Worker worker = new Worker(WithoutPermissionsActivity.this);
                    updateUI("Starting");

                    JSONObject jsonObject = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
                    updateUI("Retrieved JSON");

                    Location location = worker.getLocation();
                    updateUI("Retrieved Location");

                    String address = worker.reverseGeocode(location);
                    updateUI("Retrieved Address");

                    worker.saveToFile(location, address, jsonObject.getString("title"), "ThreadFile.txt");
                    updateUI("Done");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        workerThread.start();
    }

    public class AsyncTaskWorker extends AsyncTask<TextView, String, Boolean> {
        private TextView textView;

        @Override
        protected Boolean doInBackground(TextView... textViews) {
            boolean returnValue = false;
            textView = textViews[0];

            if(textViews.length > 0) {
                try {
                    Worker worker = new Worker(WithoutPermissionsActivity.this);
                    publishProgress("Starting");

                    JSONObject jsonObject = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
                    publishProgress("Retrieved JSON");

                    Location location = worker.getLocation();
                    publishProgress("Retrieved Location");

                    String address = worker.reverseGeocode(location);
                    publishProgress("Retrieved Address");

                    worker.saveToFile(location, address, jsonObject.getString("title"), "AsyncFile.txt");
                    publishProgress("Done");
                    returnValue = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return returnValue;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            textView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                textView.setText("Done");
            else
                textView.setText("Something failed");
        }
    }

    public void updateUI(String message) {
        tempText = message;
        /*statusText.post(new Runnable() {
            public void run() {
                getView().setText(tempText);
            }
        });*/

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText(tempText);
            }
        });
    }
}
