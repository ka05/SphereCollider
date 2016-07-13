package com.example.clay.spherecollider.view.game.management;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.clay.spherecollider.view.game.models.Background;
import com.example.clay.spherecollider.view.game.models.Ball;
import com.example.clay.spherecollider.view.game.models.Inflater;
import com.example.clay.spherecollider.view.game.models.Pause;
import com.example.clay.spherecollider.view.game.models.Point;
import com.example.clay.spherecollider.view.game.models.Reducer;
import com.example.clay.spherecollider.view.game.models.Score;
import com.example.clay.spherecollider.view.game.util.RandomUtility;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Responsible for generating the starting models
 */
public class ModelManager {
    private Context context;
    private GameMediator gameMediator;
    private ConcurrentLinkedQueue models;
    private int xMax, yMax;

    private int bgImgId;
    private int numInflaters, numReducers, numPoints;
    private String ballColor, inflaterColor, reducerColor;

    /**
     * Default constructor
     */
    public ModelManager() {
        gameMediator = GameMediator.getInstance();
        this.models = gameMediator.getModels();
        this.context = gameMediator.getContext();
        this.initialize();
    }

    /**
     * Creates the level objects
     */
    public void createLevelObjects() {
        models.add(new Background(getBitmap(bgImgId))); // background

        int ballStartingSize = Math.round((float) yMax * 0.05f); // based on screen dimensions

        int cnt = 0; // counter
        while(cnt < numPoints) { // points
            int value = RandomUtility.randIntInRange(10, 30);
            int size = value + ballStartingSize;
            Point point = new Point(size, value, "#ffd600");
            point = (Point)RandomUtility.randomizeLocation(point);
            models.add(point);
            cnt++;
        }
        cnt = 0;
        while (cnt < numInflaters) {
            int inflateValue = RandomUtility.randIntInRange(40, 80);
            int maxValue = inflateValue * 3;
            int size = inflateValue + ballStartingSize;
            Inflater inflater = new Inflater(size, maxValue, inflateValue, gameMediator.getInflaterColor());
            inflater = (Inflater)RandomUtility.randomizeLocation(inflater);
            models.add(inflater);
            cnt++;
        }
        cnt = 0;
        while (cnt < numReducers) {
            int reduceValue = RandomUtility.randIntInRange(10, 20);
            int maxValue = reduceValue * 3;
            int size = reduceValue + ballStartingSize;
            Reducer reducer = new Reducer(size, maxValue, reduceValue, gameMediator.getReducerColor());
            reducer = (Reducer)RandomUtility.randomizeLocation(reducer);
            models.add(reducer);
            cnt++;
        }
        models.add(new Ball(ballStartingSize, ballColor));
        models.add(new Score());
        models.add(new Pause());
    }

    /**
     * Initializes the values from the mediator, database
     */
    private void initialize() {
        xMax = gameMediator.getXMax();
        yMax = gameMediator.getYMax();
        bgImgId = gameMediator.getLevelBgImgSrc();
        numInflaters = gameMediator.getNumInflaters();
        numReducers = gameMediator.getNumReducers();
        numPoints = gameMediator.getNumPoints();
        ballColor = gameMediator.getBallColor();
        inflaterColor = gameMediator.getInflaterColor();
        reducerColor = gameMediator.getReducerColor();
    }

    /**
     * Gets the image given an id
     * @param id
     * @return
     */
    private Bitmap getBitmap(int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id, options);
        return Bitmap.createBitmap(image);
    }
}
