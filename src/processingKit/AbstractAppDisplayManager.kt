package AppDisplayManager

import processing.core.PApplet

abstract class AbstractAppDisplayManager() : PApplet (){
    abstract override fun settings()

    abstract override fun setup()

    abstract override fun draw()

    companion object {
        inline fun <reified T : AbstractAppDisplayManager> run() : Unit = PApplet.main(T::class.qualifiedName) //processing起動
    }
}