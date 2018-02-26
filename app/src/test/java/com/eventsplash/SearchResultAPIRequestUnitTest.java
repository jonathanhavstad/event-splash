package com.eventsplash;

import android.support.annotation.NonNull;

import com.eventsplash.model.eventbright.events.SearchResults;
import com.eventsplash.networking.EventBriteRequestManager;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by jonathanhavstad on 2/25/18.
 */

public class SearchResultAPIRequestUnitTest {
    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @Test
    public void test_validSearchResults() throws Exception {
        SearchResults searchResults = new SearchResults();

        EventBriteRequestManager eventBriteRequestManager =
                new EventBriteRequestManager("https://www.eventbriteapi.com/v3/",
                        "H7F64OEURMM4B6QBKUAY",
                        "events/search/",
                        "venues/");
        eventBriteRequestManager.requestSearch().subscribe(o -> {
            if (o != null) {
                searchResults.setEventList(o.getEventList());
            }
        });

        Assert.assertNotNull(searchResults);
        Assert.assertNotNull(searchResults.getEventList());
        Assert.assertTrue(!searchResults.getEventList().isEmpty());
    }
}
