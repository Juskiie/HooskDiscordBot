package Hoosk.api.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ShutdownCommand extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.equalsIgnoreCase("!shutdown")) {
            // Check if the user who sent the command is a bot administrator.
            // Replace "YOUR_USER_ID" with the user ID of the bot administrator.
            if (event.getAuthor().getId().equals("280703058214780929")) {
                event.getChannel().sendMessage("Shutting down...").queue();
                event.getJDA().shutdown();
                System.exit(0);
            } else {
                event.getChannel().sendMessage("You do not have permission to shut down the bot.").queue();
            }
        }
    }
}