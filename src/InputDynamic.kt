import com.jfoenix.controls.JFXButton
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class InputDynamic : View() {


    private var toggleReverse: JFXButton by singleAssign()
    private var colorButton: JFXButton by singleAssign()
    private var stcButton: JFXButton by singleAssign()
    private var wipeButton: JFXButton by singleAssign()
    private var mtcButton: JFXButton by singleAssign()
    private var selectedColor = Color.ALICEBLUE
    private var selectedDirection = Direction.FORWARD

    private fun changeColor() {
        selectedColor = CCPresets.random().toColor()
        root.apply {
            style {
                backgroundColor += selectedColor
            }
        }
    }

    private fun sendC(color: String) {
        MessageSender.send(mapOf("Animation" to Animations.COLOR1, "Color1" to parseHex(color)))
        changeColor()
    }

    private fun sendWIP(color: String) {
        val direction = when (selectedDirection) {
            Direction.BACKWARD -> 'B'
            Direction.FORWARD -> 'F'
        }
        MessageSender.send(mapOf("Animation" to Animations.WIPE, "Color1" to parseHex(color), "Direction" to direction))
        changeColor()
    }

    private fun sendSTC(color: String) {
        MessageSender.send(mapOf("Animation" to Animations.SPARKLETOCOLOR, "Color1" to parseHex(color), "Continuous" to true))
        changeColor()
    }

    private fun sendMTC(color: String) {
        val direction = when (selectedDirection) {
            Direction.BACKWARD -> 'B'
            Direction.FORWARD -> 'F'
        }
        MessageSender.send(mapOf("Animation" to Animations.MULTIPIXELRUN, "Color1" to parseHex(color), "Direction" to direction, "Continuous" to true))
        changeColor()
    }

    private fun sendPixelMarathon(color: String) = MessageSender.send(mapOf("Animation" to Animations.PIXELMARATHON, "Color1" to parseHex(color)))

    override val root = borderpane {

        onSwipeLeft = EventHandler {
            replaceWith(CustomColorView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
        }

        onSwipeRight = EventHandler {
            replaceWith(DefaultsView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
        }

        onTouchPressed = EventHandler {
            wakeScreen()
        }

        onKeyPressed = EventHandler { t ->
            when (t.code) {
                KeyCode.RIGHT -> {
//                    navigate(this@InputDynamic, SwipeDirection.RIGHT)
                    replaceWith(ContinuousAnimationAddView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
                }
                KeyCode.LEFT -> replaceWith(DefaultsView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
            }
        }

        val backColor = javafx.scene.paint.Color.AQUAMARINE

        style {
            backgroundColor += backColor
        }

        addExitAndBlankButtons(this)

        center {
            vbox {
                alignment = Pos.CENTER

                this += JFXButton("Pixel Marathon").apply {
                    alignment = Pos.CENTER
                    action {
                        sendPixelMarathon(Color.RED.getHex())
                    }
                }
                label {
                    style {
                        font = Font(5.0)
                    }
                }
                toggleReverse = JFXButton()
                this += toggleReverse.apply {
                    alignment = Pos.CENTER_LEFT
                    setMinSize(125.0, 50.0)
                    setMaxSize(125.0, 50.0)
                    style {
                        backgroundColor += selectedColor
                        alignment = Pos.CENTER
                    }
                    text = "Direction:\nForwards"
                    action {
                        toggleReverse.apply {
                            when (toggleReverse.text) {
                                "Direction:\nForwards" -> {
                                    toggleReverse.text = " Direction:\nBackwards"
                                    selectedDirection = Direction.BACKWARD
                                }
                                " Direction:\nBackwards" -> {
                                    toggleReverse.text = "Direction:\nForwards"
                                    selectedDirection = Direction.FORWARD
                                }
                                else -> {
                                    toggleReverse.text = "Direction:\nForwards"
                                    selectedDirection = Direction.FORWARD
                                }
                            }
                        }
                    }
                }
                label {
                    style {
                        font = Font(5.0)
                    }
                }
                colorButton = JFXButton("Color")
                this += colorButton.apply {
                    alignment = Pos.CENTER_LEFT
                    useMaxSize = true
                    setMinSize(125.0, 50.0)
                    setMaxSize(125.0, 50.0)
                    style {
                        backgroundColor += selectedColor
                        alignment = Pos.CENTER
                    }
                    action {
                        sendC(selectedColor.getHex())
                    }
                }
                label {
                    style {
                        font = Font(5.0)
                    }
                }
                stcButton = JFXButton("STC")
                this += stcButton.apply {
                    alignment = Pos.CENTER_LEFT
                    useMaxSize = true
                    setMinSize(125.0, 50.0)
                    setMaxSize(125.0, 50.0)
                    style {
                        backgroundColor += selectedColor
                        alignment = Pos.CENTER
                    }
                    action {
                        sendSTC(selectedColor.getHex())
                    }
                }
                label {
                    style {
                        font = Font(5.0)
                    }
                }
                wipeButton = JFXButton("Wipe")
                this += wipeButton.apply {
                    alignment = Pos.CENTER_LEFT
                    useMaxSize = true
                    setMinSize(125.0, 50.0)
                    setMaxSize(125.0, 50.0)
                    style {
                        backgroundColor += selectedColor
                        alignment = Pos.CENTER
                    }
                    action {
                        sendWIP(selectedColor.getHex())
                    }
                }
                label {
                    style {
                        font = Font(5.0)
                    }
                }
                mtcButton = JFXButton("MTC")
                this += mtcButton.apply {
                    alignment = Pos.CENTER_LEFT
                    useMaxSize = true
                    setMinSize(125.0, 50.0)
                    setMaxSize(125.0, 50.0)
                    style {
                        backgroundColor += selectedColor
                        alignment = Pos.CENTER
                    }
                    action {
                        sendMTC(selectedColor.getHex())
                    }
                }
            }
        }
    }
}
