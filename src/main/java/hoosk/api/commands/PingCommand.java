package hoosk.api.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if(event.getName().equals("ping")) {
            pingResponse(event);
        }
    }

    public void pingResponse(SlashCommandInteractionEvent event) {
        if(!event.isFromGuild()) {
            event.reply("Sorry, this command can only be used in a server!").setEphemeral(true).queue();
        } else {
            event.reply("PONG!").setEphemeral(true).queue();
        }
    }

}
