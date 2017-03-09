package me.austince.examples.midi;

import me.austince.animation.AnimatedPolyMidiCanvas;
import me.austince.clipper.PolygonClipper;
import me.austince.midi.AkaiMpkMiniController;
import me.austince.midi.AkaiMpkMiniReceiver;
import me.austince.polyshapes.PolyRectangle;
import me.austince.polyshapes.PolyShape;
import org.jdesktop.core.animation.timing.Animator;

import javax.sound.midi.Receiver;
import java.awt.*;
import java.util.Iterator;

/**
 * Created by austin on 3/9/17.
 */
public class SquareAnimationAkaiPolyCanvas extends AnimatedPolyMidiCanvas {
    private static final int BASE_WIDTH = 10;
    private Point direction;

    public SquareAnimationAkaiPolyCanvas() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public SquareAnimationAkaiPolyCanvas(int width, int height) {
        super(width, height);

        this.setBackground(Color.BLACK);

        this.setClipWindowShowing(true);
        this.direction = new Point(2, 2);

        this.setClipperBounds(0, 0, this.getWidth() - 1, this.getHeight() - 1);

        this.setReceiver(buildReceiver());
    }

    private Receiver buildReceiver() {
        return new AkaiMpkMiniReceiver() {
            @Override
            public void sendKey(AkaiMpkMiniController.AkaiKey key, byte value, long l) {
                double percentage = AkaiMpkMiniController.getValuePercentage(value);
                System.out.printf("key pressed: %s with value %d of %f\n", key.name(), value, percentage);

                int maxWidth = getWidth() - 1;
                int width = (int) (maxWidth * percentage);

                int maxHeight = getHeight() - 1;
                int height = (int) (maxHeight * percentage);

                PolygonClipper clipper = getClipper();
                Rectangle clipBounds = clipper.getBounds();

                switch (key) {
                    case PAD_1_5:
                    case PAD_2_5:
                        // Create a new square to add
                        PolyRectangle square = new PolyRectangle(BASE_WIDTH);
                        square.setCenter(new Point(
                                maxWidth / 2,
                                maxHeight / 2
                        ));
                        square.setColor(Color.CYAN);
                        addPolyShape(square);
                        break;
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
                        // Both X and Y around center
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
        Iterator<PolyShape> iter = getPolyShapes().iterator();

        while (iter.hasNext()) {
            PolyRectangle rect = (PolyRectangle) iter.next();
            rect.scale(1 + direction.x * v * .1);

            if (rect.getMaxX() > getWidth() || rect.getMinX() < 0
                    || rect.getMaxY() > getHeight() || rect.getMinY() < 0) {
                iter.remove();
            }
        }
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
