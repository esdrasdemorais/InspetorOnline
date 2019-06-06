package br.com.octabus.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Vibrator;


public class PushMessage {

    Context context = null;
    Resources resources = null;
    int bigImageIcon = 0;
    int smallImageIcon = 0;

    public PushMessage(Context context, Resources resources, int bigImageIcon, int smallImageIcon)
    {
        this.context = context;
        this.resources = resources;
        this.bigImageIcon = bigImageIcon;
        this.smallImageIcon = smallImageIcon;
    }


    public void build(String title, String assunto, String message)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                Bitmap icon = BitmapFactory.decodeResource(this.resources, this.bigImageIcon);

                NotificationManager notif = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notify = new Notification.Builder(this.context)
                        .setContentTitle(title)
                        .setContentText(assunto)
                        .setSmallIcon(this.bigImageIcon)
                        .setLargeIcon(icon)
                        .setStyle(new Notification.BigTextStyle()
                                .bigText(message)).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);

                Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

                v.vibrate(1000);
            }

        /*
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);

        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = null;

        notify = new Notification.Builder(getApplicationContext())
                .setContentTitle("New mail from ")
                .setContentText("assunto")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(icon)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(message))
                .build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
        */
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
