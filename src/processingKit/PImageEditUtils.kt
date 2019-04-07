package processingKit

import processing.core.PApplet.radians
import processing.core.PGraphics
import processing.core.PImage
import java.util.*

class PImageEditUtils{
    companion object {
        /**
         * drawImageByEllipse 画像を楕円形で描画する
         * @param canvasPG 出力画像
         * @param colorPImage 入力画像
         * @param amount 描画する点の数
         * @param minA A径最小サイズ
         * @param minB B径最小サイズ
         * @param rangeA A径振れ幅
         * @param rangeB B径振れ幅
         * @param minDeg 回転角最小
         * @param rangeDeg 回転角振れ幅
         * @param alpha 描画透明度
         */
        fun drawImageByEllipse(
            canvasPG: PGraphics,
            colorPImage: PImage,
            amount: Int,
            minA: Float, minB: Float, rangeA: Float, rangeB: Float,
            minDeg: Float, rangeDeg: Float, alpha: Float
        ) : PGraphics{
            if(minA < 0 || minB < 0 || minDeg < 0 || amount < 0 || alpha < 0){
                LogUtils.e("drawImageByEllipses", "IllegalStateException")
                return canvasPG
            }

            val canvasPW = canvasPG.width
            val canvasPH = canvasPG.height
            val colorPW = colorPImage.width
            val colorPH = colorPImage.height

            val random = Random()

            for (i in 0 until amount){
                val randW = random.nextFloat()
                val randH = random.nextFloat()

                //TODO Color
                val color = colorPImage.get((randW * colorPW).toInt(), (randH * colorPH).toInt())
                with(canvasPG){
                    fill(color, alpha)
                    pushMatrix()
                    translate(randW * canvasPW, randH * canvasPH)
                    rotate(radians(minDeg + random.nextFloat() * rangeDeg))
                    ellipse(0f, 0f, minA + random.nextFloat() * rangeA, minB + random.nextFloat() * rangeB)
                    popMatrix()
                }

            }

            return canvasPG
        }

    }
}