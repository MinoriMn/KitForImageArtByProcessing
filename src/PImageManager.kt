import processing.core.PImage

class PImageManager(imageNames: Array<String>) {
    var images: Array<PImage>

    val imagesSize: Int
        get() = images.size

    init {
        images = arrayOfNulls<PImage>(imageNames.size)
        for (i in imageNames.indices) {
            images[i] = loadImage(imageNames[i])
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
            val tempImages = arrayOfNulls<PImage>(indexes.size)
            for (i in tempImages.indices) {
                tempImages[i] = images[indexes[i]].copy()
            }
            imageTask.task(*tempImages)
        }
    }
}

interface ImageTask {
    fun task(vararg images: PImage)
}