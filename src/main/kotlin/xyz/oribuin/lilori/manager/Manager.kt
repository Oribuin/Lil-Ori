package xyz.oribuin.lilori.manager

import xyz.oribuin.lilori.LilOri

abstract class Manager(protected val bot: LilOri) {
    abstract fun enable()

    abstract fun disable()
}