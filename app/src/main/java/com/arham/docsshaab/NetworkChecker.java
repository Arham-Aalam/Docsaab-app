package com.arham.docsshaab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Created by HP on 01/09/2018.
 */

public class NetworkChecker {
    AlertDialog.Builder builder;
    ConnectivityManager connectivityManager;
    public void checkNetworkStatus(Context mcontext) {
        connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

        } else {
            informUser("Please Connect to WIFI or Internet", mcontext);
        }
    }

    private void informUser(String message,Context mcontext) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert);
        }else {
            builder = new AlertDialog.Builder(mcontext);
        }
        builder.setTitle("Network Alert")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
