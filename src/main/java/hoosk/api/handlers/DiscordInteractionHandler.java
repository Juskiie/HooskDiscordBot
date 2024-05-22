package hoosk.api.handlers;

import java.util.Map;

public class DiscordInteractionHandler {
    public boolean pingPong(Map<String, Object> body) {
        return body.get("type") != null && body.get("type").equals(1);
    }
}