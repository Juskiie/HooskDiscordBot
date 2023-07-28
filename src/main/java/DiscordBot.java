import Hoosk.api.commands.TicketCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Collections;

public class DiscordBot extends ListenerAdapter {
    public static void main(String[] args) throws InterruptedException {
        final String TOKEN = System.getenv("DISCORD_BOT");
        JDA builder = JDABuilder.createLight(TOKEN, Collections.emptyList())
                .addEventListeners(new DiscordBot())
                .setActivity(Activity.playing("Type /ticket"))
                .build()
                .awaitReady();

        builder.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("ticket", "Send a support ticket!")
        ).queue();

        builder.addEventListener(new TicketCommand());
    }
}
