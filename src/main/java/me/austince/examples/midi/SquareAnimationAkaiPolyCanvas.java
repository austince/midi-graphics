package me.austince.examples.midi;

import me.austince.animation.AnimatedPolyMidiCanvas;
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
 *
 * A animation that pops in a new square and enlarges it with every pad tap.
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

    /**
     * How to respond to midi input
     * @return the midi receiver
     */
    private Receiver buildReceiver() {
        return new AkaiMpkMiniReceiver() {
            AkaiMpkMiniReceiver clipperReceiver = SquareAnimationAkaiPolyCanvas.super.buildClipperReceiver();

            @Override
            public void sendKey(AkaiMpkMiniController.AkaiKey key, byte value, long l) {
                clipperReceiver.sendKey(key, value, l);
                double percentage = AkaiMpkMiniController.getValuePercentage(value);
                System.out.printf("key pressed: %s with value %d of %f\n", key.name(), value, percentage);

                int maxWidth = getWidth() - 1;
                int maxHeight = getHeight() - 1;

                switch (key) {
                    case PAD_1_5:
                    case PAD_2_5:
                        // Only allow the strongest hit
                        if (percentage != 1) break;
                        // Create a new square to add
                        PolyRectangle square = new PolyRectangle(BASE_WIDTH);
                        // Set a random center within 75 pixels of the center
                        Point randCenter = new Point(
                                (maxWidth / 2) + (int) (Math.random() * 150 - 75),
                                (maxHeight / 2) + (int) (Math.random() * 150 - 75)
                        );
                        square.setCenter(randCenter);
                        square.setColor(Color.CYAN);
                        addPolyShape(square);
                        break;
                    default:
                }
            }
        };
    }

    @Override
    public void update(double v) {
        // Avoid race conditions by using synchronized
        synchronized (this.polyFillList) {
            Iterator<PolyShape> iter = getPolyShapes().iterator();

            while (iter.hasNext()) {
                PolyRectangle rect = (PolyRectangle) iter.next();
                rect.scaleAboutCenter(1 + direction.x * v * .1);

                if (rect.getMaxX() > getWidth() || rect.getMinX() < 0
                        || rect.getMaxY() > getHeight() || rect.getMinY() < 0) {
                    iter.remove();
                }
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
