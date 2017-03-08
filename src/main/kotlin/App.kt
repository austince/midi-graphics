import gnu.getopt.Getopt
import me.austince.animation.AnimatedPolyCanvas
import me.austince.animation.AnimatedGui
import me.austince.examples.SquareAnimationPolyCanvas
import me.austince.midi.AkaiMpkKeyboardController
import me.austince.midi.MidiController
import org.jetbrains.annotations.Nullable
import java.awt.Frame
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.sound.midi.InvalidMidiDataException
import javax.sound.midi.MidiUnavailableException
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import kotlin.system.exitProcess

/**
 * Created by austin on 3/1/17.
 */

class App : KeyListener {
    val gui: AnimatedGui
    val midiCtrl : MidiController?

    constructor(name: String, @Nullable midiController: MidiController?) {
        val squareExample = SquareAnimationPolyCanvas()
        gui = AnimatedGui(squareExample)
        gui.title = name
        gui.canvas.addKeyListener(this)
        gui.setVisible(true)

        midiCtrl = midiController
        setupMidi()
    }

    fun setupMidi() {
        val receiver = object : Receiver {
            override fun send(midiMessage: MidiMessage, l: Long) {
                println(midiMessage)
            }

            override fun close() {
                println("close")
            }
        }
        midiCtrl?.setReciever(receiver)
    }

    fun start() {
        gui.start()
    }

    fun stop() {
        println("Quitting!")
        gui.stop()
        System.exit(0)
    }

    fun toggle() {
        if (this.gui.isPaused) {
            this.gui.resume()
        } else {
            this.gui.pause()
        }
    }

    override fun keyPressed(event: KeyEvent?) {
        val key: Char? = event?.getKeyChar()

        when (key) {
            'Q', 'q' -> this.stop()
            'P', 'p' -> this.toggle()
            else -> println(key)
        }
    }

    override fun keyReleased(event: KeyEvent?) {}
    override fun keyTyped(event: KeyEvent?) {}
}

fun main(args: Array<String>) {
    val name: String = "CS 537 Midterm Project"
    val opGetter = Getopt(name, args, "hlsd:n:D")
    var c: Int

    do {
        c = opGetter.getopt()

        when (c.toChar()) {
            'l' -> {
                MidiController.listDevices(false, false, true)
                exitProcess(0)
            }
            'd' -> println("d")
            (-1).toChar() -> { }
            else -> println("else: $c")
        }

    } while (c != -1)

    val ctrl: AkaiMpkKeyboardController? =
            try {
                AkaiMpkKeyboardController()
            } catch (e: Exception) {
                null
            }

    val app = App(name, ctrl)
    app.start()
}