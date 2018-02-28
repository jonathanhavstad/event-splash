package com.eventsplash;

import com.eventsplash.login.components.DaggerFingerprintLoginComponent;
import com.eventsplash.login.components.FingerprintLoginComponent;
import com.eventsplash.login.modules.FingerprintLoginModule;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

public class CipherGeneratedTest {
    @BeforeClass
    public static void setupTestKeystore() {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

            char[] password = "some password".toCharArray();
            ks.load(null, password);

            // Store away the keystore.
            FileOutputStream fos = new FileOutputStream("TestKeystore");
            ks.store(fos, password);
            fos.close();
        } catch (KeyStoreException |
                IOException |
                NoSuchAlgorithmException |
                CertificateException e) {

        }
    }

    @Test
    public void test_cipherGenerated() throws Exception {
        FingerprintLoginComponent fingerprintLoginComponent = DaggerFingerprintLoginComponent.builder()
                .fingerprintLoginModule(new FingerprintLoginModule("TestKeystore"))
                .build();

        Assert.assertTrue(fingerprintLoginComponent.providesIsCipherInitialized());
        Assert.assertNotNull(fingerprintLoginComponent.providesCipher());
    }
}
