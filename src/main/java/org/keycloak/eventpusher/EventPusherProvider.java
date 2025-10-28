package org.keycloak.eventpusher;


import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

public class EventPusherProvider implements EventListenerProvider {

	private final EventSender sender;

	public EventPusherProvider(EventSender sender) {
		this.sender = sender;
	}

	@Override
	public void onEvent(Event event) {
		sender.send(event);
	}

	@Override
	public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
		// Optionally handle admin events if needed
	}

	@Override
	public void close() {
	}
}