package me.austince.animation;

import me.austince.canvas.PolyCanvas;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;

import java.util.Date;

/**
 * Created by austin on 3/1/17.
 */
public class AnimatedPolyCanvas extends PolyCanvas implements TimingTarget {
    public Date animationStart;

    public AnimatedPolyCanvas() {
        super();
    }

    public AnimatedPolyCanvas(int width, int height) {
        super(width, height);
    }

    @Override
    public void begin(Animator animator) {
        System.out.println("AnimatedPolyCanvas.begin");
        animationStart = new Date();
    }

    @Override
    public void end(Animator animator) {
        System.out.println("AnimatedPolyCanvas.end");
    }

    @Override
    public void repeat(Animator animator) {
//        System.out.println("AnimatedPolyCanvas.repeat");
    }

    @Override
    public void reverse(Animator animator) {
//        System.out.println("AnimatedPolyCanvas.reverse");
    }

    @Override
    public void timingEvent(Animator animator, double v) {
//        System.out.println("AnimatedPolyCanvas.timingEvent");
//        System.out.println("animator = [" + animator + "], v = [" + v + "]");
        // Reset the background
        this.clear();
        // Repaint the changes
        repaint();
    }
}
