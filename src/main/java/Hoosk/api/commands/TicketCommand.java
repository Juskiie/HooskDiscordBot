package Hoosk.api.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
// import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketCommand extends ListenerAdapter {
    private AtomicInteger ticketCount = new AtomicInteger(1);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket")) {
            createTicketChannel(event);
        }
    }

    private void createTicketChannel(@NotNull SlashCommandInteractionEvent event) {
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

        if (server != null && member != null) {
            server.createTextChannel("ticket-" + ticketNum)
                    .addPermissionOverride(server.getPublicRole(), null, denyList)
                    .addPermissionOverride(member, allowList, null)
                    .queue(textChannel -> {
                        Button closeTicketButton = Button.danger("close_ticket", "Close Ticket");

                        textChannel.sendMessage(member.getAsMention() + ", here's your support ticket channel!")
                                .setActionRow(closeTicketButton)
                                .queue();

                        event.reply("Created a ticket channel: " + textChannel.getAsMention()).queue();
                    });
        }
    }

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
}
