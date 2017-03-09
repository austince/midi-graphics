package me.austince.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by austin on 3/7/17.
 * @see me.austince.midi.MidiController
 */
public class AkaiMpkMiniController extends MidiController {
    private static final String DEVICE_NAME = "mini";
    private static final byte MAX_VALUE = 127;
    private static final Map<Byte, AkaiKey> KEY_BINDINGS;

    // Create hashmap
    static {
        KEY_BINDINGS = new HashMap<>();
        // Pad Bank 1
        KEY_BINDINGS.put((byte) 44, AkaiKey.PAD_1_1);
        KEY_BINDINGS.put((byte) 45, AkaiKey.PAD_1_2);
        KEY_BINDINGS.put((byte) 46, AkaiKey.PAD_1_3);
        KEY_BINDINGS.put((byte) 47, AkaiKey.PAD_1_4);
        KEY_BINDINGS.put((byte) 48, AkaiKey.PAD_1_5);
        KEY_BINDINGS.put((byte) 49, AkaiKey.PAD_1_6);
        KEY_BINDINGS.put((byte) 50, AkaiKey.PAD_1_7);
        KEY_BINDINGS.put((byte) 51, AkaiKey.PAD_1_8);

        // Pad Bank 2
        KEY_BINDINGS.put((byte) 32, AkaiKey.PAD_2_1);
        KEY_BINDINGS.put((byte) 33, AkaiKey.PAD_2_2);
        KEY_BINDINGS.put((byte) 34, AkaiKey.PAD_2_3);
        KEY_BINDINGS.put((byte) 35, AkaiKey.PAD_2_4);
        KEY_BINDINGS.put((byte) 36, AkaiKey.PAD_2_5);
        KEY_BINDINGS.put((byte) 37, AkaiKey.PAD_2_6);
        KEY_BINDINGS.put((byte) 38, AkaiKey.PAD_2_7);
        KEY_BINDINGS.put((byte) 39, AkaiKey.PAD_2_8);

        KEY_BINDINGS.put((byte) 1, AkaiKey.DIAL_1);
        KEY_BINDINGS.put((byte) 2, AkaiKey.DIAL_2);
        KEY_BINDINGS.put((byte) 3, AkaiKey.DIAL_3);
        KEY_BINDINGS.put((byte) 4, AkaiKey.DIAL_4);
        KEY_BINDINGS.put((byte) 5, AkaiKey.DIAL_5);
        KEY_BINDINGS.put((byte) 6, AkaiKey.DIAL_6);
        KEY_BINDINGS.put((byte) 7, AkaiKey.DIAL_7);
        KEY_BINDINGS.put((byte) 8, AkaiKey.DIAL_8);
    }

    public enum AkaiKey {
        // Bank 1
        PAD_1_1,
        PAD_1_2,
        PAD_1_3,
        PAD_1_4,
        PAD_1_5,
        PAD_1_6,
        PAD_1_7,
        PAD_1_8,
        // Bank 2
        PAD_2_1,
        PAD_2_2,
        PAD_2_3,
        PAD_2_4,
        PAD_2_5,
        PAD_2_6,
        PAD_2_7,
        PAD_2_8,
        DIAL_1,
        DIAL_2,
        DIAL_3,
        DIAL_4,
        DIAL_5,
        DIAL_6,
        DIAL_7,
        DIAL_8,
    }

    protected static AkaiKey getKey(MidiMessage midiMessage) {
        // At the first index of the key array
        return KEY_BINDINGS.get(midiMessage.getMessage()[1]);
    }

    protected static byte getValue(MidiMessage midiMessage) {
        return midiMessage.getMessage()[2];
    }

    public static double getValuePercentage(byte value) {
        return (double) value / (double) MAX_VALUE;
    }

    public AkaiMpkMiniController() throws InvalidMidiDataException, MidiUnavailableException {
        super(DEVICE_NAME, false, true);
    }

    public AkaiMpkMiniController(int deviceIndex) throws InvalidMidiDataException, MidiUnavailableException {
        super(deviceIndex);
    }
}
