package hoosk.discordbot;

import hoosk.api.commands.TicketCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.InvalidParameterException;

public class DiscordBot extends ListenerAdapter {

    public static void main(String[] args) throws InterruptedException{

        // GET KEY
        // final String TOKEN = System.getenv("DISCORD_BOT");
        String TOKEN = (System.getenv("DISCORD_BOT") != null) ? System.getenv("DISCORD_BOT") : getSecret();

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
