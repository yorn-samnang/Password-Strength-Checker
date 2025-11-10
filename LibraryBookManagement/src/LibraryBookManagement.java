import java.io.*;
import java.util.Scanner;

public class LibraryBookManagement {
    // Maximum number of books allowed
    private static final int MAX_BOOKS = 100;
    private static String[] books = new String[MAX_BOOKS];
    private static int count = 0;
    private static final String FILE_NAME = "books.txt";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadBooksFromFile();
        int choice;

        do {
            System.out.println("\n===== Library Book Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. List All Books");
            System.out.println("4. Delete Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            // Handle invalid input
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Please enter a number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    searchBook();
                    break;
                case 3:
                    listBooks();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    saveBooksToFile();
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        } while (choice != 5);
    }

    // Feature 1: Add book
    private static void addBook() {
        if (count >= MAX_BOOKS) {
            System.out.println("Library is full! Cannot add more books.");
            return;
        }

        System.out.print("Enter book title: ");
        String title = sc.nextLine();

        if (title.trim().isEmpty()) {
            System.out.println("Book title cannot be empty!");
            return;
        }

        books[count] = title.trim();
        count++;
        System.out.println("Book added successfully!");
    }

    // Feature 2: Search book using method
    private static void searchBook() {
        System.out.print("Enter book title to search: ");
        String searchTitle = sc.nextLine().trim();
        int index = findBookIndex(searchTitle);

        if (index != -1) {
            System.out.println("✅ Book found: \"" + books[index] + "\" (Position: " + (index + 1) + ")");
        } else {
            System.out.println("❌ Book not found!");
        }
    }

    // Feature 3: Method to find book index
    private static int findBookIndex(String title) {
        for (int i = 0; i < count; i++) {
            if (books[i].equalsIgnoreCase(title)) {
                return i;
            }
        }
        return -1;
    }

    // Feature 4: List all books
    private static void listBooks() {
        if (count == 0) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("\n📚 List of Books:");
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + ". " + books[i]);
            }
        }
    }

    // Feature 5: Delete book by shifting array
    private static void deleteBook() {
        System.out.print("Enter book title to delete: ");
        String deleteTitle = sc.nextLine().trim();
        int index = findBookIndex(deleteTitle);

        if (index == -1) {
            System.out.println("Book not found!");
            return;
        }

        for (int i = index; i < count - 1; i++) {
            books[i] = books[i + 1];
        }
        count--;
        System.out.println("Book deleted successfully!");
    }

    // Read books from file (Text I/O)
    private static void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null && count < MAX_BOOKS) {
                books[count++] = line;
            }
            System.out.println("Books loaded successfully from file!");
        } catch (FileNotFoundException e) {
            System.out.println("No previous record found (new library).");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save books to file (Text I/O)
    private static void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < count; i++) {
                writer.write(books[i]);
                writer.newLine();
            }
            System.out.println("Books saved successfully to file!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
