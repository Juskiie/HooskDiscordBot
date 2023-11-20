package hoosk.api.commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiceRollCommand extends ListenerAdapter {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("roll")) {
            event.deferReply().queue();
            rollDice(event);
        }
    }

    public void rollDice(SlashCommandInteractionEvent event) {
        OptionMapping diceSidesOption = event.getOption("sides");
        OptionMapping rollTimesOption = event.getOption("times");
        int diceSides;
        int rollTimes;

        if (diceSidesOption == null) {
            event.getHook().sendMessage("You need to specify the type of dice to roll.").setEphemeral(true).queue();
            return;
        }
        else {
            if (diceSidesOption.getAsInt() < 4) {
                diceSides = 4;
                event.getHook().sendMessage("Smallest rollable dice is a d4, setting to 4.").setEphemeral(true).queue();
            } else {
                diceSides = diceSidesOption.getAsInt();
            }
        }

        if (rollTimesOption != null) {
            rollTimes = rollTimesOption.getAsInt();
            if(rollTimes > 10) {
                rollTimes = 10;
                event.getHook().sendMessage("Maximum rolls per command is capped at 10.").setEphemeral(true).queue();
            }
        } else {
            rollTimes = 1;
        }

        // event.reply("Rolling d" + diceSides + " " + rollTimes + " times: " + Arrays.toString(results)).queue();
        int finalRollTimes = rollTimes;
        TextChannel channel = (TextChannel) event.getChannel();
        int finalDiceSides1 = diceSides;
        event.getHook().sendMessage("*Rolling d" + diceSides + " " + rollTimes + " times*").queue(response -> {
            for (int roll = 1; roll <= finalRollTimes; roll++) {
                final int finalRoll = roll;
                final int finalDiceSides = finalDiceSides1;
                executorService.schedule(() -> sendRollResult(channel, finalRoll, rollResult(finalDiceSides), finalDiceSides), finalRoll * 1500L, TimeUnit.MILLISECONDS);
            }
        });
    }

    private void sendRollResult(TextChannel channel, int roll, int result, int sides) {
        channel.sendMessage("**Roll " + roll + " [d" + sides + "]**: " + result).queue();
    }

    public int rollResult(int sides) {
        Random rng = new Random();
        return rng.nextInt(1, sides);
    }
}
