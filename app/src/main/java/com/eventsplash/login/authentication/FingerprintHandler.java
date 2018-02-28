package com.eventsplash.login.authentication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by jonathanhavstad on 2/27/18.
 */

public class FingerprintHandler extends
        FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;
    private OnAuthenticationEvent onAuthenticationEvent;

    public FingerprintHandler(@NonNull Context context,
                              @NonNull OnAuthenticationEvent onAuthenticationEvent) {
        this.context = context;
        this.onAuthenticationEvent = onAuthenticationEvent;
    }

    public void startAuth(FingerprintManager manager,
                          FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject,
                cancellationSignal,
                0,
                this,
                null);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        onAuthenticationEvent.authenticationSucceeded();
    }

    @Override
    public void onAuthenticationFailed() {
        onAuthenticationEvent.authenticationFailed();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        onAuthenticationEvent.authenticationFailed();
    }

    public interface OnAuthenticationEvent {
        void authenticationSucceeded();
        void authenticationFailed();
    }
}
