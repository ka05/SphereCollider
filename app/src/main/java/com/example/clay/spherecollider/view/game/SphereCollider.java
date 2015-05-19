package com.example.clay.spherecollider.view.game;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.clay.spherecollider.database.DatabaseConnector;
import com.example.clay.spherecollider.view.dialogs.CustomModal;
import com.example.clay.spherecollider.view.game.management.GameMediator;
import com.example.clay.spherecollider.view.game.sensors.SensorHandler;
import com.example.clay.spherecollider.view.game.surface.GameSurface;

import java.util.HashMap;


public class SphereCollider extends Activity {
    private GameMediator gameMediator;
    private GameSurface gameSurface;
    private SensorHandler sensorHandler;

    /**
     * Executes when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set to fullscreen and landscape
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sensorHandler = new SensorHandler(this);
        gameMediator = GameMediator.getInstance();
        gameMediator.setContext(this);
        gameMediator.setSensorHandler(sensorHandler);
        gameMediator.setSphereCollider(SphereCollider.this);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
    }

    /**
     * Activity override
     * Called when the activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorHandler.startSensorListener();
    }

    /**
     * Activity override
     * Called when the game stops
     */
    @Override
    protected void onStop() {
        // Unregister the listener
        sensorHandler.stopSensorListener();
        super.onStop();
    }

    /**
     * Activity override
     * Called when the configuration changes
     *
     * @param newConfig the new configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Updates the level database
     */
    private class UpdateLevelCompletedTask extends AsyncTask<Long, Object, Cursor> {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(SphereCollider.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params) {
            // get a cursor containing all data on given entry
            System.out.println("Params: " + params[0]);
            databaseConnector.updateLevelCompleted(params[0]);
            return null;
        } // end method doInBackground

    }

    /**
     * Updates the level database
     */
    private class UpdateLevelUnlockedTask extends AsyncTask<Long, Object, Cursor> {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(SphereCollider.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params) {
            // get a cursor containing all data on given entry
            System.out.println("Params: " + params[0]);
            databaseConnector.updateLevelUnlocked(params[0]);
            return null;
        } // end method doInBackground

    }

    /**
     * Called when the level is finished
     *
     * @param type
     * @param score
     */
    public void levelFinished(final int type, final int score, final int percent) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showEndGameModal(type, score, percent);
            }
        }, 500);
    }

    /**
     * End of Level Modal
     *
     * @param winLossType : ( 1 = completed and won, 2 = no balls left, 3 = got too big )
     */
    private void showEndGameModal(int winLossType, int score, int percent) {

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("score", score + "");

        // pass in the total amount of points the user collected per  value below
        options.put("scoreProgressVal", percent + ""); // being parsed as int in CustomModal class

        switch (winLossType) {
            case 1:
                options.put("title", "LEVEL " + gameMediator.getLevelId() + " COMPLETE");
                options.put("levelFailed", "false");
                options.put("msg", "Good Job!");
                new UpdateLevelCompletedTask().execute(gameMediator.getLevelId());
                new UpdateLevelUnlockedTask().execute(gameMediator.getLevelId() + 1); // unlocks next level
                new LoadLevelTask().execute(gameMediator.getLevelId() + 1); // gets the next level data
                break;
            case 2:
                options.put("title", "LEVEL " + gameMediator.getLevelId() + " FAILED");
                options.put("levelFailed", "true");
                options.put("msg", "You ran out of Spheres to collect to score points.");
                break;
            case 3:
                options.put("title", "LEVEL " + gameMediator.getLevelId() + " FAILED");
                options.put("levelFailed", "true");
                options.put("msg", "You grew too large.");
                break;
        }
        CustomModal cm = new CustomModal(this, "level_complete", options);
    }

    // performs database query outside GUI thread
    private class LoadLevelTask extends AsyncTask<Long, Object, Cursor>
    {
        DatabaseConnector databaseConnector =
                new DatabaseConnector(SphereCollider.this);

        // perform the database access
        @Override
        protected Cursor doInBackground(Long... params)
        {
            databaseConnector.open();

            // get a cursor containing all data on given entry
            return databaseConnector.getOneLevel(params[0]);
        } // end method doInBackground

        // use the Cursor returned from the doInBackground method
        @Override
        protected void onPostExecute(Cursor result)
        {
            super.onPostExecute(result);

            result.moveToFirst(); // move to the first item

            // get the column index for each data item
            gameMediator.setLevelId(result.getLong(result.getColumnIndex("_id")));
            gameMediator.setLevelName(result.getString(result.getColumnIndex("levelName")));
            gameMediator.setLevelBgImgSrc(result.getInt(result.getColumnIndex("levelBG")));
            gameMediator.setLevelCompleted(result.getString(result.getColumnIndex("levelCompleted")));
            gameMediator.setLevelUnlocked(result.getString(result.getColumnIndex("levelUnlocked")));

            gameMediator.setBallColor(result.getString(result.getColumnIndex("ballColor")));
            gameMediator.setInflaterColor(result.getString(result.getColumnIndex("inflaterColor")));
            gameMediator.setReducerColor(result.getString(result.getColumnIndex("reducerColor")));
            gameMediator.setNumInflaters(result.getInt(result.getColumnIndex("numInflaters")));
            gameMediator.setNumReducers(result.getInt(result.getColumnIndex("numReducers")));
            gameMediator.setNumPoints(result.getInt(result.getColumnIndex("numPoints")));
            gameMediator.setMaxPoints(result.getInt(result.getColumnIndex("maxPoints")));

            result.close(); // close the result cursor
            databaseConnector.close(); // close database connection
        } // end method onPostExecute
    }


    // if back button pressed then pause game
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        CustomModal cm = new CustomModal(SphereCollider.this, "pause_menu", null);
        gameSurface.pause();
    }
}
