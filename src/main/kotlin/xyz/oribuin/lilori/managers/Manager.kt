package xyz.oribuin.lilori.managers

import xyz.oribuin.lilori.LilOri

abstract class Manager(protected val bot: LilOri) {
    abstract fun enable()
}