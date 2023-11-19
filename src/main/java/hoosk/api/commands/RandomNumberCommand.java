package hoosk.api.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Random;

public class RandomNumberCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals("rng")) {
            generateNumber(event);
        }
    }

    public void generateNumber(SlashCommandInteractionEvent event) {
        Random rng = new Random();
        int num = rng.nextInt(100);
        event.reply("Generated random number (0-100): " + String.valueOf(num)).setEphemeral(false).queue();
    }
}
