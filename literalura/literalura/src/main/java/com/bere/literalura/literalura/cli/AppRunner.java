package com.bere.literalura.literalura.cli;

import com.bere.literalura.literalura.entity.Author;
import com.bere.literalura.literalura.entity.Book;
import com.bere.literalura.literalura.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class AppRunner implements CommandLineRunner {

    private final LibraryService libraryService;

    public AppRunner(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n--- LiterAlura Menu ---");
            System.out.println("1. Search book by title");
            System.out.println("2. List all books");
            System.out.println("3. List books by language");
            System.out.println("4. List all authors");
            System.out.println("5. List authors alive in a year");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1 -> {
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    Optional<Book> book = libraryService.fetchBookByTitle(title);
                    book.ifPresentOrElse(
                            b -> System.out.println("Saved book: " + b.getTitle() + " - Author: " + b.getAuthor().getName()),
                            () -> System.out.println("Book not found.")
                    );
                }
                case 2 -> {
                    List<Book> books = libraryService.getAllBooks();
                    System.out.println("All books:");
                    books.forEach(b -> System.out.println(b.getTitle() + " - " + b.getAuthor().getName()));
                }
                case 3 -> {
                    System.out.print("Enter language: ");
                    String language = scanner.nextLine();
                    List<Book> booksByLanguage = libraryService.getBooksByLanguage(language);
                    System.out.println("Books in " + language + ":");
                    booksByLanguage.forEach(b -> System.out.println(b.getTitle() + " - " + b.getAuthor().getName()));
                }
                case 4 -> {
                    List<Author> authors = libraryService.getAllAuthors();
                    System.out.println("All authors:");
                    authors.forEach(a -> System.out.println(a.getName() + " (" + a.getBirthYear() + " - " + a.getDeathYear() + ")"));
                }
                case 5 -> {
                    System.out.print("Enter year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    List<Author> authorsAlive = libraryService.getAuthorsAliveInYear(year);
                    System.out.println("Authors alive in " + year + ":");
                    authorsAlive.forEach(a -> System.out.println(a.getName()));
                }
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (option != 0);

        scanner.close();
    }
}
