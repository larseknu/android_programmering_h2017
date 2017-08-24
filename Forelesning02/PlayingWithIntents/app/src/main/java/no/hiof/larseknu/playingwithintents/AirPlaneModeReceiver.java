package no.hiof.larseknu.playingwithintents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AirPlaneModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.intent.action.AIRPLANE_MODE")) {
            boolean modeIsOn = intent.getBooleanExtra("state", false);
            Log.i("AirplaneModeReceiver", "Mode is " + (modeIsOn ? "on" : "off"));
        }
    }
}
