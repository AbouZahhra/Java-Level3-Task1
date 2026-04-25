import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        User user = new User();
        Book book = new Book();

        System.out.println("Library Management System");

        System.out.println("1- Register");
        System.out.println("2- Login");

        int choice = sc.nextInt();
        sc.nextLine();

        String role = null;
        int userId = -1;

        // ===== REGISTER =====
        if (choice == 1) {

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.println("Choose Role:");
            System.out.println("1- ADMIN");
            System.out.println("2- USER");

            int roleChoice = sc.nextInt();
            sc.nextLine();

            role = (roleChoice == 1) ? "ADMIN" : "USER";

            try {
                user.register(username, role);
            } catch (Exception e) {
                System.out.println("Registration error: " + e.getMessage());
            }

            return;
        }

        // ===== LOGIN =====
        else if (choice == 2) {

            System.out.print("Username: ");
            String username = sc.nextLine();

            try {
                userId = user.getUserId(username);
                role = user.login(username);

                if (role == null) {
                    System.out.println("User not found!");
                    return;
                }

            } catch (Exception e) {
                System.out.println("Login error: " + e.getMessage());
                return;
            }
        }

        // ===== MENU =====
        while (true) {

            System.out.println("\nMENU");

            if (role.equals("ADMIN")) {
                System.out.println("1- Add Book");
                System.out.println("2- Show Books");
                System.out.println("3- Update Book");
                System.out.println("4- Delete Book");
                System.out.println("0- Exit");
            } else {
                System.out.println("1- Show Books");
                System.out.println("2- Borrow Book");
                System.out.println("3- Return Book");
                System.out.println("4- My Borrowed Books");
                System.out.println("0- Exit");
            }

            System.out.print("Choose option: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input");
                sc.next();
                continue;
            }

            int opt = sc.nextInt();

            // ===== ADMIN =====
            if (role.equals("ADMIN")) {

                if (opt == 1) {

                    sc.nextLine();

                    System.out.print("Title: ");
                    String title = sc.nextLine();

                    System.out.print("Author: ");
                    String author = sc.nextLine();

                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();

                    try {
                        book.addBook(title, author, qty);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 2) {
                    try {
                        book.showBooks();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 3) {

                    System.out.print("Book ID: ");
                    int id = sc.nextInt();

                    System.out.print("Quantity: ");
                    int qty = sc.nextInt();

                    try {
                        book.updateBook(id, qty);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 4) {

                    System.out.print("Book ID: ");
                    int id = sc.nextInt();

                    try {
                        book.deleteBook(id);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 5) {

                    System.out.print("User ID: ");
                    int uid = sc.nextInt();

                    System.out.print("Book ID: ");
                    int bid = sc.nextInt();

                    try {
                        book.borrowBook(uid, bid);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 6) {

                    System.out.print("User ID: ");
                    int uid = sc.nextInt();

                    System.out.print("Book ID: ");
                    int bid = sc.nextInt();

                    try {
                        book.returnBook(uid, bid);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else {
                    System.out.println("Goodbye Admin");
                    break;
                }
            }

            // ===== USER =====
            else {

                if (opt == 1) {

                    try {
                        book.showBooks();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 2) {

                    System.out.print("Book ID: ");
                    int bid = sc.nextInt();

                    try {
                        book.borrowBook(userId, bid);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 3) {

                    System.out.print("Book ID: ");
                    int bid = sc.nextInt();

                    try {
                        book.returnBook(userId, bid);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else if (opt == 4) {

                    try {
                        book.showUserBorrowedBooks(userId);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                else {
                    System.out.println("Goodbye ");
                    break;
                }
            }
        }

        sc.close();
    }
}