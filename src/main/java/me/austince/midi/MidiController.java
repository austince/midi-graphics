package me.austince.midi;

import javax.sound.midi.*;

/**
 * Created by austin on 3/8/17.
 */
public class MidiController {
    private MidiDevice.Info info;
    private MidiDevice device;
    private boolean allowsGetInput, allowsGetOutput;

    public MidiController(int deviceIndex) throws InvalidMidiDataException, MidiUnavailableException {
        this(getMidiDeviceInfo(deviceIndex));
    }

    public MidiController(String deviceName, boolean allowsGetOutput, boolean allowsGetInput) throws InvalidMidiDataException, MidiUnavailableException {
        this(getMidiDeviceInfo(deviceName, allowsGetOutput, allowsGetInput));
    }

    public MidiController(MidiDevice.Info info) throws InvalidMidiDataException, MidiUnavailableException {
        if (info == null) {
            throw new InvalidMidiDataException("Can't find midi: " + info);
        }
        this.info = info;
        this.device = MidiSystem.getMidiDevice(this.info);
        this.allowsGetInput = (this.device.getMaxTransmitters() != 0);
        this.allowsGetOutput = (this.device.getMaxReceivers() != 0);

        this.open();
    }

    public void setReciever(Receiver receiver) throws InvalidMidiDataException, MidiUnavailableException {
        if (!this.allowsGetInput) {
            throw new InvalidMidiDataException("Midi device " + this.info.getName() + " does not support output.");
        }

        this.device.getTransmitter().setReceiver(receiver);
    }

    public void open() throws MidiUnavailableException {
        if (!this.device.isOpen())
            this.device.open();
    }

    public void close() {
        for (Transmitter trans : this.device.getTransmitters()) {
            Receiver receiver = trans.getReceiver();
            if (receiver != null)
                receiver.close();
        }

        if (this.device.isOpen())
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
    public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName, boolean allowsOutput, boolean allowsInput) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : aInfos) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info);
            } catch (MidiUnavailableException e) {
                continue;
            }

            boolean dAllowsInput = (device.getMaxTransmitters() != 0);
            boolean dAllowsOutput = (device.getMaxReceivers() != 0);

            if (info.getName().contains(strDeviceName)
                    && (dAllowsInput == allowsInput)
                    && (dAllowsOutput == allowsOutput)) {
                return info;
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

    public static void listDevices(boolean bVerbose) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        if (aInfos.length == 0) {
            System.out.println("[No devices available]");
            return;
        }

        for (int i = 0; i < aInfos.length; i++) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
                boolean bAllowsInput = (device.getMaxTransmitters() != 0);
                boolean bAllowsOutput = (device.getMaxReceivers() != 0);

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
            } catch (MidiUnavailableException e) {
                // device is obviously not available...
                // System.out.println(e);
            }
        }

    }
}
