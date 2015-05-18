package com.example.clay.spherecollider.view.game.models;


import com.example.clay.spherecollider.view.game.util.PaintUtility;

/**
 * Inflates the ball when hit by the ball
 */
public class Inflater extends RobotBall {

    /**
     * Default constructor
     *
     * @param size
     * @param color
     */
    public Inflater(int size, int valueMax, int value, String color) {
        super(size, valueMax, value, ModelType.INFLATER,
                PaintUtility.configurePaintForCircle(PaintUtility.darkenColor(color, 1.1f)));
    }
}
