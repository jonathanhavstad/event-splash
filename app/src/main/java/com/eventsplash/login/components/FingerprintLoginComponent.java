package com.eventsplash.login.components;

import com.eventsplash.login.modules.FingerprintLoginModule;

import javax.crypto.Cipher;

import dagger.Component;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

@Component(modules = FingerprintLoginModule.class)
public interface FingerprintLoginComponent {
    Cipher providesCipher();
}
