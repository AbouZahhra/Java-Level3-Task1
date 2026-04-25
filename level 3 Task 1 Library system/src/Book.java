import java.sql.*;

public class Book {

    // ================= ADD BOOK =================
    public void addBook(String title, String author, int qty) throws Exception {

        String sql = "INSERT INTO books (title, author, quantity, is_deleted) VALUES (?, ?, ?, 0)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, qty);

            ps.executeUpdate();
            System.out.println("Book added successfully.");
        }
    }

    // ================= SHOW BOOKS =================
    public void showBooks() throws Exception {

        String sql = "SELECT * FROM books WHERE is_deleted = 0";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean found = false;

            System.out.println("Available Books:");

            while (rs.next()) {
                found = true;

                System.out.println(
                        rs.getInt("id") + " - " +
                        rs.getString("title") + " - " +
                        rs.getString("author") + " - " +
                        rs.getInt("quantity")
                );
            }

            if (!found) {
                System.out.println("No books available.");
            }
        }
    }

    // ================= UPDATE BOOK =================
    public void updateBook(int id, int qty) throws Exception {

        String sql = "UPDATE books SET quantity=? WHERE id=? AND is_deleted=0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Book updated.");
            else
                System.out.println("Book not found.");
        }
    }

    // ================= SOFT DELETE =================
    public void deleteBook(int id) throws Exception {

        String sql = "UPDATE books SET is_deleted = 1 WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Book deleted successfully.");
            else
                System.out.println("Book not found.");
        }
    }

    // ================= BORROW BOOK =================
    public void borrowBook(int userId, int bookId) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE books SET quantity = quantity - 1 " +
                    "WHERE id=? AND quantity > 0 AND is_deleted = 0"
            );

            ps.setInt(1, bookId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                System.out.println("Book not available.");
                con.rollback();
                return;
            }

            PreparedStatement tx = con.prepareStatement(
                    "INSERT INTO transactions (user_id, book_id, type) VALUES (?, ?, 'BORROW')"
            );

            tx.setInt(1, userId);
            tx.setInt(2, bookId);
            tx.executeUpdate();

            con.commit();
            System.out.println("Borrow successful.");

        } catch (Exception e) {
            con.rollback();
            System.out.println("Borrow failed: " + e.getMessage());
        } finally {
            con.close();
        }
    }

    // ================= RETURN BOOK =================
    public void returnBook(int userId, int bookId) throws Exception {

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);

        try {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE books SET quantity = quantity + 1 WHERE id=? AND is_deleted=0"
            );

            ps.setInt(1, bookId);
            ps.executeUpdate();

            PreparedStatement tx = con.prepareStatement(
                    "INSERT INTO transactions (user_id, book_id, type) VALUES (?, ?, 'RETURN')"
            );

            tx.setInt(1, userId);
            tx.setInt(2, bookId);
            tx.executeUpdate();

            con.commit();
            System.out.println("Return successful.");

        } catch (Exception e) {
            con.rollback();
            System.out.println("Return failed: " + e.getMessage());
        } finally {
            con.close();
        }
    }

    // ================= USER BORROWED BOOKS (FIXED LOGIC) =================
    public void showUserBorrowedBooks(int userId) throws Exception {

        String sql =
                "SELECT b.id, b.title, b.author " +
                "FROM books b " +
                "JOIN ( " +
                "   SELECT book_id, MAX(id) AS last_tx " +
                "   FROM transactions " +
                "   WHERE user_id=? " +
                "   GROUP BY book_id " +
                ") t1 ON b.id = t1.book_id " +
                "JOIN transactions t ON t.id = t1.last_tx " +
                "WHERE t.type='BORROW' AND b.is_deleted = 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("Borrowed Books:");

            while (rs.next()) {
                found = true;

                System.out.println(
                        rs.getInt("id") + " - " +
                        rs.getString("title") + " - " +
                        rs.getString("author")
                );
            }

            if (!found) {
                System.out.println("No borrowed books.");
            }
        }
    }
}