package hoosk.api.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class ApexRandomLegendPickerCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("apexlegend")) {
            event.deferReply().queue();
            pickRandomLegend(event);
        }
    }
    public void pickRandomLegend(SlashCommandInteractionEvent event) {
        if(!event.isFromGuild()) {
            event.getHook().sendMessage("Sorry, please use this in a server.").setEphemeral(true).queue();
        }

        String[] Legends = {"Bloodhound", "Gibraltar", "Lifeline",
                "Pathfinder", "Wraith", "Bangalore",
                "Caustic", "Mirage", "Octane",
                "Wattson", "Crypto", "Revenant",
                "Loba", "Rampart", "Horizon",
                "Fuse", "Valkyrie", "Seer",
                "Ashe", "Alter", "Maggie",
                "Newcastle", "Vantage", "Catalyst", "Conduit"};

        // Get voice chat channel for apex
        Guild guild = event.getGuild();

        TextChannel textChannel = (TextChannel) event.getChannel();
        assert guild != null;
        String guildid = guild.getId();

        try {
            VoiceChannel voiceChannel = guild.getVoiceChannelsByName("apex", true).get(0);
            System.out.println(guild.getVoiceChannels());
            assert voiceChannel != null;
            List<Member> members = voiceChannel.getMembers();
            System.out.println(voiceChannel);
            System.out.println(voiceChannel.getMembers());
            if (members.isEmpty()) {
                event.getHook().sendMessage("Sorry, I can't see anyone in the 'apex' voice channel.").setEphemeral(true).queue();
                return;
            }
            ArrayList<String> legendsList = new ArrayList<>(Arrays.asList(Legends));
            Collections.shuffle(legendsList);

            StringBuilder sb = new StringBuilder();
            for(int i=0; i<members.size(); i++){
                Member member = members.get(i);
                String legend = legendsList.get(i);
                sb.append("[").append(member.getAsMention()).append("]: ").append(legend).append("\n");
            }

            if (sb.isEmpty()) {
                event.getHook().sendMessage("No members found in the 'apex' voice channel.").setEphemeral(true).queue();
            } else {
                event.getHook().sendMessage(sb.toString()).queue();
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Voice channel was not found.");
            event.getHook().sendMessage("Sorry, I could not find the 'apex' voice channel.").queue();
        } catch (Exception e){
            System.out.println("Something else has gone horribly wrong..");
            e.printStackTrace();
            event.getHook().sendMessage("Sorry, I had an internal error. Yell at Juskie.").queue();
        }
    }
}

