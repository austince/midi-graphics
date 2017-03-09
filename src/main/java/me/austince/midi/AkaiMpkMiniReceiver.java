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
        this.sendKey(AkaiMpkMiniController.getKey(midiMessage), AkaiMpkMiniController.getValue(midiMessage), l);
    }

    @Override
    public void close() {

    }
}