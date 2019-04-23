package testCode

import AppDisplayManager.AbstractAppDisplayManager
import processingKit.LogUtil

fun main(args: Array<String>){
    LogUtil.isLOG = true

    AbstractAppDisplayManager.run<TestAppDisplayManager>()
//    AbstractAppDisplayManager.run<TestKeyFrameController>()
}