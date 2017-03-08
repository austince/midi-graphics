import me.austince.animation.AnimatedCanvas
import me.austince.animation.AnimatedGui
import me.austince.examples.SquareAnimationCanvas
import java.awt.Frame
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

/**
 * Created by austin on 3/1/17.
 */

class App : KeyListener {
    val gui : AnimatedGui

    constructor() {
        val squareExample = SquareAnimationCanvas()
        gui = AnimatedGui(squareExample)
        gui.addKeyListener(this)
        gui.canvas.addKeyListener(this)
        gui.setVisible(true)
    }

    fun start() {
        gui.start()
    }

    fun stop() {
        println("Quitting!")
        System.exit(0)
    }

    override fun keyTyped(p0: KeyEvent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun keyPressed(event: KeyEvent?) {
        val key : Char? = event?.getKeyChar()

        when(key) {
            'Q', 'q' -> this.stop()
            'P', 'p' -> gui.pause()
            'R', 'r' -> gui.resume()
            else -> println(key)
        }
    }

    override fun keyReleased(p0: KeyEvent?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun main(args: Array<String>) {
    val app = App()
    app.start()
}