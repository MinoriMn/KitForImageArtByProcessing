package processingKit

import processing.core.PApplet.radians
import processing.core.PGraphics
import processing.core.PImage
import java.util.*

object PImageEditUtils {
    /**
     * drawImageByRandomEllipse 画像を楕円形で描画する(位置ランダム)
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
    fun drawImageByRandomEllipse(
        canvasPG: PGraphics,
        colorPImage: PImage,
        amount: Int,
        minA: Float, minB: Float, rangeA: Float, rangeB: Float,
        minDeg: Float, rangeDeg: Float, minAlpha: Float, rangeAlpha: Float
    ) : PGraphics{
        if(amount < 0){
            LogUtil.e("drawImageByEllipses", "IllegalStateException")
            return canvasPG
        }

        val random = Random()
        val arrayPositions = ArrayList<Pair<Float, Float>>()

        for (i in 0 until amount){
            val randW = random.nextFloat()
            val randH = random.nextFloat()

            arrayPositions.add(Pair(randW, randH))
        }

        return drawImageByEllipse(canvasPG, colorPImage, arrayPositions, minA, minB, rangeA, rangeB, minDeg, rangeDeg, minAlpha, rangeAlpha)
    }

    fun fillColor(
    canvas: PImage,
    point: Pair<Int, Int>,
    color: Int,
    isNeededPaint: (Int, Int) -> Boolean = {oldColor, targetColor -> oldColor == targetColor}
){
    val stack = Stack<Pair<Int, Int>>()
    stack.push(point)
    val targetColor = canvas.get(point.first, point.second)

    while(!stack.isEmpty()){
        val p = stack.pop()
        if(p == null){continue}
        if(p.first < 0 || p.first >= canvas.width || p.second < 0 || p.second >= canvas.height){
            continue
        }

        val pColor = canvas.get(p.first, p.second)
        if(pColor != color && isNeededPaint(pColor, targetColor)){
        canvas.set(p.first, p.second, color)

        stack.push(Pair(p.first - 1, p.second))
        stack.push(Pair(p.first + 1, p.second))
        stack.push(Pair(p.first, p.second - 1))
        stack.push(Pair(p.first, p.second + 1))
        }
    }
}


    /**
     * drawImageByEllipse 画像を楕円形で描画する
     * @param canvasPG 出力画像
     * @param colorPImage 入力画像
     * @param positions 描画位置
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
        positions: List<Pair<Float, Float>>,
        minA: Float, minB: Float, rangeA: Float, rangeB: Float,
        minDeg: Float, rangeDeg: Float, minAlpha: Float, rangeAlpha: Float
    ) : PGraphics{
        if(minA < 0 || minB < 0 || minDeg < 0 || minAlpha < 0){
            LogUtil.e("drawImageByEllipses", "IllegalStateException")
            return canvasPG
        }

        val canvasPW = canvasPG.width
        val canvasPH = canvasPG.height
        val colorPW = colorPImage.width
        val colorPH = colorPImage.height

        val random = Random()

        for (i in 0 until positions.size){
            val posPair = positions[i]
            val widthR = posPair.first
            val heightR = posPair.second

            val color = colorPImage.get((widthR * colorPW).toInt(), (heightR * colorPH).toInt())
            with(canvasPG){
                fill(color, minAlpha + random.nextFloat() * rangeAlpha)
                pushMatrix()
                translate(widthR * canvasPW, heightR * canvasPH)
                rotate(radians(minDeg + random.nextFloat() * rangeDeg))
                ellipse(0f, 0f, minA + random.nextFloat() * rangeA, minB + random.nextFloat() * rangeB)
                popMatrix()
            }

        }

        return canvasPG
    }
}
