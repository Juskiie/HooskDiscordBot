package hoosk.api.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if(event.getName().equals("ping")) {
            // event.deferReply(true).queue();
            pingResponse(event);
        }
    }

    public void pingResponse(SlashCommandInteractionEvent event) {
        if(!event.isFromGuild()) {
            event.getHook().sendMessage("Sorry, this command can only be used in a server!").setEphemeral(true).queue();
        } else {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        }
    }

}
