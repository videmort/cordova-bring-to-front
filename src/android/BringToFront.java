package in.lucasdup.bringtofront;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class BringToFront extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("bringToFront")) {
      Intent notificationIntent = new Intent(cordova.getActivity(), cordova.getActivity().getClass());
      notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(cordova.getActivity(), 0, notificationIntent, 0);
      try {
        PowerManager powerManager = (PowerManager) cordova.getActivity().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = powerManager.newWakeLock(
            (PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
            "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) cordova.getActivity()
            .getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
        pendingIntent.send();
      } catch (PendingIntent.CanceledException e) {
        e.printStackTrace();
      }
      return true;
    }
    return false;
  }
}
