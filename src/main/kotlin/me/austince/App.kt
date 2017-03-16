@file:JvmName("AppKt")
package me.austince

import gnu.getopt.Getopt
import me.austince.animation.AnimatedMidiGui
import me.austince.animation.AnimatedPolyMidiCanvas
import me.austince.examples.midi.RectAnimationAkaiPolyCanvas
import me.austince.examples.midi.SquareAnimationAkaiPolyCanvas
import me.austince.midi.AkaiMpkMiniController
import me.austince.midi.AkaiMpkMiniReceiver
import me.austince.midi.MidiController
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

/**
 * Created by austin on 3/1/17.
 */

class App : KeyListener {
    val gui: AnimatedMidiGui
    val midiCtrl: MidiController?
    val runningThread : Thread

    constructor(name: String, midiController: MidiController?, width: Int?, height: Int?) {
        var example: AnimatedPolyMidiCanvas
        if (width != null && height != null) {
            example = RectAnimationAkaiPolyCanvas(900, 600)
//            example = SquareAnimationAkaiPolyCanvas(width, height)
        } else {
//            example = RectAnimationAkaiPolyCanvas()
            example = SquareAnimationAkaiPolyCanvas()
        }

        gui = AnimatedMidiGui(example)
        gui.title = name
        gui.canvas.addKeyListener(this)
        gui.setVisible(true)

        gui.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                stop()
            }
        })

        midiCtrl = midiController
        setupMidi()

        runningThread = object: Thread(name) {
            override fun run() {
                super.run()
            }
        }
    }

    fun setupMidi() {
        midiCtrl?.open()
        midiCtrl?.setReciever(gui.canvas.receiver)
        midiCtrl?.setReciever(object : AkaiMpkMiniReceiver() {
            override fun sendKey(key: AkaiMpkMiniController.AkaiKey?, value: Byte, l: Long) {
                when (key) {
                    AkaiMpkMiniController.AkaiKey.PAD_1_4 -> stop()
                    else -> {
                    }
                }
            }
        })
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


val USAGE = "Usage: [-u] [-l] [-w width] [-h height]"

fun main(args: Array<String>) {
    val name: String = "CS 537 Midterm Project"
    val opGetter = Getopt(name, args, "w:h:ul")
    var c: Int
    var width: Int? = null
    var height: Int? = null
    do {
        c = opGetter.getopt()

        when (c.toChar()) {
            'l' -> {
                MidiController.listDevices(true)
                exitProcess(0)
            }
            'w' -> width = Integer.parseInt(opGetter.optarg)
            'h' -> height = Integer.parseInt(opGetter.optarg)
            'u' -> { println(USAGE) ; System.exit(0) }
        }

    } while (c != -1)

    val ctrl: AkaiMpkMiniController? =
            try {
                AkaiMpkMiniController()
            } catch (e: Exception) {
                null
            }

    val app = App(name, ctrl, width, height)
    app.start()
}