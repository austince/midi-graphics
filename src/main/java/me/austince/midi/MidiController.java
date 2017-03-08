package me.austince.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;

/**
 * Created by austin on 3/8/17.
 */
public class MidiController {
    private MidiDevice.Info info;
    private MidiDevice device;
    private boolean allowsInput, allowsOutput;

    public MidiController(int deviceIndex) throws InvalidMidiDataException, MidiUnavailableException {
        this(getMidiDeviceInfo(deviceIndex));
    }

    public MidiController(String deviceName) throws InvalidMidiDataException, MidiUnavailableException {
        this(getMidiDeviceInfo(deviceName));
    }

    public MidiController(MidiDevice.Info info) throws InvalidMidiDataException, MidiUnavailableException {
        if (info == null) {
            throw new InvalidMidiDataException("Can't find midi: " + info);
        }
        this.info = info;
        this.device = MidiSystem.getMidiDevice(this.info);
        this.allowsInput = (this.device.getMaxTransmitters() != 0);
        this.allowsOutput = (this.device.getMaxReceivers() != 0);

        this.open();
    }

    public void setReciever(Receiver receiver) throws InvalidMidiDataException, MidiUnavailableException {
        if (!this.allowsOutput) {
            throw new InvalidMidiDataException("Midi device " + this.info.getName() + " does not support output.");
        }

        this.device.getTransmitter().setReceiver(receiver);
    }

    public void open() throws MidiUnavailableException {
        this.device.open();
    }

    public void close() {
        this.device.close();
    }

    /**
     * Retrieve a MidiDevice.Info for a given name.
     * <p>
     * This method tries to return a MidiDevice.Info whose name
     * matches the passed name. If no matching MidiDevice.Info is
     * found, null is returned.  If bForOutput is true, then only
     * output devices are searched, otherwise only input devices.
     *
     * @param strDeviceName the name of the device for which an info
     *                      object should be retrieved.
     * @return A MidiDevice.Info object matching the passed device
     * name or null if none could be found.
     */
    public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < aInfos.length; i++) {
            if (aInfos[i].getName().equals(strDeviceName)) {
                return aInfos[i];
            }
        }
        return null;
    }

    /**
     * Retrieve a MidiDevice.Info by index number.
     * This method returns a MidiDevice.Info whose index
     * is specified as parameter. This index matches the
     * number printed in the #listDevices method.
     * If index is too small or too big, null is returned.
     *
     * @param index the index of the device to be retrieved
     * @return A MidiDevice.Info object of the specified index
     * or null if none could be found.
     */
    public static MidiDevice.Info getMidiDeviceInfo(int index) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        if ((index < 0) || (index >= aInfos.length)) {
            return null;
        }
        return aInfos[index];
    }

    public static void listDevices(boolean bOnlyInput,
                                   boolean bOnlyOutput,
                                   boolean bVerbose) {
        if (bOnlyInput && !bOnlyOutput) {
            System.out.println("Available MIDI IN Devices:");
        } else if (!bOnlyInput && bOnlyOutput) {
            System.out.println("Available MIDI Out Devices:");
        } else {
            System.out.println("Available MIDI Devices:");
        }

        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < aInfos.length; i++) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
                boolean bAllowsInput = (device.getMaxTransmitters() != 0);
                boolean bAllowsOutput = (device.getMaxReceivers() != 0);
                if ((bAllowsInput && bOnlyInput) ||
                        (bAllowsOutput && bOnlyOutput)) {
                    if (bVerbose) {
                        System.out.println("" + i + "  "
                                + (bAllowsInput ? "IN " : "   ")
                                + (bAllowsOutput ? "Out " : "    ")
                                + aInfos[i].getName() + ", "
                                + aInfos[i].getVendor() + ", "
                                + aInfos[i].getVersion() + ", "
                                + aInfos[i].getDescription());
                    } else {
                        System.out.println("" + i + "  " + aInfos[i].getName());
                    }
                }
            } catch (MidiUnavailableException e) {
                // device is obviously not available...
                // System.out.println(e);
            }
        }
        if (aInfos.length == 0) {
            System.out.println("[No devices available]");
        }
    }
}
