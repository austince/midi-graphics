package me.austince.midi;

import gnu.getopt.Getopt;

import javax.sound.midi.*;
import java.io.IOException;

/**
 * Created by austin on 3/7/17.
 */
public class KeyboardController {

    public static void main(String[] args) {
        String strDeviceName = "mini [hw:2,0,0]";
        int nDeviceIndex = 1;
        boolean bUseDefaultSynthesizer = false;

//        listDevicesAndExit(true, false, false);

        Getopt g = new Getopt("MidiInDump", args, "hlsd:n:D");
        int c;
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'l':
                    listDevicesAndExit(true, false, false);
                    break;
                case 'd':
                    strDeviceName = g.getOptarg();
                    break;
                default:
                    break;
            }
        }

        MidiDevice.Info info;

        if (strDeviceName != null) {
            info = getMidiDeviceInfo(strDeviceName, false);
        } else {
            info = getMidiDeviceInfo(nDeviceIndex);
        }
        if (info == null) {
            if (strDeviceName != null) {
                out("no device info found for name " + strDeviceName);
            } else {
                out("no device info found for index " + nDeviceIndex);
            }
            System.exit(1);
        }


        MidiDevice inputDevice = null;
        try {
            inputDevice = MidiSystem.getMidiDevice(info);
            inputDevice.open();
        } catch (MidiUnavailableException e) {
            out(e.toString());
        }

        if (inputDevice == null) {
            out("wasn't able to retrieve MidiDevice");
            System.exit(1);
        }


        Receiver r = new Receiver() {
            @Override
            public void send(MidiMessage midiMessage, long l) {
                System.out.println(midiMessage);
            }

            @Override
            public void close() {
                System.out.println("close");
            }
        };

        try {
            Transmitter t = inputDevice.getTransmitter();
            t.setReceiver(r);
        } catch (MidiUnavailableException e) {
            out("wasn't able to connect the device's Transmitter to the Receiver:");
            out(e.toString());
            inputDevice.close();
            System.exit(1);
        }

        out("now running; interupt the program with [ENTER] when finished");
    }


    public static void listDevicesAndExit(boolean bOnlyInput,
                                          boolean bOnlyOutput,
                                          boolean bVerbose) {
        if (bOnlyInput && !bOnlyOutput) {
            out("Available MIDI IN Devices:");
        } else if (!bOnlyInput && bOnlyOutput) {
            out("Available MIDI OUT Devices:");
        } else {
            out("Available MIDI Devices:");
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
                        out("" + i + "  "
                                + (bAllowsInput ? "IN " : "   ")
                                + (bAllowsOutput ? "OUT " : "    ")
                                + aInfos[i].getName() + ", "
                                + aInfos[i].getVendor() + ", "
                                + aInfos[i].getVersion() + ", "
                                + aInfos[i].getDescription());
                    } else {
                        out("" + i + "  " + aInfos[i].getName());
                    }
                }
            } catch (MidiUnavailableException e) {
                // device is obviously not available...
                // out(e);
            }
        }
        if (aInfos.length == 0) {
            out("[No devices available]");
        }
        System.exit(0);
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
     * @param bForOutput    If true, only output devices are
     *                      considered. If false, only input devices are considered.
     * @return A MidiDevice.Info object matching the passed device
     * name or null if none could be found.
     */
    public static MidiDevice.Info getMidiDeviceInfo(String strDeviceName, boolean bForOutput) {
        MidiDevice.Info[] aInfos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < aInfos.length; i++) {
            if (aInfos[i].getName().equals(strDeviceName)) {
                try {
                    MidiDevice device = MidiSystem.getMidiDevice(aInfos[i]);
                    boolean bAllowsInput = (device.getMaxTransmitters() != 0);
                    boolean bAllowsOutput = (device.getMaxReceivers() != 0);
                    if ((bAllowsOutput && bForOutput) || (bAllowsInput && !bForOutput)) {
                        return aInfos[i];
                    }
                } catch (MidiUnavailableException e) {
                    // TODO:
                }
            }
        }
        return null;
    }


    /**
     * Retrieve a MidiDevice.Info by index number.
     * This method returns a MidiDevice.Info whose index
     * is specified as parameter. This index matches the
     * number printed in the listDevicesAndExit method.
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

    private static void out(String strMessage) {
        System.out.println(strMessage);
    }
}
