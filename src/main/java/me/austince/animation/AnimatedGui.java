package me.austince.animation;


import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by austin on 3/1/17.
 */
public class AnimatedGui extends JFrame implements ActionListener {
    public AnimatedPolyCanvas canvas;
    private final Animator animator;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 900;
    private static final String DEFAULT_NAME = "Austin's Animated GUI!";

    public AnimatedGui() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public AnimatedGui(int width, int height) {
        this(new AnimatedPolyCanvas(width, height));
    }

    public AnimatedGui(AnimatedPolyCanvas canvas) {
        this.canvas = canvas;

        TimingSource timingSource = new SwingTimerTimingSource();
        Animator.setDefaultTimingSource(timingSource);
        timingSource.init();

        this.animator = new Animator.Builder()
                // Around 60 fps
                .setDuration(17, TimeUnit.MILLISECONDS)
                // Repeat forever
                .setRepeatCount(Animator.INFINITE)
                .setDebugName(DEFAULT_NAME)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .addTarget(this.canvas)
                .build();

        setLayout(new BorderLayout());
        setTitle(DEFAULT_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(this.canvas);
        pack();
    }

    public boolean isRunning() {
        return this.animator.isRunning();
    }

    public boolean isPaused() {
        return this.animator.isPaused();
    }

    public void start() {
        this.animator.start();
    }

    public void stop() {
        this.animator.stop();
    }

    public void pause() {
        this.animator.pause();
    }

    public void resume() {
        this.animator.resume();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("actionEvent = " + actionEvent);
    }
}
