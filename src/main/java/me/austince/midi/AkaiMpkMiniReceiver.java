package me.austince.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import me.austince.midi.AkaiMpkMiniController.AkaiKey;

/**
 * Created by austin on 3/9/17.
 */
public abstract class AkaiMpkMiniReceiver implements Receiver {

    public void sendKey(AkaiKey key, byte value, long l) {};

    @Override
    public void send(MidiMessage midiMessage, long l) {
        AkaiKey key = AkaiMpkMiniController.getKey(midiMessage);

        if (key == null) {
            System.err.printf("Key %d is not supported.\n", midiMessage.getMessage()[1]);
            return;
        }

        this.sendKey(key, AkaiMpkMiniController.getValue(midiMessage), l);
    }

    @Override
    public void close() {

    }
}