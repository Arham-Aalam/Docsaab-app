package com.arham.docsshaab.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by HP on 28/06/2018.
 */

public class DocSaabSyncService extends Service {
    private static final Object aSyncAdapterLock = new Object();
    private static DocsaabSyncAdapter eDocsaabSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("DocSaabSyncService", "onCreate - DocSaabSyncService");
        synchronized (aSyncAdapterLock) {
            if(eDocsaabSyncAdapter == null) {
                eDocsaabSyncAdapter = new DocsaabSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return eDocsaabSyncAdapter.getSyncAdapterBinder();
    }

}
