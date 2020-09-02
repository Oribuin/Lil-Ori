package xyz.oribuin.lilori.utils

import java.awt.Color

object BotUtils {
    @JvmStatic
    fun formatList(list: List<Any>): String {
        return list.toString().removePrefix("[").removeSuffix("]").replace(", ", " ")
    }

    @JvmStatic
    fun formatToHex(color: Color): String {
        return "#${String.format("%02x%02x%02x", color.red, color.green, color.blue)}"
    }
}