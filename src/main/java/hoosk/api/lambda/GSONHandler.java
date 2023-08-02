package hoosk.api.lambda;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hoosk.api.handlers.DiscordInteractionHandler;

import javax.naming.Context;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GSONHandler {
    public void handler(InputStream inputStream, OutputStream outputStream, Context context) {
        // Parse input
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Gson gson = new Gson();
        Map<String, Object> body = gson.fromJson(reader, type);

        // Call the pingPong method
        boolean response = new DiscordInteractionHandler().pingPong(body);

        // TODO: Write the response to the outputStream
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        // Define your response object
        Map<String, Object> responseA = new HashMap<>();
        responseA.put("type", 1);
    }
}
