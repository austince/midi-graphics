package me.austince.examples;

import me.austince.animation.AnimatedCanvas;
import me.austince.rasterizer.PolyRasterizer;
import me.austince.shapes.Rectangle;
import org.jdesktop.core.animation.timing.Animator;

import java.awt.*;
import java.util.Date;

/**
 * Created by austin on 3/7/17.
 */
public class SquareAnimationCanvas extends AnimatedCanvas {
    private Rectangle rect;
    private Point direction;

    public SquareAnimationCanvas() {
        this.rect = new Rectangle(
                this.getWidth() / 2,
                this.getHeight() / 2
        );
        this.setBackground(Color.BLACK);

        this.rect.setColor(new Color(1.0f, 0.0f, 0.0f));

        this.addPolyShape(this.rect);

        this.direction = new Point(50, 0);
    }

    private void update(double v) {
        if (direction.x > 0
                && this.rect.getMaxX() + direction.x >= this.getWidth()) {
            direction.x *= -1;
        } else if (direction.x < 0 && this.rect.getMinX() + direction.x < 0) {
            direction.x = Math.abs(direction.x);
        }
        Point translation = new Point(
                (int) (direction.x * v),
                (int) (direction.y * v)
        );

        this.rect.translate(translation);
    }

    @Override
    public void begin(Animator animator) {
        super.begin(animator);
    }

    @Override
    public void end(Animator animator) {
        super.end(animator);
    }

    @Override
    public void timingEvent(Animator animator, double v) {
        // Update first, then call the super method
        update(v);
        super.timingEvent(animator, v);
    }
}
