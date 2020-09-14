package xyz.oribuin.lilori.handler

import net.dv8tion.jda.api.Permission

data class Category(val type: Type) {

    enum class Type(val categoryName: String, val permission: Permission?) {
        AUTHOR("Author", null),
        ADMIN("Admin", Permission.ADMINISTRATOR),
        GAMES("Games", null),
        ECONOMY("Economy", null),
        GENERAL("General", null),
        MODERATION("Moderation", null),
        MUSIC("Music", null),
        SUPPORT("Support", null)
    }
}