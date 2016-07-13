package com.example.clay.spherecollider.view.level;

/**
 * Created by Clay on 4/11/2015.
 */
public class Level {
    private String levelName, levelCompleted, levelUnlocked;
    private int levelBG;
    private String ballColor, inflaterColor, reducerColor;
    private int numReducers, numInflaters, numPoints, maxPoints;

    /**
     * Default constructor
     * @param levelName
     * @param levelCompleted
     * @param levelUnlocked
     * @param levelBG
     * @param ballColor
     * @param inflaterColor
     * @param reducerColor
     * @param numReducers
     * @param numInflaters
     * @param numPoints
     * @param maxPoints
     */
    public Level(String levelName, String levelCompleted, String levelUnlocked, int levelBG,
                 String ballColor, String inflaterColor, String reducerColor,
                 int numReducers, int numInflaters, int numPoints, int maxPoints) {
        this.levelName = levelName;
        this.levelCompleted = levelCompleted;
        this.levelUnlocked = levelUnlocked;
        this.levelBG = levelBG;
        this.ballColor = ballColor;
        this.inflaterColor = inflaterColor;
        this.reducerColor = reducerColor;
        this.numReducers = numReducers;
        this.numInflaters = numInflaters;
        this.numPoints = numPoints;
        this.maxPoints = maxPoints;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelCompleted() {
        return levelCompleted;
    }

    public void setLevelCompleted(String levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    public int getLevelBG() {
        return levelBG;
    }

    public void setLevelBG(int levelBG) {
        this.levelBG = levelBG;
    }

    public String getBallColor() {
        return ballColor;
    }

    public void setBallColor(String ballColor) {
        this.ballColor = ballColor;
    }

    public String getInflaterColor() {
        return inflaterColor;
    }

    public void setInflaterColor(String inflaterColor) {
        this.inflaterColor = inflaterColor;
    }

    public String getReducerColor() {
        return reducerColor;
    }

    public void setReducerColor(String reducerColor) {
        this.reducerColor = reducerColor;
    }

    public int getNumReducers() {
        return numReducers;
    }

    public void setNumReducers(int numReducers) {
        this.numReducers = numReducers;
    }

    public int getNumInflaters() {
        return numInflaters;
    }

    public void setNumInflaters(int numInflaters) {
        this.numInflaters = numInflaters;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getLevelUnlocked() {
        return levelUnlocked;
    }

    public void setLevelUnlocked(String levelUnlocked) {
        this.levelUnlocked = levelUnlocked;
    }
}
