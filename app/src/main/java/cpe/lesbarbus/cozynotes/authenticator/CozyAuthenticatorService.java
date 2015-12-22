package cpe.lesbarbus.cozynotes.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by arthurveys on 22/12/15.
 */
public class CozyAuthenticatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        CozyAuthenticator auth = new CozyAuthenticator(this);
        return auth.getIBinder();
    }
}
