import java.sql.*;
import java.util.Scanner;

public class AttendanceApp {
    // 1. Database Credentials
    static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    static final String USER = "root";
    static final String PASS = "root"; // <--- UPDATE THIS!

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try { // Moved try outside to handle the loop better
            while (true) {
                System.out.println("\n--- ATTENDANCE TRACKER MENU ---");
                System.out.println("1. Register New Student");
                System.out.println("2. Mark Today's Attendance");
                System.out.println("3. View Attendance Report");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 4) {
                    System.out.println("Exiting...");
                    break; // Use break instead of return to reach sc.close()
                }

                try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter Student Name: ");
                            String name = sc.nextLine();
                            registerStudent(conn, name);
                            break;
                        case 2:
                            System.out.print("Enter Student ID: ");
                            int id = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                            markAttendance(conn, id);
                            break;
                        case 3:
                            System.out.print("Enter Student ID for Report: ");
                            int reportId = sc.nextInt();
                            sc.nextLine(); // Consume the newline character
                            viewReport(conn, reportId);
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (SQLException e) {
                    System.out.println("Database Error: " + e.getMessage());
                }
            }
        } finally {
            sc.close(); // This tells VS Code: "I'm done with the keyboard now!"
            System.out.println("Scanner closed safely.");
        }
    }

    // METHOD: Add a new student to the DB
    public static void registerStudent(Connection conn, String name) throws SQLException {
        String sql = "INSERT INTO students (name) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, name);
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Registered! Student ID is: " + rs.getInt(1));
        }
    }

    // METHOD: Mark a student present for today
    public static void markAttendance(Connection conn, int id) throws SQLException {
        String sql = "INSERT INTO attendance_logs (student_id, date_recorded, status) VALUES (?, CURDATE(), 'Present')";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        System.out.println(" Attendance marked for ID " + id);
    }

    // METHOD: Show total days present
    public static void viewReport(Connection conn, int id) throws SQLException {
        // 1. Query to get both the Student's count and the Total entries in the system
        String sql = "SELECT " +
                "(SELECT name FROM students WHERE id = ?) as student_name, " +
                "(SELECT COUNT(*) FROM attendance_logs WHERE student_id = ?) as present_days, " +
                "(SELECT COUNT(DISTINCT date_recorded) FROM attendance_logs) as total_working_days";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setInt(2, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next() && rs.getString("student_name") != null) {
            String name = rs.getString("student_name");
            int present = rs.getInt("present_days");
            int totalDays = rs.getInt("total_working_days");

            // 2. Calculate Percentage (Avoid division by zero)
            double percentage = (totalDays > 0) ? ((double) present / totalDays) * 100 : 0;

            System.out.println("\n--- ATTENDANCE ANALYSIS FOR " + name.toUpperCase() + " ---");
            System.out.println("Total Working Days: " + totalDays);
            System.out.println("Days Present:       " + present);
            System.out.printf("Attendance Rate:    %.2f%%\n", percentage);

            // 3. Add a "Status" message for extra credit!
            if (percentage < 75) {
                System.out.println("Status:  SHORTAGE (Below 75%)");
            } else {
                System.out.println("Status: ELIGIBLE");
            }
        } else {
            System.out.println("Student ID not found.");
        }
    }
}