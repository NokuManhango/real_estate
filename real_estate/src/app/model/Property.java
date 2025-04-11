package app.model;

import java.time.LocalDate;

public record Property(
    String id,
    String title,
    String description,
    double price,
    PropertyType type,
    Address address,
    LocalDate listedDate,
    boolean available
) {
    @Override
    public String toString() {
        return String.format("%s | %s | %.2f EUR | %s | %s, %s (%s) | Listed: %s | Available: %s",
                id, title, price, type, address.street(), address.city(), address.zipCode(), listedDate, available);
    }
}
