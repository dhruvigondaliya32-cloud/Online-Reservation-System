import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static ArrayList<String> history = new ArrayList<>();

    public static void main(String[] args) {

        double balance = 1000;
        boolean isRunning = true;
        int choice;

        while (isRunning) {
            System.out.println("\n***************");
            System.out.println(" Banking Program ");
            System.out.println("***************");
            System.out.println("1 - Show Balance");
            System.out.println("2 - Deposit");
            System.out.println("3 - Withdraw");
            System.out.println("4 - Exit");
            System.out.println("5 - Transaction History");
            System.out.print("Enter your choice (1-5): ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> showBalance(balance);

                case 2 -> {
                    double dep = deposit();
                    balance += dep;
                    if (dep > 0) {
                        addHistory("Deposit", dep);
                    }
                }

                case 3 -> {
                    double wd = withdraw(balance);
                    if (wd > 0) {
                        balance -= wd;
                        addHistory("Withdraw", wd);
                    }
                }

                case 4 -> isRunning = false;

                case 5 -> showHistory();

                default -> System.out.println("Invalid choice");
            }
        }

        System.out.println("Thank you for using banking program!");
        scanner.close();
    }

    static void showBalance(double balance) {
        System.out.printf("Current Balance: $%.2f\n", balance);
    }

    static double deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return 0;
        }
        return amount;
    }

    static double withdraw(double balance) {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        if (amount > balance) {
            System.out.println("Insufficient funds");
            return -1;
        }
        else if (amount <= 0) {
            System.out.println("Amount must be positive");
            return -1;
        }
        return amount;
    }

    static void addHistory(String type, double amount) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String date = LocalDateTime.now().format(dtf);

        history.add(type + " : $" + amount + " | Date: " + date);
    }

    static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No transaction history found");
        } else {
            System.out.println("\n--- Transaction History ---");
            for (String record : history) {
                System.out.println(record);
            }
        }
    }
}
