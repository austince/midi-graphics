package me.austince.animation;

import me.austince.canvas.PolyCanvas;
import me.austince.canvas.SimpleCanvas;
import me.austince.shapes.PolyShape;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingTarget;

import java.util.Date;

/**
 * Created by austin on 3/1/17.
 */
public class AnimatedCanvas extends PolyCanvas implements TimingTarget {
    public Date animationStart;


    public AnimatedCanvas() {
        super();
    }

    public AnimatedCanvas(int width, int height) {
        super(width, height);
    }

    @Override
    public void begin(Animator animator) {
        System.out.println("AnimatedCanvas.begin");
        animationStart = new Date();
    }

    @Override
    public void end(Animator animator) {
        System.out.println("AnimatedCanvas.end");
    }

    @Override
    public void repeat(Animator animator) {
//        System.out.println("AnimatedCanvas.repeat");
    }

    @Override
    public void reverse(Animator animator) {
//        System.out.println("AnimatedCanvas.reverse");
    }

    @Override
    public void timingEvent(Animator animator, double v) {
//        System.out.println("AnimatedCanvas.timingEvent");
//        System.out.println("animator = [" + animator + "], v = [" + v + "]");
        // Reset the background
        this.clear();
        repaint();
    }
}
