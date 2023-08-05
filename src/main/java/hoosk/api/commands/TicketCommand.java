package hoosk.api.commands;

import hoosk.api.roles.SupportRole;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TicketCommand extends ListenerAdapter {
    private final AtomicInteger ticketCount = new AtomicInteger(1);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket")) {
            createTicketChannel(event);
        }
    }

    /**
     * When called, the following vars are used:
     * Guild server - The server where the command was issued.
     * Member member - The issuer of the command.
     *
     * @param event The SlashCommandInteractionEvent
     */
    private void createTicketChannel(@NotNull SlashCommandInteractionEvent event) {
        String categoryName = "Tickets";

        if(!event.isFromGuild()) {
            event.reply("Sorry, this command can only be used in a server!").setEphemeral(true).queue();
            return;
        }

        Guild server = event.getGuild();
        Member member = event.getMember();
        int ticketNum = ticketCount.getAndIncrement();

        List<Permission> allowList = new ArrayList<>();
        allowList.add(Permission.VIEW_CHANNEL);
        allowList.add(Permission.MESSAGE_SEND);

        List<Permission> denyList = new ArrayList<>();
        denyList.add(Permission.VIEW_CHANNEL);

        // Creates a text channel, in the "Tickets" category.
        if (server != null && member != null) {
            Category category = CreateCategoryIfNotPresent(server, categoryName);

            if(categoryExists(server, categoryName)){
                ChannelAction<TextChannel> channelAction = server.createTextChannel("ticket-" + ticketNum)
                        .addPermissionOverride(server.getPublicRole(), null, denyList)
                        .addPermissionOverride(member, allowList, null)
                        .setParent(category);

                channelAction.queue(textChannel -> {
                    Button closeTicketButton = Button.danger("close_ticket", "Close Ticket");

                    textChannel.sendMessage(member.getAsMention() + ", here's your support ticket channel!")
                            .setActionRow(closeTicketButton)
                            .queue();

                    getSupportRole(server, supportRole -> textChannel.sendMessage(supportRole.getAsMention() + ", will be here to assist you shortly.")
                            .queue());

                    event.reply("Created a ticket channel: " + textChannel.getAsMention()).setEphemeral(true).queue();
                });
            }
        }
    }

    /**
     * Once the support ticket is resolved and this button is clicked, close the containing channel.
     * @param event The button being clicked
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(event.getComponentId().equals("close_ticket")){
            Member member = event.getMember();

            if (member != null) {
                TextChannel channel = (TextChannel) event.getChannel();
                channel.delete().queue();
            }
        }
    }
    public Category CreateCategoryIfNotPresent(Guild guild, String categoryName) {
        Category category;

        if(!categoryExists(guild, categoryName)) {
            category = guild.createCategory(categoryName).complete();
        } else {
            category = getCategory(guild, categoryName);
        }

        return category;
    }
    public boolean categoryExists(@NotNull Guild guild, String category){
        for (Category cat : guild.getCategories()) {
            if(cat.getName().equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }
    public Category getCategory(@NotNull Guild guild, String category) {
        for (Category cat : guild.getCategories()) {
            if(cat.getName().equalsIgnoreCase(category)) {
                return cat;
            }
        }
        return null;  // if no category is found, return null
    }

    /**
     * Calls the createSupportRole method of the SupportRole class
     * and either creates the role if it doesn't exist, or returns it if it does exist.
     * @param guild The server the command was issued
     */
    public void getSupportRole(Guild guild, Consumer<Role> callback) {
        SupportRole.createSupportRole(guild, callback);
    }

}

