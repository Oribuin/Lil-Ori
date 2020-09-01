package xyz.oribuin.lilori.handler

data class Category(val type: Type) {

    enum class Type(val categoryName: String) {
        AUTHOR("Author"),
        ADMIN("Admin"),
        GAMES("Games"),
        GENERAL("General"),
        MODERATION("Moderation"),
        MUSIC("Music"),
        SUPPORT("Support")
    }
}