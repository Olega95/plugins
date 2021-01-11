package io.flutter.plugins.androidalarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.content.pm.PackageManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
  private static PowerManager.WakeLock wakeLock;

  @override
  public void onReceive(Context context, Intent intent) {
    PowerManager powerManager = (PowerManager)
context.getSystemService(Context.POWER_SERVICE);
    wakeLock = powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            PowerManager.ACQUIRE_CAUSES_WAKEUP |
            PowerManager.ON_AFTER_RELEASE, "My wakelock");

    Intent startIntent = context
            .getPackageManager()
            .getLaunchIntentForPackage(context.getPackageName());

    startIntent.setFlags(
            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    );

    wakeLock.acquire();
    context.startActivity(startIntent);
    AlarmService.enqueueAlarmProcessing(context, intent);
    wakeLock.release();
  }
}
