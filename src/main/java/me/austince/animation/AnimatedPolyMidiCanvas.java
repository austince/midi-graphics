package me.austince.animation;

import me.austince.clipper.PolygonClipper;
import me.austince.midi.AkaiMpkMiniController;
import me.austince.midi.AkaiMpkMiniReceiver;

import javax.sound.midi.Receiver;
import java.awt.*;

/**
 * Created by austin on 3/9/17.
 */
public class AnimatedPolyMidiCanvas extends AnimatedPolyCanvas {
    private Receiver midiReceiver;

    public AnimatedPolyMidiCanvas() {
        super();
    }

    public AnimatedPolyMidiCanvas(int width, int height) {
        super(width, height);
    }

    public AkaiMpkMiniReceiver buildClipperReceiver() {
        return new AkaiMpkMiniReceiver() {
            @Override
            public void sendKey(AkaiMpkMiniController.AkaiKey key, byte value, long l) {
                double percentage = AkaiMpkMiniController.getValuePercentage(value);
//                System.out.printf("key pressed: %s with value %d of %f\n", key.name(), value, percentage);

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
                }
            }
        };
    }

    public Receiver getReceiver() {
        return midiReceiver;
    }

    public void setReceiver(Receiver receiver) {
        this.midiReceiver = receiver;
    }
}
