package app.model;

public final class SaleListing implements Listing {
    private final boolean mortgageAvailable;

    public SaleListing(boolean mortgageAvailable) {
        this.mortgageAvailable = mortgageAvailable;
    }

    public boolean isMortgageAvailable() {
        return mortgageAvailable;
    }
}