package me.austince.animation;

/**
 * Created by austin on 3/9/17.
 */
public class AnimatedMidiGui extends AnimatedGui {
    /**
     * Replaces super class's canvas with a Midi listening one
     */
    public AnimatedPolyMidiCanvas canvas;

    public AnimatedMidiGui() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public AnimatedMidiGui(int width, int height) {
        this(new AnimatedPolyMidiCanvas(width, height));
    }

    public AnimatedMidiGui(AnimatedPolyMidiCanvas canvas) {
        super(canvas);
        this.canvas = canvas;
    }
}
