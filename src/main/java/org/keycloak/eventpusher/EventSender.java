package org.example.keycloak.eventpusher;

import org.keycloak.events.Event;

public class EventSender {

    private final String endpointUrl;

    public EventSender(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public void send(Event event) {
        // Logic to send the event to the specified endpoint URL
        // This could involve making an HTTP POST request with the event data
        System.out.println("Sending event to " + endpointUrl + ": " + event);
        // Actual implementation would go here
    }
}