package processingKit

import processing.core.PApplet
import processing.core.PImage

class PImageManager(imageNames: Array<String>, pApplet: PApplet, resizeRatio : Float = 1f) {
    var images = ArrayList<PImage>()

    var imgMaxWidth = Integer.MIN_VALUE
    var imgMaxHeight = Integer.MIN_VALUE
    var imgMinWidth = Integer.MAX_VALUE
    var imgMinHeight = Integer.MAX_VALUE

    val imagesSize: Int
        get() = images.size

    init {
        LogUtils.d("${imageNames.size}")

        for (i in 0 until imageNames.size) {
            images.add(pApplet.loadImage(imageNames[i]).also {
                it.resize((it.width * resizeRatio).toInt(), (it.height * resizeRatio).toInt())
            })
            if(imgMaxWidth < images[i].width){imgMaxWidth = images[i].width}
            if(imgMaxHeight < images[i].height){imgMaxHeight = images[i].height}
            if(imgMinWidth > images[i].width){imgMinWidth = images[i].width}
            if(imgMinHeight > images[i].height){imgMinHeight = images[i].height}
        }

    }

    fun getImage(index: Int): PImage {
        return images[index].copy()
    }

    @JvmOverloads
    fun imageTaskRun(imageTask: ImageTask, indexes: IntArray? = null) {
        if (indexes == null) {
            imageTask.task()
        } else {
            val tempImages = Array<PImage>(indexes.size) { i -> images[indexes[i]].copy()}
            imageTask.task(*tempImages)
        }
    }
}

interface ImageTask {
    fun task(vararg images: PImage)
}