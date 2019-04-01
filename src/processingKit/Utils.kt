package processingKit

import java.io.File



class Utils{
    companion object {
        fun pwd() : String {
            val cd = File(".").absoluteFile.parent
            LogUtils.i("cd", cd)
            return cd
        }
    }
}

class LogUtils{
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