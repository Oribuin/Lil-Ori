package xyz.oribuin.lilori.handler.console

import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.handler.CommandEvent

abstract class ConsoleCMD(val bot: LilOri) {

    lateinit var name: String
        protected set
    var description: String? = null
        protected set
    var aliases: List<String>? = null
        protected set

    abstract fun executeCommand(event: ConsoleEvent)

}