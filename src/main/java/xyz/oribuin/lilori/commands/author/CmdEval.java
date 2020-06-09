package xyz.oribuin.lilori.commands.author;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import xyz.oribuin.lilori.managers.command.Command;
import xyz.oribuin.lilori.managers.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CmdEval extends Command {
    public CmdEval() {
        this.name = "Eval";
        this.description = "An evaluation.";
    }

    @Override
    public void executeCommand(CommandEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (args.length == 1) {
            event.reply(event.getAuthor().getAsMention() + ", Please include some code to evaluate.");
            return;
        }

        final String eval = event.getMessage().getContentRaw().substring(args[0].length() + 1);

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("bot", event.getJDA());
        engine.put("event", event);
        engine.put("guild", event.getGuild());
        engine.put("message", event.getMessage());
        engine.put("channel", event.getChannel());
        engine.put("voice", event.getGuild().getSelfMember().getVoiceState());
        engine.put("watching", Activity.watching(eval));
        engine.put("playing", Activity.playing(eval));

        for (Permission permission : Permission.values())
            engine.put(permission.getName(), permission);

        for (OnlineStatus value : OnlineStatus.values())
            engine.put(value.name(), value);

        for (Activity.ActivityType activityType : Activity.ActivityType.values())
            engine.put(activityType.name(), activityType);


        String error = null;

        try {
            engine.eval(eval);
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
