package testCode

import AppDisplayManager.AbstractAppDisplayManager
import processingKit.KeyFrameController
import processingKit.KeyFrameController.AddKeyFrameData
import processingKit.KeyFrameController.TransitionType

class TestKeyFrameController: AbstractAppDisplayManager(){
    private val keyFrameController: KeyFrameController
    init {
        val p1x : Pair<String, ArrayList<AddKeyFrameData>> = Pair("p1x",
            arrayListOf(
                //    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute, val keyFrameSetType: KeyFrameSetType = KeyFrameSetType.Absolute)
                AddKeyFrameData(0, 0f),
                AddKeyFrameData(200, 200f, TransitionType.END_OF_KEY_FRAME)
            )
        )

        val p1y : Pair<String, ArrayList<AddKeyFrameData>> = Pair("p1y",
            arrayListOf(
                //    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute, val keyFrameSetType: KeyFrameSetType = KeyFrameSetType.Absolute)
                AddKeyFrameData(0, 0f),
                AddKeyFrameData(200, 200f, TransitionType.END_OF_KEY_FRAME)
            )
        )

        val p2x : Pair<String, ArrayList<AddKeyFrameData>> = Pair("p2x",
            arrayListOf(
                //    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute, val keyFrameSetType: KeyFrameSetType = KeyFrameSetType.Absolute)
                AddKeyFrameData(0, 0f, TransitionType.QuarticEaseInOut),
                AddKeyFrameData(200, 200f, TransitionType.QuarticEaseInOut),
                AddKeyFrameData(400, 0f, TransitionType.END_OF_KEY_FRAME)
        )
        )

        val p2y : Pair<String, ArrayList<AddKeyFrameData>> = Pair("p2y",
            arrayListOf(
                //    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute, val keyFrameSetType: KeyFrameSetType = KeyFrameSetType.Absolute)
                AddKeyFrameData(0, 0f, TransitionType.Linear),
                AddKeyFrameData(300, 150f, TransitionType.END_OF_KEY_FRAME)
            )
        )

        keyFrameController = KeyFrameController(
            arrayOf(p1x, p1y, p2x, p2y)
        )
    }

    override fun settings() {
        size(200, 200)
    }

    override fun setup() {
        background(255)

        frameRate = 10f

        noStroke()
    }

    override fun draw() {
        keyFrameController.update()

        val p1x = keyFrameController.getPosition("p1x")
        val p1y = keyFrameController.getPosition("p1y")

        val p2x = keyFrameController.getPosition("p2x")
        val p2y = keyFrameController.getPosition("p2y")


        if(p1x != keyFrameController.NOT_ON_KEY_FRAME && p1y != keyFrameController.NOT_ON_KEY_FRAME){
            fill(0f, 0f, 0f, 100f)
//            ellipse(p1x, p1y, 2f, 2f)
        }

        if(p2x != keyFrameController.NOT_ON_KEY_FRAME && p2y != keyFrameController.NOT_ON_KEY_FRAME){
            fill(255f, 0f, 0f, 100f)
            ellipse(p2x, p2y, 2f, 2f)
        }
    }
}