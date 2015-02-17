package com.example.android.scheduler;

import android.app.VideoManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;

/**
 * When the video fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class SampleVideoReceiver extends WakefulBroadcastReceiver {
    // The app's VideoManager, which provides access to the system video services.
    private VideoManager videoMgr;
    // The pending intent that is triggered when the video fires.
    private PendingIntent videoIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) {   
        // BEGIN_INCLUDE(video_onreceive)
        /* 
         * If your receiver intent includes extras that need to be passed along to the
         * service, use setComponent() to indicate that the service should handle the
         * receiver's intent. For example:
         * 
         * ComponentName comp = new ComponentName(context.getPackageName(), 
         *      MyService.class.getName());
         *
         * // This intent passed in this call will include the wake lock extra as well as 
         * // the receiver intent contents.
         * startWakefulService(context, (intent.setComponent(comp)));
         * 
         * In this example, we simply create a new intent to deliver to the service.
         * This intent holds an extra identifying the wake lock.
         */
        Intent service = new Intent(context, SampleSchedulingService.class);
        
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
        // END_INCLUDE(video_onreceive)
    }

    // BEGIN_INCLUDE(set_video)
    /**
     * Sets a repeating video that runs once a day at approximately 8:30 a.m. When the
     * video fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setVideo(Context context) {
        videoMgr = (VideoManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleVideoReceiver.class);
        videoIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the video's trigger time to 8:30 a.m.
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
  
        /* 
         * If you don't have precise time requirements, use an inexact repeating video
         * the minimize the drain on the device battery.
         * 
         * The call below specifies the video type, the trigger time, the interval at
         * which the video is fired, and the video's associated PendingIntent.
         * It uses the video type RTC_WAKEUP ("Real Time Clock" wake up), which wakes up 
         * the device and triggers the video according to the time of the device's clock. 
         * 
         * Alternatively, you can use the video type ELAPSED_REALTIME_WAKEUP to trigger 
         * an video based on how much time has elapsed since the device was booted. This 
         * is the preferred choice if your video is based on elapsed time--for example, if 
         * you simply want your video to fire every 60 minutes. You only need to use 
         * RTC_WAKEUP if you want your video to fire at a particular date/time. Remember 
         * that clock-based time may not translate well to other locales, and that your 
         * app's behavior could be affected by the user changing the device's time setting.
         * 
         * Here are some examples of ELAPSED_REALTIME_WAKEUP:
         * 
         * // Wake up the device to fire a one-time video in one minute.
         * videoMgr.set(VideoManager.ELAPSED_REALTIME_WAKEUP, 
         *         SystemClock.elapsedRealtime() +
         *         60*1000, videoIntent);
         *        
         * // Wake up the device to fire the video in 30 minutes, and every 30 minutes
         * // after that.
         * videoMgr.setInexactRepeating(VideoManager.ELAPSED_REALTIME_WAKEUP, 
         *         VideoManager.INTERVAL_HALF_HOUR, 
         *         VideoManager.INTERVAL_HALF_HOUR, videoIntent);
         */
        
        // Set the video to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        videoMgr.setInexactRepeating(VideoManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), VideoManager.INTERVAL_DAY, videoIntent);
        
        // Enable {@code SampleBootReceiver} to automatically restart the video when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_video)

    /**
     * Cancels the video.
     * @param context
     */
    // BEGIN_INCLUDE(cancel_video)
    public void cancelVideo(Context context) {
        // If the video has been set, cancel it.
        if (videoMgr!= null) {
            videoMgr.cancel(videoIntent);
        }
        
        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // video when the device is rebooted.
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(cancel_video)
}
