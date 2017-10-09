package no.hiof.larseknu.playingwithservices;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;

import no.hiof.larseknu.playingwithservices.services.MyIntentService;
import no.hiof.larseknu.playingwithservices.services.MyStartedService;
import no.hiof.larseknu.playingwithservices.services.MyScheduledService;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView)findViewById(R.id.resultTextView);
    }

    public void scheduleJob(View view) {
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(MyScheduledService.class)
                .setTag("MyScheduledJob")
                .setTrigger(Trigger.NOW)
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.mustSchedule(myJob);

        Log.d("ScheduleActivity", "ScheduleButton Clicked");
    }

    public void startService(View view) {
        MyResultReceiver myResultReceiver = new MyResultReceiver(null);

        Intent intent = new Intent(this, MyStartedService.class);
        intent.putExtra("receiver", myResultReceiver);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this, MyStartedService.class);
        stopService(intent);
    }

    public void retreiveAddress(View view) {
        MyResultReceiver myResultReceiver = new MyResultReceiver(null);
        MyIntentService.startActionRetreiveAndSaveAddress(this, "IntentService.txt", myResultReceiver);
    }

    private class MyResultReceiver extends ResultReceiver {
        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            Log.i("MyResultReceiver", "Thread: " + Thread.currentThread().getName());

            if (resultCode == 1 && resultData != null) {
                String result = resultData.getString("resultStartedService");
                resultTextView.setText(result);
            }
            else if (resultCode == 2 && resultData != null) {
                final String result = resultData.getString("resultIntentService");

                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("MyHandler", "Thread: " + Thread.currentThread().getName());
                        resultTextView.setText(result);
                    }
                });
            }

        }
    }
}
