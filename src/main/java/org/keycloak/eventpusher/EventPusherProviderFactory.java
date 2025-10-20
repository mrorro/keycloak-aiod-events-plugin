package org.example.keycloak.eventpusher;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class EventPusherProviderFactory implements EventListenerProviderFactory {

    private String endpointUrl;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new EventPusherProvider(new EventSender(endpointUrl));
    }

    @Override
    public void init(Config.Scope config) {
        endpointUrl = config.get("endpointUrl", "http://localhost:8080/api/events");
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
