package xyz.oribuin.lilori.commands.games;

import xyz.oribuin.lilori.managers.commands.command.Command;
import xyz.oribuin.lilori.managers.commands.command.CommandEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CmdCoinflip extends Command {

    public CmdCoinflip() {
        this.name = "Coinflip";
        this.category = new Category("Games");
        this.aliases = new String[]{"flip"};
        this.arguments = "";
        this.help = "Flip a coin.";
    }

    @Override
    protected void execute(CommandEvent event) {
        int lowerInt = 1;
        int higherInt = 100;
        event.getChannel().sendMessage(":moneybag: Flipping...").queue(message -> {
            int randomInt = new Random().nextInt(higherInt - lowerInt + 1) + lowerInt;

            if (randomInt > 50)
                message.editMessage(":moneybag: You landed on **Heads**.").queueAfter(2, TimeUnit.SECONDS);
            else
                message.editMessage(":moneybag: You landed on **Tails**.").queueAfter(2, TimeUnit.SECONDS);
        });

    }
}
