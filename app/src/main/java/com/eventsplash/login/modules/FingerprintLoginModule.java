package com.eventsplash.login.modules;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

@Module
public class FingerprintLoginModule {
    private static final String KEY_NAME = "fingerprint_reader";

    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private boolean cipherInitialized;

    private final String keyStoreProvider;
    private final boolean requireUserAuth;

    public FingerprintLoginModule(String keyStoreProvider,
                                  boolean requireUserAuth) {
        this.keyStoreProvider = keyStoreProvider;
        this.requireUserAuth = requireUserAuth;
        generateKey();
        cipherInitialized = cipherInit();
    }

    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance(keyStoreProvider);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to get KeyStore instance", e);
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    keyStoreProvider);
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(requireUserAuth)
                    .setUserAuthenticationValidityDurationSeconds(60 * 60 * 24 * 365)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Provides
    public boolean cipherInitialized() {
        return cipherInitialized;
    }

    @Provides
    public Cipher cipher() {
        return cipher;
    }
}
