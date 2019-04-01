package testCode

import AppDisplayManager.AbstractAppDisplayManager
import processingKit.LogUtils
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
        LogUtils.d(imageNames.toString())

        pImageManager = PImageManager(imageNames, this, imageResizeRatio)

        if(pImageManager.imgMaxWidth != Integer.MIN_VALUE && pImageManager.imgMaxHeight != Integer.MIN_VALUE) {
            size(pImageManager.imgMaxWidth, pImageManager.imgMaxHeight)
        }else{
            throw Exception("画像が読み込めていません。")
        }
    }

    override fun setup() {
        for (i in imagesSize - 1 downTo 0){
            image(pImageManager.getImage(i), 0f, 0f)
        }
    }

    override fun draw() {
    }

}

fun main(args: Array<String>){
    LogUtils.isLOG = true

    AbstractAppDisplayManager.run<TestAppDisplayManager>()
}
