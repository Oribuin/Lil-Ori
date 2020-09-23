package xyz.oribuin.lilori.util

import java.io.File

object FileUtils {
    @JvmStatic
    fun createFile(directory: String, fileName: String): File {
        val dir = File(directory)
        val file = File(dir, fileName)

        if (!file.exists()) {
            if (!dir.exists()) {
                dir.mkdirs()
            }

            file.createNewFile()
        }

        return file
    }

    @JvmStatic
    fun createFile(file: File): File {

        if (!file.exists()) {
            if (file.parentFile.exists()) {
                file.parentFile.mkdir()
            }

            file.createNewFile()
        }

        return file
    }
}