package xyz.oribuin.lilori.utils

import java.io.File

object FileUtils {
    @JvmStatic
    fun createFile(directory: String, fileName: String) {
        val dir = File(directory)
        val file = File(dir, fileName)

        if (!file.exists()) {
            if (dir.exists()) {
                file.mkdirs()
            }

            file.createNewFile()
        }

    }

    @JvmStatic
    fun createFile(file: File) {

        if (!file.exists()) {
            if (file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            file.createNewFile()
        }

    }
}