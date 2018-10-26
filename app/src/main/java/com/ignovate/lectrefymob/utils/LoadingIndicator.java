package com.ignovate.lectrefymob.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.ignovate.lectrefymob.login.LoginActivity;


public class LoadingIndicator {
    private static ProgressDialog mProgressDialog;

    public static void showLoading(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    public static void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public static void alertDialog(Context context, boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Good! Connected to Internet";
        } else {
            message = "Sorry! Not connected to internet";
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
