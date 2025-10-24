package org.example.keycloak.eventpusher;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EventSender {

    private final String endpointUrl;

    public EventSender(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public void send(Event event) {
        EventType type = event.getType();
        if (type != EventType.LOGIN && type != EventType.REGISTER) {
            return; // Ignore other events
        }

        String json = toJson(event);
        System.out.println("[USER EVENT] Sending to " + endpointUrl + ": " + json);

    }

    private String toJson(Event event) {
        Map<String, String> details = event.getDetails();

        String type = event.getType() != null ? event.getType().toString() : "UNKNOWN";
        String realm = event.getRealmId();
        String client = event.getClientId();
        String user = event.getUserId();
        String redirectUri = details != null ? details.getOrDefault("redirect_uri", "") : "";
        String username = details != null ? details.getOrDefault("username", "") : "";
        long time = event.getTime();

        Map<String, String> utmParams = extractUtmParams(redirectUri);

        StringBuilder utmJson = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> e : utmParams.entrySet()) {
            if (!first) utmJson.append(", ");
            utmJson.append(String.format("\"%s\": \"%s\"", escape(e.getKey()), escape(e.getValue())));
            first = false;
        }
        utmJson.append("}");


        // Build minimal JSON manually (no dependencies)
        return String.format(
            "{ \"type\": \"%s\", \"realm\": \"%s\", \"client\": \"%s\", \"user\": \"%s\", \"username\": \"%s\", \"time\": %d, \"utm\": %s }",
            escape(type), escape(realm), escape(client), escape(user), escape(username), time, utmJson
        );
    }

    private Map<String, String> extractUtmParams(String uri) {
        Map<String, String> utms = new HashMap<>();
        if (uri == null || !uri.contains("?")) return utms;

        String query = uri.substring(uri.indexOf('?') + 1);
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].startsWith("utm_")) {
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                utms.put(key, value);
            }
        }
        return utms;
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\""); // simple escaping for quotes
    }
}