package no.hiof.larseknu.playingwithnotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int notificationCount = 0;
    private int NOTIFICATION_ID = 1;

    public static final String AVENGERS_CHANNEL_ID = "com.hiof.larseknu.playingwithnotifications.AVENGERS";
    public static final String AVENGERS_CHANNEL_NAME = "AVENGERS MESSAGE CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initChannels();
    }

    public void initChannels() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        // create android channel
        NotificationChannel avengersChannel = new NotificationChannel(AVENGERS_CHANNEL_ID,
                AVENGERS_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        avengersChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        avengersChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        avengersChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        avengersChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(avengersChannel);
    }

    public void btnNotificationOnClick(View view) {
        String title = "Avengers";
        String text = "Assemble";

        // Create (pending)intent WITHOUT extras
        Intent intent = new Intent(this, DetailsActivity.class);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailsActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AVENGERS_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_avengers_white)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);


        // Create (pending)intent WITH extras
        Intent intentWithExtras = new Intent(this, DetailsActivity.class);
        intentWithExtras.putExtra(DetailsActivity.TITLE_EXTRA, title);
        intentWithExtras.putExtra(DetailsActivity.BODY_TEXT_EXTRA, text);
        PendingIntent pendingIntentWithExtras = PendingIntent.getActivity(this, 0, intentWithExtras, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builderExtras = new NotificationCompat.Builder(this, AVENGERS_CHANNEL_ID);
        builderExtras.setSmallIcon(R.drawable.ic_avengers_white)
                .setContentTitle(title + " with extras")
                .setContentText(text)
                .setAutoCancel(true);
        builderExtras.setContentIntent(pendingIntentWithExtras);
        Notification notificationExtras = builderExtras.build();

        notificationManager.notify(NOTIFICATION_ID+1, notificationExtras);
    }

    public void btnMultipleNotification(View view) {
        ++notificationCount;

        String title = "Messages from Hulk";
        String text = "Hello, how are you?";

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setAction("MultipleNotification");
        intent.putExtra(DetailsActivity.TITLE_EXTRA, title);
        intent.putExtra(DetailsActivity.BODY_TEXT_EXTRA, text);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (notificationCount > 1) {
            inboxStyle.setBigContentTitle("You have " + notificationCount + " messages from the Hulk");

            String[] hulk_messages = getResources().getStringArray(R.array.hulk_messages);
            String generatedHulkMessages = text + "\n";
            inboxStyle.addLine(text);

            for (int i = 0; i < notificationCount-1 && i < hulk_messages.length; i++) {
                inboxStyle.addLine(hulk_messages[i]);
                generatedHulkMessages += hulk_messages[i] + "\n";
            }

            intent.putExtra(DetailsActivity.BODY_TEXT_EXTRA, generatedHulkMessages);
        }

        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);
        // Make multi
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_hulk))
                .setNumber(notificationCount)
                .setTicker("You received another message from Hulk") // Only works for < 5.0 (except for with accessibility services (will be read out loud))
                .setStyle(inboxStyle);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void btnBigTextNotificationOnClick(View view) {
        String title = "Tony Stark";
        String bigText = "I'm not saying I'm responsible for this country's longest run of uninterrupted peace in 35 years! I'm not saying that from the ashes of captivity, never has a Phoenix metaphor been more personified! I'm not saying Uncle Sam can kick back on a lawn chair, sipping on an iced tea, because I haven't come across anyone man enough to go toe to toe with me on my best day! It's not about me. It's not about you, either. It's about legacy, the legacy left behind for future generations. It's not about us! ";
        String summary = "Phoenix metaphor personified";

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.setAction("BigTextNotification");
        intent.putExtra(DetailsActivity.TITLE_EXTRA, title);
        intent.putExtra(DetailsActivity.BODY_TEXT_EXTRA, bigText);

        NotificationCompat.Builder builder = initBasicBuilder(title + " small", summary, intent);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_iron_man));

        // Add the Big Text Style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title)
                .setSummaryText(summary)
                .bigText(bigText);
        builder.setStyle(bigTextStyle);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID+3, notification);
    }

    public void btnBigPictureNotificationOnClick (View view) {
        String title = "Avengers - to New York";
        String text = "New York is under attack!";

        // Create the Intent to display the picture in an Activity
        Intent intent = new Intent(this, PictureActivity.class);
        intent.setAction("BigPictureNotification");
        intent.putExtra(PictureActivity.TITLE_EXTRA, title);
        intent.putExtra(PictureActivity.IMAGE_TEXT_EXTRA, text);
        intent.putExtra(PictureActivity.IMAGE_RESOURCE_ID_EXTRA, R.drawable.avengers_new_york);

        // Create Builder with basic notification info
        NotificationCompat.Builder builder = initBasicBuilder(title, text, intent);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_captain_america));

        // Add the Big Picture Style
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.avengers_new_york))
                .setSummaryText(text)
                .setBigContentTitle(title);

        builder.setStyle(bigPictureStyle);

        // Construct the Notification
        Notification notification = builder.build();

        // Display the Notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID + 10, notification);
    }

    private NotificationCompat.Builder initBasicBuilder(String title, String text, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AVENGERS_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_avengers_white)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true);

        if (intent != null){
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        return builder;
    }

}
