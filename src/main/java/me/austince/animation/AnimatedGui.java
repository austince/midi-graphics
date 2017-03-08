package me.austince.animation;


import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by austin on 3/1/17.
 */
public class AnimatedGui extends JFrame implements ActionListener {
    TimingSource timingSource;
    AnimatedCanvas canvas;
    Animator animator;

    public AnimatedGui() {
        this(new AnimatedCanvas());
    }

    public AnimatedGui(AnimatedCanvas canvas) {
        this.canvas = canvas;

        this.timingSource = new SwingTimerTimingSource();
        Animator.setDefaultTimingSource(this.timingSource);

        this.animator = new Animator.Builder()
                // Around 60 fps
                .setDuration(17, TimeUnit.MILLISECONDS)
                // Repeat forever
                .setRepeatCount(Animator.INFINITE)
                .setRepeatBehavior(Animator.RepeatBehavior.LOOP)
                .build();

        this.animator.addTarget(this.canvas);

        setLayout(new FlowLayout());
        setTitle("CS 537 Midterm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        int x = 5;
        Integer.toString(new Integer(x));
        add(this.canvas);
        pack();
    }

    public void start() {
        this.timingSource.init();
        animator.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("actionEvent = " + actionEvent);
    }
}
