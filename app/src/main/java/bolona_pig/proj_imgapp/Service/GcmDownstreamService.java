package bolona_pig.proj_imgapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import bolona_pig.proj_imgapp.Activity.ClueDetail;
import bolona_pig.proj_imgapp.ObjectClass.User;
import bolona_pig.proj_imgapp.ObjectClass.UserLocalStore;
import bolona_pig.proj_imgapp.R;

public class GcmDownstreamService extends GcmListenerService {
    static final int NOTIFY_ID = 1;
    private static final String TAG = "custom_check";
    android.support.v4.app.NotificationCompat.Builder notifyBuilder;
    NotificationManager notifyManager;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();

        String clue_id = data.getString("clue_id", "");
        Log.e(TAG, "clue_id : " + clue_id);

        String notice_data = data.getString("notice_id_data", "");
        Log.e(TAG, "notice_data : " + notice_data);
        String lostName = "";
        try {
            JSONObject jObj = new JSONObject(notice_data);
            if (jObj.length() != 0) {
                lostName = jObj.getString(user.username);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (!lostName.isEmpty()) NotifyPush(clue_id, lostName);
    }

    private void NotifyPush(String clue_id, String lostName) {
        Intent intent = new Intent(this, ClueDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("clueId", clue_id);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_description_white_24dp);

        notifyBuilder = new NotificationCompat.Builder(this).setTicker("มีเบาะแสใหม่ !").setSmallIcon(R.drawable.ic_description_white_24dp).setLargeIcon(icon)
                .setContentTitle("เบาะแสของ " + lostName).setContentText("กรุณากดเพื่อดูรายละเอียดของเบาะแส").setContentIntent(pIntent)
                .setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND).setVibrate(new long[]{100, 3000, 500, 1000});

        Notification notification = notifyBuilder.build();

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notifyManager.notify(NOTIFY_ID, notification);
    }
}