package com.eventsplash.login;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.eventsplash.R;
import com.eventsplash.login.authentication.FingerprintHandler;
import com.eventsplash.login.components.DaggerFingerprintLoginComponent;
import com.eventsplash.login.components.FingerprintLoginComponent;
import com.eventsplash.login.modules.FingerprintLoginModule;

/**
 * Created by jonathanhavstad on 2/26/18.
 */

public class FingerprintLoginActivity extends AppCompatActivity {
    private static final int REQUEST_ACCESS_FINGERPRINT_READER = 1001;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private FingerprintLoginComponent fingerprintLoginComponent;
    private FingerprintManager.CryptoObject cryptoObject;
    private TextInputEditText usernameEdit;
    private View mainContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_login);

        mainContentView = findViewById(R.id.fingerprint_view);
        usernameEdit = findViewById(R.id.username_edit);

        keyguardManager =
                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager =
                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(this,
                    "Lock screen security not enabled in Settings",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.USE_FINGERPRINT)) {
                Snackbar.make(mainContentView, "Allow access to Fingerprint Reader", Snackbar.LENGTH_LONG)
                        .setAction("ACCEPT", v -> {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.USE_FINGERPRINT},
                                    REQUEST_ACCESS_FINGERPRINT_READER);
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.USE_FINGERPRINT},
                        REQUEST_ACCESS_FINGERPRINT_READER);

            }
        } else {
            finalizeFingerprintInitialization();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINGERPRINT_READER:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finalizeFingerprintInitialization();
                }
                break;
            default:
        }
    }

    protected void finalizeFingerprintInitialization() {
        if (!fingerprintManager.hasEnrolledFingerprints()) {

            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Register at least one fingerprint in Settings",
                    Toast.LENGTH_LONG).show();
            return;
        }

        fingerprintLoginComponent = DaggerFingerprintLoginComponent.builder()
                .fingerprintLoginModule(new FingerprintLoginModule("AndroidKeyStore",
                        false))
                .build();

        cryptoObject =
                new FingerprintManager.CryptoObject(fingerprintLoginComponent.providesCipher());

        startAuth(fingerprintManager, cryptoObject);
    }

    private void startAuth(FingerprintManager fingerprintManager,
                           FingerprintManager.CryptoObject cryptoObject) {
        FingerprintHandler helper = new FingerprintHandler(this,
                new FingerprintHandler.OnAuthenticationEvent() {
                    @Override
                    public void authenticationSucceeded() {
                        if (!usernameEdit.getText().toString().isEmpty()) {
                            Intent usernameData = new Intent();
                            usernameData.putExtra(getString(R.string.username_extra_key),
                                    usernameEdit.getText().toString());
                            setResult(RESULT_OK, usernameData);
                            finish();
                        } else {
                            Snackbar.make(mainContentView, "Please input a username", Snackbar.LENGTH_LONG)
                                    .setAction("OK", v -> {
                                        usernameEdit.requestFocus();
                                        startAuth(fingerprintManager, cryptoObject);
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void authenticationFailed() {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
        helper.startAuth(fingerprintManager, cryptoObject);
    }
}
