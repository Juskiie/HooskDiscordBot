package hoosk.discordbot;

import hoosk.api.commands.PingCommand;
import hoosk.api.commands.RandomNumberCommand;
import hoosk.api.commands.TicketCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import org.json.JSONObject;

import java.util.Collections;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

/**
 * Initializes a bot instance.
 */
public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) throws InterruptedException{

        // GET KEY
        // If running local should get from environment variable, otherwise use secure secrets manager
        String TOKEN = (System.getenv("DISCORD_BOT") != null) ? System.getenv("DISCORD_BOT") : getSecret();

        JDA builder = JDABuilder.createLight(TOKEN, Collections.emptyList())
                .addEventListeners(new DiscordBot())
                .setActivity(Activity.playing("Type /ticket"))
                .build()
                .awaitReady();

        builder.updateCommands().addCommands(
                Commands.slash("ping", "Calculate ping of the bot"),
                Commands.slash("ticket", "Send a support ticket!"),
                Commands.slash("rng", "Generate a random number!")
        ).queue();

        builder.addEventListener(new TicketCommand());
        builder.addEventListener(new PingCommand());
        builder.addEventListener(new RandomNumberCommand());

    }

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
            e.printStackTrace();
            throw e;
        }

        String secretString = getSecretValueResponse.secretString();

        // Convert the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(secretString);

        return jsonObject.getString("DiscordBotToken");
    }
}
