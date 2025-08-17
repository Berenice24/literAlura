package com.bere.literalura.literalura;

import com.bere.literalura.literalura.entity.Author;
import com.bere.literalura.literalura.entity.Book;
import com.bere.literalura.literalura.service.LibraryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final LibraryService libraryService;

	public LiteraluraApplication(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		int option;

		do {
			System.out.println("\n--- Menú LiterAlura ---");
			System.out.println("1. Buscar libro por título");
			System.out.println("2. Listar todos los libros");
			System.out.println("3. Listar libros por idioma");
			System.out.println("4. Listar todos los autores");
			System.out.println("5. Listar autores vivos en un año");
			System.out.println("0. Salir");
			System.out.print("Seleccione una opción: ");

			option = scanner.nextInt();
			scanner.nextLine(); // limpiar buffer

			switch (option) {
				case 1:
					System.out.print("Ingrese título del libro: ");
					String title = scanner.nextLine();
					Optional<Book> book = libraryService.fetchBookByTitle(title);
					book.ifPresentOrElse(
							b -> System.out.println("Libro guardado: " + b.getTitle() + " - Autor: " + b.getAuthor().getName()),
							() -> System.out.println("No se encontró el libro.")
					);
					break;
				case 2:
					List<Book> allBooks = libraryService.getAllBooks();
					allBooks.forEach(b -> System.out.println(b.getTitle() + " | " + b.getLanguage() + " | Autor: " + b.getAuthor().getName()));
					break;
				case 3:
					System.out.print("Ingrese idioma: ");
					String lang = scanner.nextLine();
					List<Book> booksByLang = libraryService.getBooksByLanguage(lang);
					booksByLang.forEach(b -> System.out.println(b.getTitle() + " | " + b.getLanguage() + " | Autor: " + b.getAuthor().getName()));
					break;
				case 4:
					List<Author> authors = libraryService.getAllAuthors();
					authors.forEach(a -> System.out.println(a.getName() + " | " + a.getBirthYear() + " - " + a.getDeathYear()));
					break;
				case 5:
					System.out.print("Ingrese año: ");
					int year = scanner.nextInt();
					scanner.nextLine();
					List<Author> aliveAuthors = libraryService.getAuthorsAliveInYear(year);
					aliveAuthors.forEach(a -> System.out.println(a.getName() + " | " + a.getBirthYear() + " - " + a.getDeathYear()));
					break;
				case 0:
					System.out.println("Saliendo...");
					break;
				default:
					System.out.println("Opción inválida.");
			}
		} while (option != 0);

		scanner.close();
	}
}
