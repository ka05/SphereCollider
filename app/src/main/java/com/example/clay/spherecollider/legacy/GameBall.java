//package com.example.clay.spherecollider;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.drawable.ShapeDrawable;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.Display;
//import android.view.View;
//import android.view.WindowManager;
//
//import com.example.clay.spherecollider.database.DatabaseConnector;
//import com.example.clay.spherecollider.view.dialogs.CustomModal;
//import com.example.clay.spherecollider.view.level.CurrentLevel;
//import com.example.clay.spherecollider.view.util.AudioPlayer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
//public class BallGame extends Activity implements SensorEventListener {
//    private CustomDrawableView mCustomDrawableView = null;
//    private ShapeDrawable mDrawable = new ShapeDrawable();
//    private float xPosition, pitch , xVelocity = 0.0f;
//    private float yPosition, roll , yVelocity = 0.0f;
//    private int xMax, yMax;
//    private int totalLevelCoinCount = 0;
//    private Bitmap mBitmap;
//    private Bitmap mBg;
//    private SensorManager sensorManager;
//    private Sensor accelerometer;
//    private Sensor magnetometer;
//    private ArrayList<Point> levelCollisionPoints;
//    private float[] gravity, geomagnetic;
//    private float frameTime = 0.666f;
//    private CurrentLevel currentLevel;
//    public int windowHeight, windowWidth;
//    private AudioPlayer ap;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//
//        // Set to fullscreen and landscape
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//        // instantiate reference to the sensorManager, accelerometer,magnetometer
//        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        initializeLevel();
//
//        mCustomDrawableView = new CustomDrawableView(this);
//        setContentView(mCustomDrawableView);
//        // setContentView(R.layout.main);
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        xMax = size.x - 50;
//        yMax = size.y - 50;
//        System.out.println("XMax: " + xMax + " yMax: " + yMax);
//
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        windowHeight = displaymetrics.heightPixels;
//        windowWidth = displaymetrics.widthPixels;
//
//        ap = new AudioPlayer(this);
//
////        // temp code
////        Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                // ONLY EXCUTE THIS IF THEY COMPLETED THE LEVEL!!
////                // updates level completed in DB - needed for rows in level selection activity - LevelView
////                new UpdateLevelCompletedTask().execute(currentLevel.getLevelId());
////                showEndGameModal(1); // 1 = level completed
////                // MAKE SURE IF YOU DO THE FOLLOWING: ( DONT RUN UpdateLevelCompletedTask() )
////                // showEndGameModal(2); // 2 = ran out of balls
////                // showEndGameModal(3); // 3 = grew too large
////            }
////        }, 1000);
//        new UpdateLevelCompletedTask().execute(currentLevel.getLevelId() + 1);
//        showEndGameModal(1);
//
//    }
//
//    public void initializeLevel(){
//
//        new LoadLevelTask().execute(currentLevel.getLevelId());
//
//        // use this to hold collision points for walls
//        levelCollisionPoints = new ArrayList<>();
//        levelCollisionPoints.add(new Point(20, yMax));
//    }
//
//
//    // I've chosen to not implement this method
//    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
//
//    // This method will update the UI on new sensor events
//    public void onSensorChanged(SensorEvent sensorEvent)
//    {
//        // get values generated by the sensors
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) gravity = sensorEvent.values;
//        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) geomagnetic = sensorEvent.values;
//
//        // get the orientation
//        if (gravity != null && geomagnetic != null) {
//            float R[] = new float[9];
//            float I[] = new float[9];
//            boolean gotRotationMatrix = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
//
//            // check for success
//            if (gotRotationMatrix) {
//                float orientation[] = new float[3];
//                SensorManager.getOrientation(R, orientation);
//                pitch = orientation[1];
//                roll = orientation[2];
//                updateBall();
//            }
//        }
//    }
//
//    private void updateBall() {
//
//        //Calculate new speed
//        xVelocity += (pitch * frameTime);
//        yVelocity += (roll * frameTime);
//
//        //Calc distance travelled in that time
//        float xS = ((xVelocity / 2) * frameTime);
//        float yS = ((yVelocity / 2) * frameTime);
//
//        //Add to position negative due to sensor
//        //readings being opposite to what we want!
//        xPosition -= xS;
//        yPosition -= yS;
//
//        if (xPosition > xMax || xPosition < 0) {
//            xVelocity *= -0.5;
//            xS = ((xVelocity / 2) * frameTime);
//            yS = ((yVelocity / 2) * frameTime);
//            xPosition -= xS;
//            yPosition -= yS;
//        }
//        else if (yPosition > yMax || yPosition < 0) {
//            yVelocity *= -0.5;
//            xS = ((xVelocity / 2) * frameTime);
//            yS = ((yVelocity / 2) * frameTime);
//            xPosition -= xS;
//            yPosition -= yS;
//        }
//
//        if (xPosition > xMax) {
//            xPosition = xMax;
//        } else if (xPosition < 0) {
//            xPosition = 0;
//        }
//        if (yPosition > yMax) {
//            yPosition = yMax;
//        } else if (yPosition < 0) {
//            yPosition = 0;
//        }
//
//
//    }
//
//
//    public void updateCoinCount(int val){
//        totalLevelCoinCount += val;
//    }
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
//        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
//    }
//
//    @Override
//    protected void onStop()
//    {
//        // Unregister the listener
//        sensorManager.unregisterListener(this);
//        super.onStop();
//    }
//
//
//    public class CustomDrawableView extends View
//    {
//        final int ROTATE_TIME_MILLIS = 200;
//        Matrix matrix;
//
//        public CustomDrawableView(Context context)
//        {
//            super(context);
//            Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
//            final int dstWidth = 50;
//            final int dstHeight = 50;
//            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true);
////            mBg = BitmapFactory.decodeResource(getResources(), R.drawable.android_game_bg2);
//            matrix = new Matrix();
//
//        }
//
//        protected void onDraw(Canvas canvas)
//        {
//
//            final Bitmap bitmap = mBitmap;
//
//            // draw background
////            canvas.drawBitmap(mBg, 0, 0, null); // for background image on canvas
//
//            // draw score
//            Paint p = new Paint();
//            p.setColor(Color.BLACK);
//            p.setTextSize(24);
//            canvas.drawText("Score: " + totalLevelCoinCount, yMax - 100, 40, p);
//
//
//            //draw ball
//            canvas.drawBitmap(bitmap, xPosition, yPosition, null);
//            invalidate();
//        }
//
//    }
//
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // TODO Auto-generated method stub
//        super.onConfigurationChanged(newConfig);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }
//
//
//
//    // performs database query outside GUI thread
//    private class UpdateLevelCompletedTask extends AsyncTask<Long, Object, Cursor>
//    {
//        DatabaseConnector databaseConnector =
//                new DatabaseConnector(BallGame.this);
//
//        // perform the database access
//        @Override
//        protected Cursor doInBackground(Long... params)
//        {
//            databaseConnector.open();
//
//            // get a cursor containing all data on given entry
//            databaseConnector.updateLevelCompleted(params[0]);
//            return null;
//        } // end method doInBackground
//
//    }
//
//    // performs database query outside GUI thread
//    private class LoadLevelTask extends AsyncTask<Long, Object, Cursor>
//    {
//        DatabaseConnector databaseConnector =
//                new DatabaseConnector(BallGame.this);
//
//        // perform the database access
//        @Override
//        protected Cursor doInBackground(Long... params)
//        {
//            databaseConnector.open();
//
//            // get a cursor containing all data on given entry
//            return databaseConnector.getOneLevel(params[0]);
//        } // end method doInBackground
//
//        // use the Cursor returned from the doInBackground method
//        @Override
//        protected void onPostExecute(Cursor result)
//        {
//            super.onPostExecute(result);
//
//            result.moveToFirst(); // move to the first item
//
//            // get the column index for each data item
//            currentLevel = CurrentLevel.getInstance(getApplicationContext());
//            currentLevel.setLevelId(result.getLong(result.getColumnIndex("_id")));
//            currentLevel.setLevelName(result.getString(result.getColumnIndex("levelname")));
//            currentLevel.setLevelBgImgsrc(result.getString(result.getColumnIndex("levelbgimgsrc")));
//            currentLevel.setLevelCompleted(result.getString(result.getColumnIndex("levelcompleted")));
//
//            currentLevel.setBallColor(result.getString(result.getColumnIndex("ballColor")));
//            currentLevel.setInflaterColor(result.getString(result.getColumnIndex("inflaterColor")));
//            currentLevel.setDeflaterColor(result.getString(result.getColumnIndex("deflaterColor")));
//
//            currentLevel.setMaxPoints(result.getInt(result.getColumnIndex("maxpoints")));
//            currentLevel.setOneStarPoints(result.getInt(result.getColumnIndex("onestarpoints")));
//            currentLevel.setTwoStarPoints(result.getInt(result.getColumnIndex("twostarpoints")));
//            currentLevel.setThreeStarPoints(result.getInt(result.getColumnIndex("threestarpoints")));
//
//            currentLevel.setNumLives(result.getInt(result.getColumnIndex("numlives")));
//            currentLevel.setNumDeflaters(result.getInt(result.getColumnIndex("numdeflaters")));
//            currentLevel.setNumInflaters(result.getInt(result.getColumnIndex("numinflaters")));
//            currentLevel.setInflaterMaxVelocity(result.getInt(result.getColumnIndex("inflatermaxvelocity")));
//            currentLevel.setInflaterMinVelocity(result.getInt(result.getColumnIndex("inflaterminvelocity")));
//
//            result.close(); // close the result cursor
//            databaseConnector.close(); // close database connection
//        } // end method onPostExecute
//    }
//
//    /**
//     * End of Level Modal
//     * @param winLossType : ( 1 = completed and won, 2 = no balls left, 3 = got too big )
//     */
//    private void showEndGameModal(int winLossType){
//
//        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("score", totalLevelCoinCount+"");
//
//        // pass in the total amount of points the user collected per  value below
//        options.put("scoreProgressVal", 87+""); // being parsed as int in CustomModal class
//
//        switch(winLossType){
//            case 1:
//                options.put("title", "Level Complete");
//                options.put("msg", "Good Job!");
//                break;
//            case 2:
//                options.put("title", "Level Failed");
//                options.put("msg", "You ran out of Spheres to collect to score points.");
//                break;
//            case 3:
//                options.put("title", "Level Failed");
//                options.put("msg", "You grew too large.");
//                break;
//        }
//
//        CustomModal cm = new CustomModal(this, "level_complete", options);
//    }
//
//    /**
//     * Grabs the percentage value for the score
//     * @param totalPoints
//     * @return
//     */
//    private int getScorePercent(int totalPoints){
//        return ( ( currentLevel.getMaxPoints() - totalPoints) / currentLevel.getMaxPoints() ) * 100;
//    }
//}