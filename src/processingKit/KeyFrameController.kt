package processingKit

/**
 * KeyFrameController
 * @param initInfos Array<キー名:String, Triple<初期座標(絶対座標のみ):Float, 座標指定タイプ:TransitionType , スタートフレーム: Int>>
 */
class KeyFrameController(initInfos: Array<String>){
    private val keyNamesMap: Map<String, Int> //キー名 -> 連番
    private val listOfKeyFrameList: ArrayList<ArrayList<KeyFrameData>> //連番 -> キー番 -> (遷移基準フレーム, 遷移座標, 遷移タイプ)
    private val currentKeyFrameNumbers: ArrayList<Int> //現在実行中のキーフレーム番目
    private val prePositions: ArrayList<Float> //前フレーム位置
    private val nextFrames: ArrayList<Int> //次フレーム

    private var frame = -1
        get() = frame

    val NOT_ON_KEY_FRAME = Float.NaN

    init {
        keyNamesMap = mutableMapOf()
        listOfKeyFrameList = ArrayList()
        currentKeyFrameNumbers = ArrayList()
        prePositions = ArrayList()
        nextFrames = ArrayList()

        for(i in 0 until initInfos.size){
            keyNamesMap.plus(Pair(initInfos[i], i))

            val arrayOfFirstFrameAndPosition = ArrayList<KeyFrameData>()
            listOfKeyFrameList.add(arrayOfFirstFrameAndPosition)
            currentKeyFrameNumbers.add(-1)
            prePositions.add(NOT_ON_KEY_FRAME)
            nextFrames.add(0)
        }

    }

    fun update(){
        frame++
        for (index in 0 until keyNamesMap.size){
            if(nextFrames[index] == frame){

            }
        }
    }

    fun addPairOfFrameAndPosition(keyName: String, addKeyFrames: ArrayList<AddKeyFrameData>){
        val position = keyNamesMap[keyName]
        if(position != null){
            val keyFrameList = listOfKeyFrameList[position]
            var lastKeyFrame = if(keyFrameList.isNotEmpty()) keyFrameList.last() else KeyFrameData(-1, NOT_ON_KEY_FRAME, TransitionType.NOT_ON_FRAME)
            addKeyFrames.forEach {
                val frame = (it.frame * TheNumberOfFramesMultiplyUtil.ratio).toInt()
                val position = if(it.positionType == PositionType.Absolute) it.position else it.position + lastKeyFrame.position
                val keyFrameData = KeyFrameData(frame, position, it.transitionType)
                keyFrameList.add(keyFrameData)
                lastKeyFrame = keyFrameData
            }
        }
    }

    private data class KeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType )
    data class AddKeyFrameData(val frame: Int, val position: Float, val transitionType: TransitionType = TransitionType.Linear, val positionType: PositionType = PositionType.Absolute)

    enum class TransitionType{
        Freeze,
        Linear,
        EaseIn,
        EaseOut ,
        EaseInOut,
        NOT_ON_FRAME,
        END_OF_KEY_FRAME
    }

    enum class PositionType{
        Absolute,
        Relative
    }
}