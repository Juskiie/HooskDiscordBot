import Hoosk.api.commands.TicketCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Collections;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) throws InterruptedException{
        final String TOKEN = System.getenv("DISCORD_BOT");
        JDA builder = JDABuilder.createLight(TOKEN, Collections.emptyList())
                .addEventListeners(new DiscordBot())
                .setActivity(Activity.playing("Type /ticket"))
                .build()
                .awaitReady();

        builder.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("ticket", "Send a support ticket!")
        ).queue();

        builder.addEventListener(new TicketCommand());
    }

    @SuppressWarnings("unused")
    public static String getSecret() {
        String secretName = "hooskbot/api/key";
        Region region = Region.of("eu-west-2");

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            // For a list of exceptions thrown, see
            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
            e.printStackTrace();
            throw e;
        }

        return getSecretValueResponse.secretString();
    }
}
