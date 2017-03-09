package me.austince.examples.midi;

import me.austince.animation.AnimatedPolyMidiCanvas;
import me.austince.clipper.PolygonClipper;
import me.austince.midi.AkaiMpkMiniController;
import me.austince.midi.AkaiMpkMiniController.AkaiKey;
import me.austince.midi.AkaiMpkMiniReceiver;
import me.austince.polyshapes.PolyRectangle;
import org.jdesktop.core.animation.timing.Animator;

import java.awt.*;

/**
 * Created by austin on 3/7/17.
 */
public class SquareAnimationAkaiPolyCanvas extends AnimatedPolyMidiCanvas {
    private PolyRectangle rect;
    private Point direction;

    public SquareAnimationAkaiPolyCanvas() {
        this.rect = new PolyRectangle(
                this.getWidth() / 2,
                this.getHeight() / 2
        );
        this.setBackground(Color.BLACK);

        this.rect.setColor(new Color(1.0f, 0.0f, 0.0f));
        this.rect.setCenter(new Point(
                (getWidth() - 1) / 2,
                (getHeight() - 1) / 4
        ));
        this.addPolyShape(this.rect);
        this.setClipWindowShowing(true);

        this.direction = new Point(50, 0);

        this.setClipperBounds(0, 0, this.getWidth() - 1, this.getHeight() - 1);

        this.setReceiver(new AkaiMpkMiniReceiver() {
            @Override
            public void sendKey(AkaiKey key, byte value, long l) {
                double percentage = AkaiMpkMiniController.getValuePercentage(value);
                System.out.printf("key pressed: %s with value %d of %f\n", key.name(), value, percentage);

                int maxWidth = getWidth() - 1;
                int width = (int) (maxWidth * percentage);

                int maxHeight = getHeight() - 1;
                int height = (int) (maxHeight * percentage);

                PolygonClipper clipper = getClipper();
                Rectangle clipBounds = clipper.getBounds();

                switch (key) {
                    case DIAL_1:
                        // Just X
                        setClipperBounds(
                                width / 2,
                                (int) clipBounds.getY(),
                                maxWidth - (width / 2),
                                (int) clipBounds.getHeight()
                        );
                        break;
                    case DIAL_2:
                        // Just Y
                        setClipperBounds(
                                (int) clipBounds.getX(),
                                height / 2,
                                (int) clipBounds.getWidth(),
                                maxHeight - (height / 2)
                        );
                        break;
                    case DIAL_3:
                        // Both X and Y
                        setClipperBounds(
                                width / 2,
                                height / 2,
                                maxWidth - (width / 2),
                                maxHeight - (height / 2)
                        );
                        break;
                    case DIAL_4:
                        int adjX = (int) (percentage * getWidth()) / 2;
                        int adjY = (int) (percentage * getHeight()) / 2;
                        setClipperBounds(
                                (int) clipBounds.getX() + adjX,
                                (int) clipBounds.getY() + adjY,
                                (int) clipBounds.getMaxX() - adjX,
                                (int) clipBounds.getMaxY() - adjY
                        );
                        break;
                    default:
                        System.out.println("Key has no effect!");
                }
            }
        });
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
