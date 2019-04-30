package processingKit

import processingKit.TheNumberOfFramesMultiplyUtil.frameMultiply

/**
 * KeyFrameController
 * @param initInfos　初期化キーフレーム情報 initInfo = Pair<キー名 : String, キーフレーム : ArrayList<AddKeyFrameData>>
 */
class KeyFrameController(initInfos: Array<Pair<String, ArrayList<AddKeyFrameData>>>){
    private val keyNamesMap: Map<String, Int> //キー名 -> 連番
    private val listOfKeyFrameQueue: ArrayList<Queue<KeyFrameData>> //連番 -> キー番 -> (遷移基準フレーム, 遷移座標, 遷移タイプ)
    private val currentKeyFrameNumbers: ArrayList<Int> //現在実行中のキーフレーム番目
    private val bothEndsPositions: ArrayList<Pair<Float, Float>> //前後位置
    private val bothEndsFrames: ArrayList<Pair<Int, Int>> //次フレーム
    private val transitionInfos: ArrayList<Pair<TransitionType, TransitionType>>

    private var frame = -1

    val NOT_ON_KEY_FRAME = Float.NaN

    init {
        keyNamesMap = mutableMapOf()
        listOfKeyFrameQueue = ArrayList()
        currentKeyFrameNumbers = ArrayList()
        bothEndsPositions = ArrayList()
        bothEndsFrames = ArrayList()
        transitionInfos = ArrayList()

        for(i in 0 until initInfos.size){
            keyNamesMap += Pair(initInfos[i].first, i)

            val firstAddKeyFrameData = initInfos[i].second.removeAt(0)
            listOfKeyFrameQueue.add(Queue())
            currentKeyFrameNumbers.add(-1)
            bothEndsPositions.add(Pair(NOT_ON_KEY_FRAME, firstAddKeyFrameData.position))
            bothEndsFrames.add(Pair(0, frameMultiply(firstAddKeyFrameData.frame)))
            transitionInfos.add(Pair(TransitionType.NOT_ON_FRAME, firstAddKeyFrameData.transitionType))
            addPairOfFrameAndPosition(i, initInfos[i].second)
        }
    }

    fun update(){
        frame++
        for (index in 0 until keyNamesMap.size){
            if(bothEndsFrames[index].second <= frame){
                val keyFrameData = listOfKeyFrameQueue[index].dequeue()
                currentKeyFrameNumbers[index]++
                if(keyFrameData != null) {
                    bothEndsPositions[index] = Pair(bothEndsPositions[index].second, keyFrameData.position)
                    transitionInfos[index] = Pair(transitionInfos[index].second, keyFrameData.transitionType)
                    var nextFrame = if(keyFrameData.keyFrameSetType == KeyFrameSetType.Absolute) frameMultiply(keyFrameData.frame) else bothEndsFrames[index].second + frameMultiply(keyFrameData.frame)
                    if(nextFrame <= frame){nextFrame = frame + 1}
                    bothEndsFrames[index] = Pair(bothEndsFrames[index].second, nextFrame)
                }else{
                    bothEndsPositions[index] = Pair(bothEndsPositions[index].second, NOT_ON_KEY_FRAME)
                    transitionInfos[index] = Pair(transitionInfos[index].second, TransitionType.END_OF_KEY_FRAME)
                    bothEndsFrames[index] = Pair(bothEndsFrames[index].second, Int.MAX_VALUE)
                }
            }
        }
    }

    fun getPosition(keyName: String): Float{
        val index = keyNamesMap[keyName]
        return if(index != null) getPosition(index) else NOT_ON_KEY_FRAME
    }

    fun getPosition(index: Int): Float{
        return when(transitionInfos[index].first){
            TransitionType.Linear -> calPositionLinear(bothEndsPositions[index], bothEndsFrames[index])
            TransitionType.QuarticEaseIn -> calQuarticPositionEaseIn(bothEndsPositions[index], bothEndsFrames[index])
            TransitionType.QuarticEaseOut -> calQuarticPositionEaseOut(bothEndsPositions[index], bothEndsFrames[index])
            TransitionType.QuarticEaseInOut -> calQuarticPositionEaseInOut(bothEndsPositions[index], bothEndsFrames[index])
            TransitionType.Freeze -> bothEndsPositions[index].first
            else -> bothEndsPositions[index].first
        }
    }

    private fun calPositionLinear(positions: Pair<Float, Float>, frames: Pair<Int, Int>): Float{
        val d = positions.first * (frames.first - frame) + positions.second * (frame - frames.first)
        return d / (frames.second - frames.first).toFloat()
    }

    private fun calQuarticPositionEaseIn(positions: Pair<Float, Float>, frames: Pair<Int, Int>): Float{
        val diff = positions.second - positions.first
        val t = (frame - frames.first).toFloat() / (frames.second - frames.first).toFloat()
        return diff * t * t + positions.first
    }

    private fun calQuarticPositionEaseOut(positions: Pair<Float, Float>, frames: Pair<Int, Int>): Float{
        val diff = positions.second - positions.first
        val t = (frame - frames.first).toFloat() / (frames.second - frames.first).toFloat()
        return diff * t * (2f - t) + positions.first
    }

    private fun calQuarticPositionEaseInOut(positions: Pair<Float, Float>, frames: Pair<Int, Int>): Float{
        val diff = positions.second - positions.first
        var t = (frame - frames.first).toFloat() / (frames.second - frames.first).toFloat()
        if(t < 0.5f) {
            return 2f * diff * t * t + positions.first
        }else{
            return 2f * diff * (t * (2f - t) - 0.5f) + positions.first
        }
    }

    fun addPairOfFrameAndPosition(keyName: String, addKeyFrames: ArrayList<AddKeyFrameData>){
        val position = keyNamesMap[keyName]
        if(position != null) {
            addPairOfFrameAndPosition(position, addKeyFrames)
        }
    }

    private fun addPairOfFrameAndPosition(position: Int, addKeyFrames: ArrayList<AddKeyFrameData>){
        val keyFrameQueue = listOfKeyFrameQueue[position]
        var lastKeyFrame = if(!keyFrameQueue.isEmpty()) keyFrameQueue.last() else KeyFrameData(-1, NOT_ON_KEY_FRAME, TransitionType.NOT_ON_FRAME, KeyFrameSetType.Absolute)
        addKeyFrames.forEach {
            val frame = (it.frame * TheNumberOfFramesMultiplyUtil.ratio).toInt()
            val position = if(it.positionType == PositionType.Absolute) it.position else it.position + lastKeyFrame!!.position
            val keyFrameData = KeyFrameData(frame, position, it.transitionType, it.keyFrameSetType)
            keyFrameQueue.enqueue(keyFrameData)
            lastKeyFrame = keyFrameData
        }
    }

    fun getCurrentKeyFrameNumber(keyName: String): Int{
        val position = keyNamesMap[keyName]
        return if (position != null){
            currentKeyFrameNumbers[position]
        }else{
            -1
        }
    }

    private data class KeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType, val keyFrameSetType: KeyFrameSetType)
    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute, val keyFrameSetType: KeyFrameSetType = KeyFrameSetType.Absolute)

    enum class TransitionType{
        Freeze,
        Linear,
        QuarticEaseIn,
        QuarticEaseOut ,
        QuarticEaseInOut,
        NOT_ON_FRAME,
        END_OF_KEY_FRAME
    }

    enum class PositionType{
        Absolute,
        Relative
    }

    enum class KeyFrameSetType{
        Absolute,
        Relative
    }
}