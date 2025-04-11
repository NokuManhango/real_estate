package app.model;

import java.time.LocalDateTime;

public record Appointment(User client, Property property, LocalDateTime dateTime) {
    @Override
    public String toString() {
        return String.format("%s has an appointment to view '%s' on %s",
                client.name(), property.title(), dateTime);
    }
}