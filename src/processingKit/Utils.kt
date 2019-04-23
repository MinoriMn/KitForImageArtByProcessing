package processingKit

import com.sun.istack.internal.NotNull
import processing.core.PApplet
import java.io.File
import javax.swing.*
import gifAnimation.*

class Utils{
    companion object {
        fun pwd() : String {
            val cd = File(".").absoluteFile.parent
            LogUtil.i("cd", cd)
            return cd
        }
    }
}

class LogUtil{
    companion object {
        var isLOG = false

        fun v(msg: String) = v("", msg)
        fun v(tag:String, msg:String) = if(isLOG) println("V/${tag}: ${msg}") else null

        fun d(msg: String) = d("", msg)
        fun d(tag:String, msg:String) = if(isLOG) println("D/${tag}: ${msg}")  else null

        fun i(msg: String) = i("", msg)
        fun i(tag:String, msg:String) = if(isLOG) println("I/${tag}: ${msg}")  else null

        fun e(msg: String) = e("", msg)
        fun e(tag:String, msg:String) = if(isLOG) println("E/${tag}: ${msg}")  else null

        val TAG_DEBUG = "DEBUG"
    }
}

class SaveFrameUtil(@NotNull private val pApplet: PApplet, savePath: String? = null ,private val maxFrameNum: Int = Int.MAX_VALUE){
    private var isSaveFrame = false
    private var framePrefix = "test_name"
    private var frameSuffix = ".tif"
    private var frameNum = 0

    private var savePath: String = Utils.pwd()

    init {
        checkSaveFrameDialog(savePath)
    }

    private fun checkSaveFrameDialog(savePath: String?){
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.layout = layout
        panel.add(JLabel("フレーム書き出しを実行しますか？"))
        if(JOptionPane.showConfirmDialog(
            null,
            panel,
            "フレーム出力の確認",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        ) == 1){ return }

        this.isSaveFrame = true

        panel.removeAll()
        panel.add(JLabel("書き出す画像の名前を決めてください。"))
        this.framePrefix = JOptionPane.showInputDialog(
            null,
             panel,
            "test_name")

        panel.removeAll()
        panel.add(JLabel("拡張子を入力してください"))
        this.frameSuffix = JOptionPane.showInputDialog(
            null,
            panel,
            "tif")

        if(savePath != null) {
            this.savePath = savePath
        }else{
            val chooser = JFileChooser()
            chooser.selectedFile = File(Utils.pwd())
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            chooser.showOpenDialog(null)
            val selectedFile = chooser.selectedFile
            if(selectedFile != null){
                this.savePath = selectedFile.absolutePath
                LogUtil.i("SaveFrameUtils Path", "selected path ${this.savePath}")
            }else{
                LogUtil.i("SaveFrameUtils Path", "didn't select ${this.savePath}")
            }
        }

        this.framePrefix = framePrefix
        this.frameSuffix = frameSuffix
    }

    fun saveFrame(frameNum: Int? = null){
        if(isSaveFrame) {
            val tempFrameNum = frameNum ?: (this.frameNum++)
            if(tempFrameNum < maxFrameNum) {
                LogUtil.i(
                    "Processing_saving_frame",
                    "Try to save ${framePrefix}_%04d.${frameSuffix}".format(tempFrameNum)
                )
                val frameName = "${framePrefix}_%04d.${frameSuffix}".format(tempFrameNum)
                pApplet.save("${savePath}/${frameName}")
            }
        }
    }
}

class SaveGifUtil constructor (@NotNull pApplet: PApplet, savePath: String? = null, private val maxFrameNum: Int = Int.MAX_VALUE, repeat: Int? = null, quality: Int? = null, delay: Int? = null){
    private val gifExport : GifMaker
    private var isSaveFrame = false
    private var framePrefix = "no_name"
    private var frameNum = 0
    private var savePath: String = Utils.pwd()

    init {
        val panel = JPanel()
        val layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.layout = layout
        panel.add(JLabel("フレーム書き出しを実行しますか？"))
        if(JOptionPane.showConfirmDialog(
                null,
                panel,
                "フレーム出力の確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            ) != 1){ this.isSaveFrame = true }


        panel.removeAll()
        panel.add(JLabel("書き出す画像の名前を決めてください。"))
        this.framePrefix = JOptionPane.showInputDialog(
            null,
            panel,
            "test_name")

        if(savePath != null) {
            this.savePath = savePath
        }else{
            val chooser = JFileChooser()
            chooser.selectedFile = File(Utils.pwd())
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            chooser.showOpenDialog(null)
            val selectedFile = chooser.selectedFile
            if(selectedFile != null){
                this.savePath = selectedFile.absolutePath
                LogUtil.i("SaveFrameUtils Path", "selected path ${this.savePath}")
            }else{
                LogUtil.i("SaveFrameUtils Path", "didn't select ${this.savePath}")
            }
        }

        gifExport = GifMaker(pApplet, "${this.savePath}/${framePrefix}.gif")

        if(repeat != null){
            gifExport.setRepeat(repeat)
        }

        if (quality != null) {
            gifExport.setQuality(quality)
        }

        if (delay != null) {
            gifExport.setDelay(delay)
        }
    }

    constructor(@NotNull pApplet: PApplet, savePath: String? = null, maxFrameNum: Int = Int.MAX_VALUE, repeat: Int? = null, quality: Int? = null, fps: Float? = null): this(pApplet, savePath, maxFrameNum, repeat, quality, if(fps != null) (1000 / fps).toInt() else null)

    /**
     * repeat == 0 means endless repeats.
     */
    fun setRepeat(repeat : Int){
        if(isSaveFrame){
            gifExport.setRepeat(repeat)
        }
    }

    /**
     * the default of quality is 10.
     */
    fun setQuality(quality : Int){
        if(isSaveFrame){
            gifExport.setQuality(quality)
        }
    }

    fun setDelay(delay : Int){
        if(isSaveFrame){
            gifExport.setDelay(delay)
        }
    }

    fun setFps(fps : Float){
        if(isSaveFrame){
            this.setDelay((1000 / fps).toInt())
        }
    }

    fun addFrame(){
        if(isSaveFrame){
            val tempFrameNum = frameNum++
            if(tempFrameNum < maxFrameNum) {
                LogUtil.i(
                    "Processing_saving_frame",
                    "Try to save ${framePrefix}.gif add frame:%04d".format(tempFrameNum)
                )
                gifExport.addFrame()
            }else if(tempFrameNum == maxFrameNum){
                LogUtil.i(
                    "Processing_saving_frame",
                    "finish ${framePrefix}.gif"
                )
                finish()
            }
        }
    }

    fun finish(){
        if(isSaveFrame){
            gifExport.finish()
        }
    }

    fun getGifExport() : GifMaker = gifExport
}

class TheNumberOfFramesMultiplyUtil{
    companion object {
        var ratio = 1f

        fun frameMultiply(frame: Int): Int{
            return (frame * ratio).toInt()
        }
    }
}

class Queue<T>{


    var items:MutableList<T> = mutableListOf()

    fun isEmpty():Boolean = items.isEmpty()

    fun size():Int = items.count()

    override  fun toString() = items.toString()

    fun enqueue(element:T){
        items.add(element)
    }

    fun dequeue():T?{
        return if (this.isEmpty()){
            null
        } else {
            items.removeAt(0)
        }
    }

    fun last(): T?{
        return this.get(size() - 1)
    }

    fun get(index: Int): T?{
        return if (this.isEmpty()){
            null
        } else {
            items.get(index)
        }
    }

    fun peek():T?{
        return items[0]
    }
}