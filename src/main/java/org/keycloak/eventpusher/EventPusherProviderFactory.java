package org.keycloak.eventpusher;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class EventPusherProviderFactory implements EventListenerProviderFactory {

    private String endpointUrl;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new EventPusherProvider(new EventSender(endpointUrl));
    }

    @Override
    public void init(Config.Scope config) {
        endpointUrl = loadFromFile();
    
    // Allow Keycloak SPI override (takes precedence)
        String configured = config.get("endpointUrl");
        if (configured != null && !configured.isBlank()) {
            endpointUrl = configured;
        }

        // Fallback default if nothing else found
        if (endpointUrl == null || endpointUrl.isBlank()) {
            endpointUrl = "http://localhost:8080/api/events";
        }
        //System.out.println("[EventPusher] Using endpoint URL: " + endpointUrl);
    }

    private String loadFromFile() {
        String[] possiblePaths = {
            "/opt/keycloak/data/eventpusher.properties", 
            "/opt/keycloak/conf/eventpusher.properties",
            "conf/eventpusher.properties"
        };
    for (String path : possiblePaths) {
            try (InputStream input = new FileInputStream(path)) {
                Properties props = new Properties();
                props.load(input);
                String url = props.getProperty("endpointUrl");
                if (url != null && !url.isBlank()) {
                    System.out.println("[EventPusher] Loaded endpoint URL from: " + path);
                    return url.trim();
                }
            } catch (IOException ignored) { }
        }

        return null; // None found
    }



    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "event-pusher";
    }
}
