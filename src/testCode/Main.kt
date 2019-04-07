package testCode

import AppDisplayManager.AbstractAppDisplayManager
import processing.core.PGraphics
import processingKit.LogUtils
import processingKit.PImageEditUtils
import processingKit.PImageManager
import processingKit.Utils
import java.lang.Exception

class TestAppDisplayManager() : AbstractAppDisplayManager(){
    private val pathHead = "res/"
    private val imageNames = arrayOf(
        "${pathHead}hyousi_0000_yagura.png",
        "${pathHead}hyousi_0001_girl.png",
        "${pathHead}hyousi_0002_tent.png",
        "${pathHead}hyousi_0003_hill.png",
        "${pathHead}hyousi_0004_background.png")
    private val imageResizeRatio = 0.3f

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
        frameRate = 20f
    }

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
                PImageEditUtils.drawImageByEllipse(canvas, img,
                    10000, 10f, 1f, 20f, 2f,
                    0f, 180f, 100f)
                canvas.endDraw()
                image(canvas, 0f, 0f)
            }
        }
    }

}

fun main(args: Array<String>){
    LogUtils.isLOG = true

    AbstractAppDisplayManager.run<TestAppDisplayManager>()
}
