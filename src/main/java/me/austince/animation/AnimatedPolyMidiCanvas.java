package me.austince.animation;

import javax.sound.midi.Receiver;

/**
 * Created by austin on 3/9/17.
 */
public class AnimatedPolyMidiCanvas extends AnimatedPolyCanvas {
    private Receiver midiReceiver;

    public AnimatedPolyMidiCanvas() {
        super();
    }

    public AnimatedPolyMidiCanvas(int width, int height) {
        super(width, height);
    }

    public Receiver getReceiver() {
        return midiReceiver;
    }

    public void setReceiver(Receiver receiver) {
        this.midiReceiver = receiver;
    }
}
