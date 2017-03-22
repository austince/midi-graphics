package me.austince.animation;

/**
 * Created by austin on 3/9/17.
 */
public class AnimatedMidiFrame extends AnimatedFrame {
    /**
     * Replaces super class's canvas with a Midi listening one
     */
    public AnimatedPolyMidiCanvas canvas;

    public AnimatedMidiFrame() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public AnimatedMidiFrame(int width, int height) {
        this(new AnimatedPolyMidiCanvas(width, height));
    }

    public AnimatedMidiFrame(AnimatedPolyMidiCanvas canvas) {
        super(canvas);
        this.canvas = canvas;
    }
}
