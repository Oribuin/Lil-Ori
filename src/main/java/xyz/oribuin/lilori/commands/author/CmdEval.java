package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdEval extends Command {
    public CmdEval() {
        this.name = "Eval";
        this.description = "An evaluation.";
        this.category = new Category("Test");
        this.arguments = "<Code>";
        this.hidden = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1) {
            event.reply(event.getAuthor().getAsMention() + ", Please include some code to evaluate.");
            return;
        }

        final String eval = event.getMessage().getContentRaw().substring(args[0].length() + 1);

        ScriptEngine se = new ScriptEngineManager().getEngineByName("nashorn");
        se.put("bot", event.getJDA());
        se.put("event", event);
        se.put("guild", event.getGuild());
        se.put("message", event.getMessage());
        se.put("channel", event.getChannel());
        se.put("voice", event.getSelfMember().getVoiceState());
        se.put("watching", Activity.watching(eval));
        se.put("playing", Activity.playing(eval));

        for (Permission permission : Permission.values())
            se.put(permission.getName(), permission);

        for (OnlineStatus value : OnlineStatus.values())
            se.put(value.name(), value);

        for (Activity.ActivityType activityType : Activity.ActivityType.values())
            se.put(activityType.name(), activityType);

        String error = null;

        try {
            se.eval(eval);
        } catch (ScriptException e) {
            error = e.getMessage();
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor("Lil' Ori Evaluation")
                .setColor(Color.decode("#33539e"))
                .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                .setDescription("**Eval:**```java\n" + eval + "```\n**Exception:**\n" + error);

        event.deleteCmd();
        event.timedReply(embedBuilder.build(), 5, TimeUnit.SECONDS);
    }
}
