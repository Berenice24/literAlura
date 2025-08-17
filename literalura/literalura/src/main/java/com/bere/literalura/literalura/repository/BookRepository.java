// BookRepository.java
package com.bere.literalura.literalura.repository;

import com.bere.literalura.literalura.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguage(String language);

    // Nuevo método para buscar libro por título
    Optional<Book> findByTitle(String title);
}
