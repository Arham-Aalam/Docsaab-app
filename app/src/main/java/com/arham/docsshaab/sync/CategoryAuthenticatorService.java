package com.arham.docsshaab.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Arham on 28/06/2018.
 */

public class CategoryAuthenticatorService extends Service {
    private CategoryAuthenticator cAuthenticator;

    @Override
    public void onCreate() {
        cAuthenticator = new CategoryAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return cAuthenticator.getIBinder();
    }
}
