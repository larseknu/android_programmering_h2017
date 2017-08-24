package no.hiof.larseknu.playingwithintents;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private static final String ACTION_PRODUCE = "no.hiof.larseknu.playingwithintents.action.PRODUCE";

    private static final String PRODUCT = "no.hiof.larseknu.playingwithintents.extra.PRODUCT";
    private static final String AMOUNT = "no.hiof.larseknu.playingwithintents.extra.AMOUNT";

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Produce with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionProduce(Context context, String product, int amount) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_PRODUCE);
        intent.putExtra(PRODUCT, product);
        intent.putExtra(AMOUNT, amount);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PRODUCE.equals(action)) {
                final String product = intent.getStringExtra(PRODUCT);
                final int amount = intent.getIntExtra(AMOUNT, 100);
                handleActionProduce(product, amount);
            }
        }
    }

    /**
     * Handle action Produce in the provided background thread with the provided
     * parameters.
     */
    private void handleActionProduce(String product, int amount) {
        Log.i("MyIntentService", "Producing: " + product + " - Quantity: " + amount);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
