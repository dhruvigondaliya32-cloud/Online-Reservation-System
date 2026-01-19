import java.sql.*;
import java.util.Scanner;
import java.util.Random;

public class Task1 {
    private static final int min = 1000;
    private static final int max = 9999;

    public static class User {
        private String username;
        private String password;
        Scanner scanner = new Scanner(System.in);

        public String getUserName() {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            return username;
        }

        public String getPassword() {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            return password;
        }
    }

    public static class PnrRecord {
        private int pnrNumber;
        private String passengerName;
        private String trainNumber;
        private String classType;
        private String journeyDate;
        private String start;
        private String end;
        Scanner scanner = new Scanner(System.in);

        public int getPnrNumber() {
            Random r = new Random();
            pnrNumber = r.nextInt(max - min + 1) + min;
            return pnrNumber;
        }

        public String getPassengerName() {
            System.out.print("Enter passenger name: ");
            passengerName = scanner.nextLine();
            return passengerName;
        }

        public String getTrainNumber() {
            System.out.print("Enter train number: ");
            trainNumber = scanner.nextLine();
            return trainNumber;
        }

        public String getClassType() {
            System.out.print("Enter class type: ");
            classType = scanner.nextLine();
            return classType;
        }

        public String getJourneyDate() {
            System.out.print("Enter journey date (DD-MM-YYYY): ");
            journeyDate = scanner.nextLine();
            return journeyDate;
        }

        public String getStart() {
            System.out.print("Enter starting place: ");
            start = scanner.nextLine();
            return start;
        }

        public String getEnd() {
            System.out.print("Enter ending place: ");
            end = scanner.nextLine();
            return end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User u1 = new User();

        String username = u1.getUserName();
        String password = u1.getPassword();
        String url = "jdbc:mysql://localhost:3306/reservationdb";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                System.out.println(" User connection granted.\n");

                boolean q = true;
                while (q) {
                    System.out.println("\nEnter your choice:");
                    System.out.println("1. Insert record");
                    System.out.println("2. Delete record by PNR");
                    System.out.println("3. Show all records");
                    System.out.println("4. Exit");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 1:
                            PnrRecord p1 = new PnrRecord();
                            int pnr = p1.getPnrNumber();
                            String passengerName = p1.getPassengerName();
                            String trainNumber = p1.getTrainNumber();
                            String classType = p1.getClassType();
                            String journeyDate = p1.getJourneyDate();
                            String start = p1.getStart();
                            String end = p1.getEnd();

                            String insertQuery = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
                                ps.setInt(1, pnr);
                                ps.setString(2, passengerName);
                                ps.setString(3, trainNumber);
                                ps.setString(4, classType);
                                ps.setString(5, journeyDate);
                                ps.setString(6, start);
                                ps.setString(7, end);

                                int rows = ps.executeUpdate();
                                System.out.println(rows > 0 ? " Record added successfully." : " No record added.");
                            } catch (SQLException e) {
                                System.out.println("SQLException: " + e.getMessage());
                            }
                            break;

                        case 2:
                            System.out.print("Enter PNR number to delete: ");
                            int delPNR = scanner.nextInt();
                            scanner.nextLine();

                            String deleteQuery = "DELETE FROM reservations WHERE pnr_number = ?";
                            try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
                                ps.setInt(1, delPNR);
                                int rows = ps.executeUpdate();
                                System.out.println(rows > 0 ? " Record deleted." : " No record found.");
                            } catch (SQLException e) {
                                System.out.println("SQLException: " + e.getMessage());
                            }
                            break;

                        case 3:
                            String showQuery = "SELECT * FROM reservations";
                            try (PreparedStatement ps = connection.prepareStatement(showQuery);
                                 ResultSet rs = ps.executeQuery()) {
                                System.out.println("\n All Reservations:");
                                while (rs.next()) {
                                    System.out.println("PNR: " + rs.getInt("pnr_number"));
                                    System.out.println("Passenger: " + rs.getString("passengerName"));
                                    System.out.println("Train: " + rs.getString("trainNumber"));
                                    System.out.println("Class: " + rs.getString("classType"));
                                    System.out.println("Date: " + rs.getString("journeyDate"));
                                    System.out.println("From: " + rs.getString("started"));
                                    System.out.println("To: " + rs.getString("ended"));
                                    System.out.println("---------------------------");
                                }
                            } catch (SQLException e) {
                                System.out.println("SQLException: " + e.getMessage());
                            }
                            break;

                        case 4:
                            System.out.println(" Exiting program.");
                            q = false;
                            break;

                        default:
                            System.out.println(" Invalid choice.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading JDBC driver: " + e.getMessage());
        }
    }
}