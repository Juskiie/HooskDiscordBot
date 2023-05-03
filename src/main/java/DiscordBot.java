import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class DiscordBot extends ListenerAdapter {
    public static void main(String[] args) {
        final String TOKEN = System.getenv("DISCORD_BOT");
        JDA builder = JDABuilder.createLight(TOKEN, Collections.emptyList())
                .addEventListeners(new DiscordBot())
                .setActivity(Activity.playing("Type /ping"))
                .build();

        builder.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("ban", "Ban a user from the server")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)) // only usable with ban permissions
                        .setGuildOnly(true) // Ban command only works inside a guild
                        .addOption(OptionType.USER, "user", "The user to ban", true) // required option of type user (target to ban)
                        .addOption(OptionType.STRING, "reason", "The ban reason"), // optional reason
                Commands.slash("ticket", "Send a support ticket!")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ALL_TEXT_PERMISSIONS))
        ).queue();
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // make sure we handle the right command
        if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        }
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.equalsIgnoreCase("!shutdown")) {
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
