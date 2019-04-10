package processingKit

/**
 * KeyFrameController
 * @param initInfos Array<キー名:String, Triple<初期座標(絶対座標のみ):Float, 座標指定タイプ:TransitionType , スタートフレーム: Int>>
 */
class KeyFrameController(initInfos: Array<String, Array<Pair <> >>){
    private val keyNamesMap: Map<String, Int> //キー名 -> 連番
    private val tripleOfFrame_Position_TransitionType: ArrayList<ArrayList<Triple<Int, Float, TransitionType>>> //連番 -> (遷移基準フレーム, 遷移座標)
    private val currentKeyFrames: ArrayList<Int> //現在実行中のキーフレーム番目
    private val prePositions: ArrayList<Float> //前フレーム位置

    val NOT_ON_KEY_FRAME = Float.NaN

    init {
        keyNamesMap = mutableMapOf()
        tripleOfFrame_Position_TransitionType = ArrayList()
        currentKeyFrames = ArrayList()
        prePositions = ArrayList()

        for(i in 0 until initInfos.size){
            val info = initInfos[i]
            keyNamesMap.plus(Pair(initInfos[i], i))

            val arrayOfFirstFrameAndPosition = ArrayList<Pair<Int, Float>>()
            tripleOfFrame_Position_TransitionType.add(arrayOfFirstFrameAndPosition)
            currentKeyFrames.add(-1)
            prePositions.add(NOT_ON_KEY_FRAME)
        }

    }

    //* ()
    private fun initPairOfFrameAndPosition(){

    }

    data class AddKeyFrameInfo()


    enum class TransitionType{
        Freeze,
        Linear,
        EaseIn,
        EaseOut,
        NOT_ON_KEY_FRAME,
        END_OF_KEY_FRAME
    }

    enum class PositionType{
        Absolute,
        Relative
    }


}