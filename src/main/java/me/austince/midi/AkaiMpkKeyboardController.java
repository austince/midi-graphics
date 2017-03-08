package me.austince.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

/**
 * Created by austin on 3/7/17.
 */
public class AkaiMpkKeyboardController extends MidiController {
    private static final String DEVICE_NAME = "mini [hw:2,0,0]";

    public AkaiMpkKeyboardController() throws InvalidMidiDataException, MidiUnavailableException {
        super(DEVICE_NAME);
    }

    public AkaiMpkKeyboardController(int deviceIndex) throws InvalidMidiDataException, MidiUnavailableException {
        super(deviceIndex);
    }
}
