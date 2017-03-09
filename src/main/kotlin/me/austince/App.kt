package me.austince

import gnu.getopt.Getopt
import me.austince.animation.AnimatedMidiGui
import me.austince.examples.midi.SquareAnimationAkaiPolyCanvas
import me.austince.examples.midi.RectAnimationAkaiPolyCanvas
import me.austince.midi.AkaiMpkMiniController
import me.austince.midi.MidiController
import org.jetbrains.annotations.Nullable
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

/**
 * Created by austin on 3/1/17.
 */

class App : KeyListener {
    companion object {
        val NAME = "Hey"
    }

    val gui: AnimatedMidiGui
    val midiCtrl : MidiController?

    constructor(name: String, @Nullable midiController: MidiController?) {
//        val example = RectAnimationAkaiPolyCanvas()
        val example = SquareAnimationAkaiPolyCanvas()
        gui = AnimatedMidiGui(example)
        gui.title = name
        gui.canvas.addKeyListener(this)
        gui.setVisible(true)

        gui.addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                stop()
            }
        })

        midiCtrl = midiController
        setupMidi()
    }

    fun setupMidi() {
        midiCtrl?.open()
        midiCtrl?.setReciever(gui.canvas.receiver)
    }

    fun start() {
        gui.start()
    }

    fun stop() {
        println("Quitting!")
        gui.stop()
        gui.close()
        midiCtrl?.close()
        System.exit(0)
    }

    fun toggle() {
        if (this.gui.isPaused) {
            this.gui.resume()
        } else {
            this.gui.pause()
        }
    }

    fun toggleClipWindow() {
        this.gui.canvas.isClipWindowShowing = !this.gui.canvas.isClipWindowShowing
    }

    override fun keyPressed(event: KeyEvent?) {
        val key: Char? = event?.getKeyChar()

        when (key) {
            'Q', 'q' -> this.stop()
            'P', 'p' -> this.toggle()
            'C', 'c' -> this.toggleClipWindow()
            else -> println(key)
        }
    }

    override fun keyReleased(event: KeyEvent?) {}
    override fun keyTyped(event: KeyEvent?) {}
}

fun main(args: Array<String>) {
    val name: String = "CS 537 Midterm Project"
    val opGetter = Getopt(name, args, "hld:n:D")
    var c: Int

    do {
        c = opGetter.getopt()

        when (c.toChar()) {
            'l' -> {
                MidiController.listDevices(true)
                exitProcess(0)
            }
            'h' -> println("d")
        }

    } while (c != -1)

    val ctrl: AkaiMpkMiniController? =
            try {
                AkaiMpkMiniController()
            } catch (e: Exception) {
                null
            }

    val app = App(name, ctrl)
    app.start()
}