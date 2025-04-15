package hoosk.api.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Probably going to remove this soon.
 */
public class ApexRandomLegendPickerCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("apexlegend")) {
            event.deferReply().queue();
            pickRandomLegend(event);
        }
    }


    /**
     * Main command handler.
     * @param event SlashCommandInteractionEvent
     */
    public void pickRandomLegend(SlashCommandInteractionEvent event) {
        if(!event.isFromGuild()) {
            event.getHook().sendMessage("Sorry, please use this in a server.").setEphemeral(true).queue();
        }

        ArrayList<String> legendsList = getLegendsList();
        Collections.shuffle(legendsList);

        // Where stuff could potentially go wrong, channels cant be found or invalid permissions. If it does go wrong it will be handled here.
        try {
            Guild guild = event.getGuild();
            VoiceChannel voiceChannel = Objects.requireNonNull(guild).getVoiceChannelsByName("apex", true).get(0);
            List<Member> members = voiceChannel.getMembers();
            Member commandSender = event.getMember();

            // After we get the reference to the 'apex' voice chat channel, make sure people are actually in it. If not, treat as solo player.
            if (members.isEmpty() || !(members.contains(commandSender))) {
                pickLegendWithoutChannel(event);
            }

            StringBuilder sb = new StringBuilder();
            for(int i=0; i<members.size(); i++){
                Member member = members.get(i);
                String legend = legendsList.get(i);
                sb.append("[").append(member.getAsMention()).append("]: ").append(legend).append("\n");
            }

            if (sb.isEmpty()) {
                event.getHook().sendMessage("No members found in the 'apex' voice channel.").setEphemeral(true).queue();
            } else {
                event.getHook().sendMessage(sb.toString()).setEphemeral(false).queue();
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Voice channel was not found.");
            event.getHook().sendMessage("Sorry, I could not find the 'apex' voice channel.").queue();
        } catch (NullPointerException npe) {
            System.out.println("The bot either couldn't find the guild, or a matching channel. See stack trace");
            npe.printStackTrace();
            event.getHook().sendMessage("Sorry, I couldn't find the required resource, report this to Juskie!").queue();
        } catch (Exception e){
            System.out.println("Something else has gone horribly wrong..");
            e.printStackTrace();
            event.getHook().sendMessage("Sorry, I had an internal error. Yell at Juskie.").queue();
        }
    }

    /**
     * Store/retrieve of Apex Legends list.
     * @return The updated list of apex legends.
     */
    @NotNull
    private static ArrayList<String> getLegendsList() {
        String[] Legends = {
                "Alter", "Ash", "Ballistic",
                "Bangalore", "Bloodhound", "Catalyst",
                "Caustic", "Conduit", "Crypto",
                "Fuse", "Gibraltar", "Horizon",
                "Lifeline", "Loba", "Maggie",
                "Mirage", "Newcastle", "Octane",
                "Pathfinder", "Rampart", "Revenant",
                "Seer", "Valkyrie", "Vantage",
                "Wattson", "Wraith"
        };

        return new ArrayList<>(Arrays.asList(Legends));
    }

    public void pickLegendWithoutChannel(SlashCommandInteractionEvent event) {
        ArrayList<String> legendsList = getLegendsList();
        Collections.shuffle(legendsList);
        Random random = new Random();
        int index = random.nextInt(legendsList.size());
        event.getHook().sendMessage("[" + Objects.requireNonNull(event.getMember()).getAsMention() + "]: " + legendsList.get(index)).queue();
    }
}

