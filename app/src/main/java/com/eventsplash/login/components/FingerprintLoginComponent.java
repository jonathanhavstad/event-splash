package com.eventsplash.login.components;

import com.eventsplash.login.modules.FingerprintLoginModule;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

@Component(modules = FingerprintLoginModule.class)
public interface FingerprintLoginComponent {
    Cipher providesCipher();
}
