package com.eventsplash;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.eventsplash.login.FingerprintLoginActivity;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

public class FingerprintLoginActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<FingerprintLoginActivity> mActivityRule = new ActivityTestRule<>(
            FingerprintLoginActivity.class);

    @Test
    public void validateFragmentPlaceholders() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.fingerprint_view))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
