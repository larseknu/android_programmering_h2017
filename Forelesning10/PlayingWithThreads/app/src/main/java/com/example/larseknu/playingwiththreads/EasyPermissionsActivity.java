package com.example.larseknu.playingwiththreads;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EasyPermissionsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private TextView statusText;
    AsyncTaskWorker asyncTaskWorker;

    private static final int MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_permissions);

        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));
        getSupportActionBar().setTitle("EasyPermissions");

        statusText = findViewById(R.id.status_text);
        statusText.setText("Ready");
    }

    public void doAsyncWork(View view) {
        getAndSaveAddress();
    }

    @AfterPermissionGranted(MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE)
    public void getAndSaveAddress() {
        asyncTaskWorker = new AsyncTaskWorker();

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms))
            asyncTaskWorker.execute(statusText);
        else
            EasyPermissions.requestPermissions(this, "Location and and local storage access needed to be able to save a local address", MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE, perms);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("Threads", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    public class AsyncTaskWorker extends AsyncTask<TextView, String, Boolean> {
        private TextView textView;

        @Override
        protected Boolean doInBackground(TextView... textViews) {
            boolean returnValue = false;
            textView = textViews[0];

            if(textViews.length > 0) {
                try {
                    Worker worker = new Worker(EasyPermissionsActivity.this);
                    publishProgress("Starting");

                    JSONObject jsonObject = worker.getJSONObjectFromURL("http://www.it-stud.hiof.no/android/data/randomData.php");
                    publishProgress("Retrieved JSON");

                    Location location = worker.getLocation();
                    publishProgress("Retrieved Location");

                    String address = worker.reverseGeocode(location);
                    publishProgress("Retrieved Address");

                    worker.saveToFile(location, address, jsonObject.getString("title"), "Addresses.txt");

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
