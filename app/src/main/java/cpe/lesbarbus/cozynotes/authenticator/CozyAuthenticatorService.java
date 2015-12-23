package cpe.lesbarbus.cozynotes.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by arthurveys on 22/12/15.
 */
public class CozyAuthenticatorService extends Service {


    private CozyAuthenticator auth;

    @Override
    public void onCreate() {
        auth = new CozyAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return auth.getIBinder();
    }
}
