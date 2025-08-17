// AuthorRepository.java
// AuthorRepository.java
package com.bere.literalura.literalura.repository;

import com.bere.literalura.literalura.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer yearStart, Integer yearEnd);

    // Nuevo método para buscar autor por nombre
    Optional<Author> findByName(String name);
}
