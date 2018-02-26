package com.eventsplash;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.eventsplash.base.MainActivity;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by jonathanhavstad on 2/25/18.
 */

public class MainActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void validateFragmentPlaceholders() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.main_content))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
