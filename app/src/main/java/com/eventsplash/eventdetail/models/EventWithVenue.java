package com.eventsplash.eventdetail.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.eventsplash.R;
import com.eventsplash.model.eventbright.events.Event;
import com.eventsplash.model.eventbright.venues.Venue;
import com.eventsplash.providers.FavoritesProvider;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class EventWithVenue implements View.OnClickListener {
    private Event event;
    private Venue venue;

    private Drawable favoriteImage;

    private boolean eventAddedToUserFavorites;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Drawable getFavoriteImage() {
        return favoriteImage;
    }

    public void setFavoriteImage(Drawable favoriteImage) {
        this.favoriteImage = favoriteImage;
    }

    @BindingAdapter({"bind:favoriteImage"})
    public static void loadImage(ImageView view, EventWithVenue eventWithVenue) {
        applyFavoriteImage(view.getContext(),
                view,
                eventWithVenue);
    }

    private static void applyFavoriteImage(Context context,
                                           ImageView imageView,
                                           EventWithVenue eventWithVenue) {
        if (isUserLoggedIn(context)) {
            Cursor c = context.getContentResolver().query(FavoritesProvider.CONTENT_URI,
                    null,
                    FavoritesProvider.SEARCH_BY_USER_NAME_AND_EVENT_NAME_SELECTION,
                    new String[] {getUsername(context), eventWithVenue.getEvent().getName().getText()},
                    null,
                    null);
            if (c != null) {
                if (0 < c.getCount()) {
                    Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                            R.drawable.ic_favorite_white_24dp);
                    imageView.setImageDrawable(favoriteDrawable);
                } else {
                    Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                            R.drawable.ic_favorite_border_white_24dp);
                    imageView.setImageDrawable(favoriteDrawable);
                }
                c.close();
            } else {
                Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                        R.drawable.ic_favorite_border_white_24dp);
                imageView.setImageDrawable(favoriteDrawable);
            }
        } else {
            imageView.setImageDrawable(null);
        }
    }

    private static boolean isUserLoggedIn(Context context) {
        return context.getSharedPreferences(context.getString(R.string.login_status_pref_name), 0)
                .getBoolean(context.getString(R.string.logged_in_pref_name), false);
    }

    private static String getUsername(Context context) {
        return context.getSharedPreferences(context.getString(R.string.logged_in_username_pref_name), 0)
                .getString(context.getString(R.string.username_pref_name), null);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        ImageView imageView = (ImageView) v;
        if (isUserLoggedIn(context)) {
            Cursor c = context.getContentResolver().query(FavoritesProvider.CONTENT_URI,
                    null,
                    FavoritesProvider.SEARCH_BY_USER_NAME_AND_EVENT_NAME_SELECTION,
                    new String[] {getUsername(context), getEvent().getName().getText()},
                    null,
                    null);
            if (c != null) {
                if (0 < c.getCount()) {
                    context.getContentResolver().delete(FavoritesProvider.CONTENT_URI,
                            FavoritesProvider.SEARCH_BY_USER_NAME_AND_EVENT_NAME_SELECTION,
                            new String[] {getUsername(context), getEvent().getName().getText()});
                    Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                            R.drawable.ic_favorite_border_white_24dp);
                    imageView.setImageDrawable(favoriteDrawable);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoritesProvider._USER_NAME, getUsername(context));
                    contentValues.put(FavoritesProvider._EVENT_NAME, event.getName().getText());
                    context.getContentResolver().insert(FavoritesProvider.CONTENT_URI,
                            contentValues);
                    Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                            R.drawable.ic_favorite_white_24dp);
                    imageView.setImageDrawable(favoriteDrawable);
                }
                c.close();
            } else {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoritesProvider._USER_NAME, getUsername(context));
                    contentValues.put(FavoritesProvider._EVENT_NAME, event.getName().getText());

                    context.getContentResolver().insert(FavoritesProvider.CONTENT_URI,
                            contentValues);

                    Drawable favoriteDrawable = ContextCompat.getDrawable(context,
                            R.drawable.ic_favorite_white_24dp);
                    imageView.setImageDrawable(favoriteDrawable);
                } catch (SQLException sqlException) {

                }
            }
        }
    }
}
