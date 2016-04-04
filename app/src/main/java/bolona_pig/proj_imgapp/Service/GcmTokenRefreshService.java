package bolona_pig.proj_imgapp.Service;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class GcmTokenRefreshService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
    }
}