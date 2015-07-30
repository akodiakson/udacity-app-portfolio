package com.akodiakson.udacity.portfolio.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SpotifyPlaybackService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START_PLAYBACK = "com.akodiakson.udacity.portfolio.service.action.FOO";
//    private static final String ACTION_PAUSE_PLAYBACK = "com.akodiakson.udacity.portfolio.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.akodiakson.udacity.portfolio.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.akodiakson.udacity.portfolio.service.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionStartPlayback(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SpotifyPlaybackService.class);
        intent.setAction(ACTION_START_PLAYBACK);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

//    /**
//     * Starts this service to perform action Baz with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionPausePlayback(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, SpotifyPlaybackService.class);
//        intent.setAction(ACTION_PAUSE_PLAYBACK);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    public SpotifyPlaybackService() {
        super("SpotifyPlaybackService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
                System.out.println("spotify->playback service started: " + intent);

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_PLAYBACK.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionStartPlayback(param1, param2);
            }
//            else if (ACTION_PAUSE_PLAYBACK.equals(action)) {
//                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
//                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionPausePlayback(param1, param2);
//            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStartPlayback(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    /**
//     * Handle action Baz in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionPausePlayback(String param1, String param2) {
//        // TODO: Handle action Baz
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
}
