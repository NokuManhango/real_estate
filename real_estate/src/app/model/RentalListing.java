package app.model;

public final class RentalListing implements Listing {
    private final double deposit;
    private final int leaseMonths;

    public RentalListing(double deposit, int leaseMonths) {
        this.deposit = deposit;
        this.leaseMonths = leaseMonths;
    }

    public double getDeposit() {
        return deposit;
    }

    public int getLeaseMonths() {
        return leaseMonths;
    }
}