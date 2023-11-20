package hoosk.api.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class RandomNumberCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("rng")) {
            event.deferReply().queue();
            generateNumber(event);
        }
    }

    public void generateNumber(SlashCommandInteractionEvent event) {
        // Defaults
        int min = 0;
        int max = 100;

        OptionMapping minRangeOption = event.getOption("min_range");
        OptionMapping maxRangeOption = event.getOption("max_range");

        if (minRangeOption != null) {
            min = minRangeOption.getAsInt(); // Assuming min_range is a Long type option
        }

        if (maxRangeOption != null) {
            max = maxRangeOption.getAsInt(); // Assuming max_range is a Long type option
        }

        Random rng = new Random();
        int num = rng.nextInt(max - min + 1) + min;
        event.getHook().sendMessage("Generated random number (" + min + "-" + max + "): " + num)
                .setEphemeral(false)
                .queue();
    }
}