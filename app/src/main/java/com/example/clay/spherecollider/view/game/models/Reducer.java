package com.example.clay.spherecollider.view.game.models;


import com.example.clay.spherecollider.view.game.util.PaintUtility;

/**
 * Created by wyattmcbain on 5/16/15.
 */
public class Reducer extends RobotBall {

    /**
     * Default constructor
     *
     * @param size
     * @param color
     */
    public Reducer(int size, int valueMax, int value, String color) {
        super(size, valueMax, value, ModelType.REDUCER,
                PaintUtility.configurePaintForCircle(PaintUtility.lightenColor(color, 0.2f)));
    }
}
