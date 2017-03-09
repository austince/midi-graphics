package me.austince.examples.midi;

import me.austince.animation.AnimatedPolyMidiCanvas;
import me.austince.clipper.PolygonClipper;
import me.austince.midi.AkaiMpkMiniController;
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

        this.setReceiver(buildReceiver());
    }

    private Receiver buildReceiver() {
        return new AkaiMpkMiniReceiver() {
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
                    case DIAL_5:
                        // desired horizontal center is 'width'
                        int clipHorizCenter = (int) (clipBounds.getX() + (clipBounds.getWidth() / 2));
                        int xDif = width - clipHorizCenter;

                        setClipperBounds(
                                Math.max((int) clipBounds.getX() + xDif, 0),
                                clipper.lly,
                                Math.min((int) clipBounds.getMaxX() + xDif, maxWidth),
                                clipper.ury
                        );
                        break;
                    case DIAL_6:
                        // desired vertical center is 'height'
                        int clipVertCenter = (int) (clipBounds.getY() + (clipBounds.getHeight() / 2));
                        int yDif = height - clipVertCenter;

                        setClipperBounds(
                                clipper.llx,
                                Math.max((int) clipBounds.getY() + yDif, 0),
                                clipper.urx,
                                Math.min((int) clipBounds.getMaxY() + yDif, maxHeight)
                                );
                        break;
                    default:
                        System.out.println("Key has no effect!");
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
