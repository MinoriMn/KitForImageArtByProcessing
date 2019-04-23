package testCode

import AppDisplayManager.AbstractAppDisplayManager
import processing.core.PConstants
import processingKit.LogUtil
import processingKit.PImageEditUtils
import processingKit.PImageManager
import processingKit.SaveFrameUtil
import java.lang.Exception

class Thumbnail : AbstractAppDisplayManager(){
    private val pathHead = "res/"
    private val imageNames = arrayOf(
        "${pathHead}hyousi_0000_yagura.png",
//        "${pathHead}hyousi_0001_girl.png",
        "${pathHead}hyousi_0002_tent.png",
        "${pathHead}hyousi_0003_hill.png",
        "${pathHead}hyousi_0004_background.png")
    private val imageResizeRatio = 0.5f
    private val saveFrameUtils = SaveFrameUtil(this, maxFrameNum = 2)

    lateinit var pImageManager: PImageManager
    private val imagesSize = imageNames.size

    override fun settings() {
        pImageManager = PImageManager(imageNames, this, imageResizeRatio)

        if(pImageManager.imgMaxWidth != Integer.MIN_VALUE && pImageManager.imgMaxHeight != Integer.MIN_VALUE) {
            size(pImageManager.imgMaxWidth, pImageManager.imgMaxHeight)
        }else{
            throw Exception("画像が読み込めていません。")
        }
    }

    override fun setup() {
    }

    private var dx = 0f
    private var dy = 0f

    override fun draw() {
        background(255)
        for (i in imagesSize - 1 downTo 0){
            if(i != imagesSize - 1) {
                image(pImageManager.getImage(i), 0f, 0f)
            }else{
                val img = pImageManager.getImage(i)
                val canvas = createGraphics(img.width, img.height)

                canvas.beginDraw()
                canvas.noStroke()
                canvas.image(img, 0f, 0f)
                PImageEditUtils.drawImageByRandomEllipse(canvas, img,
                    10000, 8f, 8f, 15f, 8f,
                    0f, 180f, 255f, 0f)
                canvas.blendMode(PConstants.ADD)
                PImageEditUtils.drawImageByRandomEllipse(canvas, img,
                    2000, 2f, 1f, 45f, 1f,
                    50f, 20f, 0f, 255f)
                canvas.endDraw()

                blendMode(PConstants.SUBTRACT)

                dx += random(-0.2f, 0.2f)
                dy += random(-0.2f, 0.2f)
                val r = 5f
                dx = if(dx < -r) -r else if (dx > r) r else dx
                dy = if(dy < -r) -r else if (dy > r) r else dy


                tint(255f, 0f, 0f)
                image(canvas, dx, 0f)
                tint(0f, 255f, 0f)
                image(canvas, -dx, 0f)
                tint(0f, 0f, 255f)
                image(canvas, 0f, 0f)
                noTint()
                blendMode(PConstants.BLEND)

            }
        }

        saveFrameUtils.saveFrame()
    }

}

fun main(args: Array<String>){
    LogUtil.isLOG = true

//    AbstractAppDisplayManager.run<TestAppDisplayManager>()
    AbstractAppDisplayManager.run<TestKeyFrameController>()
}
