package com.example.larseknu.playingwiththreads;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private String tempText;
    private Thread workerThread;
    private AsyncTaskWorker asyncTaskWorker;

    private static final int MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE = 1;
    private String[] neededPermissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar)findViewById(R.id.my_toolbar));

        statusText = findViewById(R.id.status_text);
        statusText.setText("Ready");

        //StrictMode.enableDefaults();
    }

    public void doAsyncWork(View view) {
        asyncTaskWorker = new AsyncTaskWorker();

        // Check if we have the necessary permissions
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Have we requested permission before and been denied? (as well as the user NOT selecting "never ask again")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("You need to enable access to your location and local storage to retrieve an address")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        neededPermissions ,
                                        MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE);
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
            else {
                // If not, ask for them and handle the reply in the onPermissionResult callback
                ActivityCompat.requestPermissions(MainActivity.this,
                        neededPermissions,
                        MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE);
            }
        }
        else
            asyncTaskWorker.execute(statusText);
    }


    public void doWork(View view) {
        // Check if we have the necessary permissions
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, ask for them and handle the reply in the onPermissionResult callback
            ActivityCompat.requestPermissions(MainActivity.this,
                    neededPermissions,
                    MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE);
        }
        else {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Worker worker = new Worker(MainActivity.this);
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
    }

    public class AsyncTaskWorker extends AsyncTask<TextView, String, Boolean> {
        private TextView textView;

        @Override
        protected Boolean doInBackground(TextView... textViews) {
            boolean returnValue = false;
            textView = textViews[0];

            if(textViews.length > 0) {
                try {
                    Worker worker = new Worker(MainActivity.this);
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_LOCATION_AND_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean granted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED)
                            granted = false;
                    }

                    if (granted)
                        asyncTaskWorker.execute(statusText);
                    else {

                        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), "You need to enable access to your location and local storage to retrieve an address", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Enable", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        } );
                        snackbar.show();
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.easy_permissions:
                Intent intent = new Intent(this, EasyPermissionsActivity.class);
                startActivity(intent);
                return true;
            case R.id.without_permissions:
                Intent withoutIntent = new Intent(this, WithoutPermissionsActivity.class);
                startActivity(withoutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
