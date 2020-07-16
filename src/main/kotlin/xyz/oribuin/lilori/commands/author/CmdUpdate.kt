package xyz.oribuin.lilori.commands.author

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import org.apache.commons.lang3.StringUtils
import org.apache.commons.net.ftp.FTPClient
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.io.File
import java.io.FileInputStream
import java.util.*

class CmdUpdate : Command() {

    init {
        name = "Update"
        aliases = emptyList()
        description = "Update the jar file on the latest ."
        arguments = emptyList()
        isOwnerOnly = true
    }

    override fun executeCommand(event: CommandEvent) {

        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            event.reply("<a:bee:730546474424729712> Please select a correct update type.")
            return
        }

        when (args[1].toLowerCase()) {
            "jar", "bot" -> {
                this.updateJar(event)
            }

            "plugin" -> {

                if (event.message.attachments.size == 0) {
                    event.reply("<a:bee:730546474424729712> Please include a jar file to upload.")
                    return
                }

                this.updatePlugin(event, args[2].toLowerCase())
            }
        }
    }

    private fun updatePlugin(event: CommandEvent, pluginName: String) {
        val client = FTPClient()
        val fileName = event.message.attachments[0].fileName
        val file = File("plugins", fileName)

        // Download file
        event.channel.sendMessage("<a:bee:730546474424729712> **Starting to update of plugin ${StringUtils.capitalize(pluginName)} file.**").queue { msg ->
            try {

                msg.editMessage("<a:bee:730546474424729712> **Downloading jar file onto bot!.**").queue()

                event.message.attachments[0].downloadToFile(file)

                msg.editMessage("<a:bee:730546474424729712> **Logging into website FTP!**").queue()
                client.connect(Settings.FTP_URL)
                client.login(Settings.FTP_USERNAME, Settings.FTP_PASSWORD)

                msg.editMessage("<a:bee:730546474424729712> **Storing $fileName into website FTP.**").queue()
                client.storeFile("jars.oribuin.xyz/$pluginName/${file.name}", FileInputStream(file.path))

                msg.editMessage("<a:bee:730546474424729712> **Logging out of FTP..**").queue()
                client.logout()
                FileInputStream(file.path).close()
                client.disconnect()

                msg.editMessage("<a:bee:730546474424729712> **Successfully updated ${StringUtils.capitalize(pluginName)}! (https://jars.oribuin.xyz/$pluginName/${file.name})**").queue()
                file.delete()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun updateJar(event: CommandEvent) {
        event.channel.sendMessage("<a:bee:730546474424729712> **Starting to update JAR file.**").queue { msg ->
            try {
                val file = File("build/libs", "LilOri.jar")
                // Edit message
                msg.editMessage("<a:bee:730546474424729712> **Logging into SFTP.**").queue()

                // Lesson of the day: Logging into SFTP is a little bitch
                val jsch = JSch()

                // Get the session
                val session = jsch.getSession(Settings.JAR_FTP_USERNAME, Settings.JAR_FTP_URL, 2022)
                session.setPassword(Settings.JAR_FTP_PASSWORD)

                // Config stuff i don't understand
                val properties = Properties()
                properties.setProperty("StrictHostKeyChecking", "no")
                session.setConfig(properties)

                // Connect stuff
                session.connect()

                val channel = session.openChannel("sftp")
                val sftp = channel as ChannelSftp
                sftp.connect()


                // More messages :)
                msg.editMessage("<a:bee:730546474424729712> **Uploading new LilOri.jar File.**").queue()

                // Save file
                sftp.put(FileInputStream(file), file.name)
                msg.editMessage("<a:bee:730546474424729712> **Logging out of SFTP.**").queue()

                // Logout
                sftp.disconnect()
                session.disconnect()

                msg.editMessage("<a:bee:730546474424729712> **Successfully updated Jar File! Restart bot to see the changes.**").queue()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}