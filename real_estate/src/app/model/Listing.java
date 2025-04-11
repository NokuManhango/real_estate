package app.model;

public sealed interface Listing permits RentalListing, SaleListing {}

