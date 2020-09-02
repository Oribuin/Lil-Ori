package xyz.oribuin.lilori.commands.author

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import org.apache.commons.lang3.StringUtils
import org.apache.commons.net.ftp.FTPClient
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CmdUpdate(bot: LilOri) : Command(bot) {

    init {
        name = "Update"
        category = Category(Category.Type.AUTHOR)
        aliases = emptyList()
        description = "Update the jar file on the latest ."
        arguments = emptyList()
        isOwnerOnly = true
        isEnabled = true
    }

    override fun executeCommand(event: CommandEvent) {

        val args = event.message.contentRaw.split(" ").toTypedArray()

        if (args.size < 2) {
            event.reply("<a:bee:730546474424729712> Please select a correct update type.")
            return
        }

        when (args[1].toLowerCase()) {
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

                client.storeFile("/web/jars.oribuin.xyz/public_html/eternalreports/${file.name}", FileInputStream(file.path))
                println(client.replyCode)
                msg.editMessage("<a:bee:730546474424729712> **Logging out of FTP..**").queue()
                client.logout()
                FileInputStream(file.path).close()
                client.disconnect()

                msg.editMessage("<a:bee:730546474424729712> **Successfully updated ${StringUtils.capitalize(pluginName)}! (https://jars.oribuin.xyz/$pluginName/${file.name})**").queue()
                file.delete()

                event.deleteCmd()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}