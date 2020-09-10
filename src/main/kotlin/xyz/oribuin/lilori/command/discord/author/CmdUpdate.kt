package xyz.oribuin.lilori.command.discord.author

import org.apache.commons.lang3.StringUtils
import org.apache.commons.net.ftp.FTPClient
import xyz.oribuin.lilori.LilOri
import xyz.oribuin.lilori.Settings
import xyz.oribuin.lilori.handler.Category
import xyz.oribuin.lilori.handler.Command
import xyz.oribuin.lilori.handler.CommandEvent
import xyz.oribuin.lilori.util.BotUtils
import java.io.File
import java.io.FileInputStream

class CmdUpdate(bot: LilOri) : Command(bot) {

    init {
        name = "Update"
        category = Category(Category.Type.AUTHOR)
        aliases = emptyList()
        description = "Update the jar file on the latest ."
        arguments = listOf("plugin", "<plugin>")
        isOwnerOnly = true
        isEnabled = true
    }

    override fun executeCommand(event: CommandEvent) {


        if (event.args.size < 2) {
            event.sendEmbedReply("❗ Invalid Arguments", "The correct usage is ${event.prefix}${name.toLowerCase()} ${arguments?.let { BotUtils.formatList(it) }}")
            return
        }

        when (event.args[1].toLowerCase()) {
            "plugin" -> {

                // Require a jar file
                if (event.message.attachments.size == 0) {
                    event.sendEmbedReply("❗ No Jar File Attached", "Please include the jar file you want to update.")
                    return
                }

                this.updatePlugin(event, event.args[2].toLowerCase())
            }
        }
    }

    private fun updatePlugin(event: CommandEvent, pluginName: String) {
        val client = FTPClient()
        val fileName = event.message.attachments[0].fileName
        val file = File("plugins", fileName)

        event.channel.sendMessage("<a:bee:730546474424729712> **Starting to update of plugin ${StringUtils.capitalize(pluginName)} file.**").queue { msg ->
            try {

                // Download the file into the bot
                msg.editMessage("<a:bee:730546474424729712> **Downloading jar file onto bot!.**").queue()

                event.message.attachments[0].downloadToFile(file)

                // Log into website
                msg.editMessage("<a:bee:730546474424729712> **Logging into website FTP!**").queue()
                client.connect(Settings.FTP_URL)
                client.login(Settings.FTP_USERNAME, Settings.FTP_PASSWORD)

                // Store the file into the folder required
                msg.editMessage("<a:bee:730546474424729712> **Storing $fileName into website FTP.**").queue()
                client.storeFile("/web/jars.oribuin.xyz/public_html/${pluginName}/${file.name}", FileInputStream(file.path))

                // Logout the ftp
                msg.editMessage("<a:bee:730546474424729712> **Logging out of FTP..**").queue()
                FileInputStream(file.path).close()
                client.logout()
                client.disconnect()

                // Send the update message
                msg.editMessage("<a:bee:730546474424729712> **Successfully updated ${StringUtils.capitalize(pluginName)}! (https://jars.oribuin.xyz/$pluginName/${file.name})**").queue()
                file.delete()
                event.deleteCmd()

            } catch (ex: Exception) {
                event.sendEmbedReply("❗ An exception has occured", "Here is the exception message.\n \n${ex.message}")
                ex.printStackTrace()
            }
        }
    }
}