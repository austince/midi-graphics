package me.austince.examples.midi;

import me.austince.animation.AnimatedPolyMidiCanvas;
import me.austince.midi.AkaiMpkMiniController.AkaiKey;
import me.austince.midi.AkaiMpkMiniReceiver;
import me.austince.polyshapes.PolyRectangle;
import org.jdesktop.core.animation.timing.Animator;

import javax.sound.midi.Receiver;
import java.awt.*;

/**
 * Created by austin on 3/7/17.
 */
public class RectAnimationAkaiPolyCanvas extends AnimatedPolyMidiCanvas {
    private PolyRectangle rect;
    private Point direction;

    public RectAnimationAkaiPolyCanvas() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public RectAnimationAkaiPolyCanvas(int width, int height) {
        super(width, height);
        this.rect = new PolyRectangle(
                this.getWidth() / 2,
                this.getHeight() / 2
        );
        this.setBackground(Color.BLACK);

        this.rect.setColor(new Color(1.0f, 0.0f, 0.0f));
        this.rect.setCenter(new Point(
                (getWidth() - 1) / 2,
                (getHeight() - 1) / 2
        ));
        this.addPolyShape(this.rect);
        this.setClipWindowShowing(true);

        this.direction = new Point(50, 0);

        this.setClipperBounds(0, 0, this.getWidth() - 1, this.getHeight() - 1);

        this.setReceiver(buildReceiver());
    }

    private Receiver buildReceiver() {
        return new AkaiMpkMiniReceiver() {
            AkaiMpkMiniReceiver clipperReceiver = RectAnimationAkaiPolyCanvas.super.buildClipperReceiver();

            @Override
            public void sendKey(AkaiKey key, byte value, long l) {
                clipperReceiver.sendKey(key, value, l);
                switch (key) {
                    case DIAL_7:
                        // Rotate
                        // 0 -> should be at
                        rect.rotateAboutCenter(.5);
                        break;
                    default:
                }
            }
        };
    }

    @Override
    public void update(double v) {
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
}
