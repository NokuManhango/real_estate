package app;

import app.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static List<Property> properties = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static User currentUser;

    public static void main(String[] args) {
        seedData();
        login();
        mainMenu();
    }

    // Seed sample data
    private static void seedData() {
        properties = new ArrayList<>(List.of(
            new Property("P001", "2 Bed Apartment", "Nice view", 1500.0, PropertyType.RENTAL,
                    new Address("Dublin", "Ireland", "Main St", "D01"), LocalDate.now(), true),
            new Property("P002", "3 Bed House", "Family home", 250000.0, PropertyType.SALE,
                    new Address("Galway", "Ireland", "Park Rd", "G02"), LocalDate.now().minusDays(10), true),
            new Property("P003", "1 Bed Flat", "Student friendly", 1800.0, PropertyType.RENTAL,
                    new Address("Limerick", "Ireland", "College Rd", "L03"), LocalDate.now().minusDays(3), false)
        ));

        users = List.of(
            new User("U001", "Alice", Role.ADMIN, "alice@google.com", "Admin123"),
            new User("U002", "Bob", Role.AGENT, "bob@google.com", "Agent123"),
            new User("U003", "Charlie", Role.CLIENT, "charlie@google.com", "Client123")
        );
    }

    // Handle login logic with email/password validation using regex
    private static void login() {
        System.out.println("\n--- LOGIN ---");
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (!isValidEmail(email) || !isValidPassword(password)) {
                System.out.println("❌ Invalid format for email or password.");
                continue;
            }

            Optional<User> match = users.stream()
                    .filter(u -> u.email().equalsIgnoreCase(email) && u.password().equals(password))
                    .findFirst();

            if (match.isPresent()) {
                currentUser = match.get();
                System.out.println("✅ Logged in as " + currentUser.name());
                break;
            } else {
                System.out.println("❌ Invalid credentials. Try again.");
            }
        }
    }

    // Email format validation
    private static boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email);
    }

    // Password must contain lowercase, uppercase, digit, and be 6+ characters
    private static boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", password);
    }

    // Layered main menu
    private static void mainMenu() {
        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            switch (currentUser.role()) {
                case ADMIN -> System.out.println("👑 Admin access granted.");
                case AGENT -> System.out.println("🏢 Agent access granted.");
                case CLIENT -> System.out.println("👤 Client access granted.");
            }

            System.out.println("1. Properties");
            System.out.println("2. Users");
            System.out.println("3. Simulated Tasks");
            System.out.println("4. File & Localization");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> propertyMenu();
                case "2" -> userMenu();
                case "3" -> simulateTasks();
                case "4" -> fileAndLocalization();
                case "5" -> System.exit(0);
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void propertyMenu() {
        while (true) {
            System.out.println("\n--- PROPERTY MENU ---");
            System.out.println("1. View All");
            System.out.println("2. Pattern Match Demo");
            System.out.println("3. Filter by City & Sort by Price");
            System.out.println("4. Back");
            System.out.print("Choose: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> properties.forEach(System.out::println);
                case "2" -> {
                    Listing listing = new RentalListing(1000, 12);
                    demoPatternMatching(listing);
                    listing = new SaleListing(true);
                    demoPatternMatching(listing);
                }
                case "3" -> {
                    System.out.print("Enter city to filter: ");
                    String city = scanner.nextLine();
                    properties.stream()
                        .filter(p -> p.address().city().equalsIgnoreCase(city))
                        .sorted(Comparator.comparing(Property::price))
                        .forEach(System.out::println);
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("Try again");
            }
        }
    }

    private static void demoPatternMatching(Listing listing) {
        switch (listing) {
            case RentalListing r ->
                System.out.println("Rental - Deposit: " + r.getDeposit() + ", Lease: " + r.getLeaseMonths() + " months");
            case SaleListing s ->
                System.out.println("Sale - Mortgage Available: " + s.isMortgageAvailable());
        }
    }


    private static void userMenu() {
        System.out.println("\n--- USERS ---");
        users.forEach(u -> System.out.println(u.name() + " (" + u.role() + ") - Email: " + u.email()));
    }

    private static void simulateTasks() {
        ExecutorService service = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = List.of(
            () -> "📩 Messages loaded for Alice",
            () -> "🏡 Recommendations fetched for Bob",
            () -> "📞 Leads processed for Charlie"
        );
        try {
            System.out.println("\n--- Simulated Concurrent Tasks ---");
            List<Future<String>> results = service.invokeAll(tasks);
            for (Future<String> f : results) System.out.println(f.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.shutdown();
        }
    }

    private static void fileAndLocalization() {
        try {
            Locale userLocale = Locale.getDefault();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(userLocale);

            Path path = Paths.get("properties_output.txt");
            Files.write(path, properties.stream()
                    .map(p -> p.toString().replaceFirst("(\\d+\\.\\d{2}) EUR", currencyFormatter.format(p.price())))
                    .collect(Collectors.toList()));

            System.out.println("\n✅ Property data saved to " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}
