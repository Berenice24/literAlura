package com.bere.literalura.literalura.service;

import com.bere.literalura.literalura.dto.GutendexBookDto;
import com.bere.literalura.literalura.dto.GutendexAuthorDto;
import com.bere.literalura.literalura.entity.Author;
import com.bere.literalura.literalura.entity.Book;
import com.bere.literalura.literalura.repository.AuthorRepository;
import com.bere.literalura.literalura.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LibraryService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Optional<Book> fetchBookByTitle(String title) {
        String url = "https://gutendex.com/books/?search=" + title.replace(" ", "%20");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.path("results");

            if (results.isArray() && results.size() > 0) {
                JsonNode firstBook = results.get(0);
                GutendexBookDto dto = objectMapper.treeToValue(firstBook, GutendexBookDto.class);

                // Solo primer autor y primer idioma
                GutendexAuthorDto apiAuthor = dto.getAuthors().get(0);

                // Verificar si el autor ya existe
                Optional<Author> existingAuthor = authorRepository
                        .findAll()
                        .stream()
                        .filter(a -> a.getName().equalsIgnoreCase(apiAuthor.getName()))
                        .findFirst();

                Author author;
                if (existingAuthor.isPresent()) {
                    author = existingAuthor.get();
                } else {
                    author = new Author();
                    author.setName(apiAuthor.getName());
                    author.setBirthYear(apiAuthor.getBirthYear());
                    author.setDeathYear(apiAuthor.getDeathYear());
                    authorRepository.save(author);
                }

                // Verificar si el libro ya existe (mismo título y autor)
                Optional<Book> existingBook = bookRepository
                        .findAll()
                        .stream()
                        .filter(b -> b.getTitle().equalsIgnoreCase(dto.getTitle()) && b.getAuthor().equals(author))
                        .findFirst();

                if (existingBook.isPresent()) {
                    return existingBook;
                }

                Book book = new Book();
                book.setTitle(dto.getTitle());
                book.setLanguage(dto.getLanguages().get(0));
                book.setDownloadCount(dto.getDownloadCount());
                book.setAuthor(author);
                bookRepository.save(book);

                return Optional.of(book);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> getAuthorsAliveInYear(int year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);
    }
}
