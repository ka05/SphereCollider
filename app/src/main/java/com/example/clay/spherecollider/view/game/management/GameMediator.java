package com.example.clay.spherecollider.view.game.management;

import android.content.Context;
import android.util.DisplayMetrics;


import com.example.clay.spherecollider.view.game.models.Ball;
import com.example.clay.spherecollider.view.game.models.Score;
import com.example.clay.spherecollider.view.game.sensors.SensorHandler;
import com.example.clay.spherecollider.view.game.surface.GameSurface;

import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Responsible for getting frame information
 */
public class GameMediator {
    private Context context;
    private SensorHandler sensorHandler;
    private GameSurface surface;

    private static GameMediator instance;
    private ConcurrentLinkedQueue models;
    private Ball gameBall;
    private Score gameScore;

    private long levelId;
    private int levelBgImgSrc;

    private String levelName;
    private String levelCompleted;
    private String ballColor, inflaterColor, reducerColor;

    private int xMax, yMax;
    private int numInflaters, numReducers, numPoints, maxPoints;
    private int score, maxScore;

    /**
     * Default constructor
     *
     */
    private GameMediator() {}

    /**
     * Gets the instance of the FrameInfo class
     * @return the FrameInfo class
     */
    public static GameMediator getInstance() {
        if (instance == null) {
            instance = new GameMediator();
        }
        return instance;
    }

    /**
     * Gets the frame bounds
     */
    private void getFrameBounds() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        yMax = Math.round(displayMetrics.heightPixels);
        xMax = Math.round(displayMetrics.widthPixels);
    }

    /**
     * Sets the sensor handler for the activity
     *
     * @param sensorHandler
     */
    public void setSensorHandler(SensorHandler sensorHandler) {
        this.sensorHandler = sensorHandler;
    }

    /**
     * Gets the sensor handler for the actibity
     *
     * @return
     */
    public SensorHandler getSensorHandler() {
        return sensorHandler;
    }

    /**
     * Sets the context of the activity
     *
     * @param context the activity context
     */
    public void setContext(Context context) {
        this.context = context;
        instance.getFrameBounds();
    }

    /**
     * Gets the current activity context
     *
     * @return the current context of the activity
     */
    public Context getContext() {
        return context;
    }

    /**
     * Gets the xMax of the frame
     *
     * @return the x bounds
     */
    public int getXMax() {
        return xMax;
    }

    /**
     * Gets the yMax of the frame
     *
     * @return the y bounds
     */
    public int getYMax() {
        return yMax;
    }

    /**
     * Gets the level name
     *
     * @return
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Sets the level name
     *
     * @param levelName
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Gets the level Id
     * @return
     */
    public long getLevelId() {
        return levelId;
    }

    /**
     * Sets the level id
     *
     * @param levelId
     */
    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    /**
     * Gets the background image source
     *
     * @return
     */
    public int getLevelBgImgSrc() {
        return levelBgImgSrc;
    }

    /**
     * Sets the background image source
     *
     * @param levelBgImgsrc
     */
    public void setLevelBgImgSrc(int levelBgImgsrc) {
        this.levelBgImgSrc = levelBgImgsrc;
    }

    /**
     * Gets the level completed string
     *
     * @return
     */
    public String getLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Sets the level completed string
     * @param levelCompleted
     */
    public void setLevelCompleted(String levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    /**
     * Gets the ball color
     *
     * @return
     */
    public String getBallColor() {
        return ballColor;
    }

    /**
     * Sets the ball color string
     *
     * @param ballColor
     */
    public void setBallColor(String ballColor) {
        this.ballColor = ballColor;
    }

    /**
     * Gets the inflater color
     *
     * @return
     */
    public String getInflaterColor() {
        return inflaterColor;
    }

    /**
     * Sets the inflater color
     *
     * @param inflaterColor
     */
    public void setInflaterColor(String inflaterColor) {
        this.inflaterColor = inflaterColor;
    }

    /**
     * Gets the reducer color
     *
     * @return
     */
    public String getReducerColor() {
        return reducerColor;
    }

    /**
     * Sets the reducer color
     *
     * @param reducerColor
     */
    public void setReducerColor(String reducerColor) {
        this.reducerColor = reducerColor;
    }

    /**
     * Gets the number of inflaters
     *
     * @return
     */
    public int getNumInflaters() {
        return numInflaters;
    }

    /**
     * Sets the number of inflaters
     *
     * @param numInflaters
     */
    public void setNumInflaters(int numInflaters) {
        this.numInflaters = numInflaters;
    }

    /**
     * Gets the number of reducers
     *
     * @return
     */
    public int getNumReducers() {
        return numReducers;
    }

    /**
     * Sets the number of reducers
     *
     * @param numReducers
     */
    public void setNumReducers(int numReducers) {
        this.numReducers = numReducers;
    }

    /**
     * Gets the number of points
     *
     * @return
     */
    public int getNumPoints() {
        return numPoints;
    }

    /**
     * Sets the number of points
     * @param numPoints
     */
    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    /**
     * Gets the max number of points
     * @return
     */
    public int getMaxPoints() {
        return maxPoints;
    }

    /**
     * Sets the max number of points
     * @param maxPoints
     */
    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    /**
     * Gets the max score
     * @return
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * Sets the max score
     * @param maxScore
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Gets the score
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the queue of models
     *
     * @return
     */
    public ConcurrentLinkedQueue getModels() {
        return models;
    }

    /**
     * Sets the models
     * @param models
     */
    public void setModels(ConcurrentLinkedQueue models) {
        this.models = models;
    }

    /**
     * Gets the game ball
     * @return
     */
    public Ball getGameBall() {
        return gameBall;
    }

    /**
     * Sets the game ball
     * @param gameBall
     */
    public void setGameBall(Ball gameBall) {
        this.gameBall = gameBall;
    }

    /**
     * Gets the game score
     * @return
     */
    public Score getGameScore() {
        return gameScore;
    }

    /**
     * Sets the game score
     * @param gameScore
     */
    public void setGameScore(Score gameScore) {
        this.gameScore = gameScore;
    }

    /**
     * Gets the surface
     *
     * @return
     */
    public GameSurface getSurface() {
        return surface;
    }

    /**
     * Sets the surface
     * @param surface
     */
    public void setSurface(GameSurface surface) {
        this.surface = surface;
    }
}
