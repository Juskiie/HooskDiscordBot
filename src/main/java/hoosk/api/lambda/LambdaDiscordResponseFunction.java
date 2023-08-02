package hoosk.api.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/*
  Discord requires bots return a ping request back when pinged,
  so this function serves to reply to the Discord API when pinged.
 */
public class LambdaDiscordResponseFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        int type = Integer.parseInt(input.getQueryStringParameters().get("type"));
        if (type == 1) {
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("{\"type\": 1}");
        }
        // return some default response otherwise
        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("{\"message\": \"ok\"}");
    }
}
