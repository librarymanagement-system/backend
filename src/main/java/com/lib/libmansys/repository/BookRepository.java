package com.lib.libmansys.repository;

import com.lib.libmansys.entity.Book;
import com.lib.libmansys.entity.Enum.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByGenresName(String genreName);
    List<Book> findByPublishersName(String publisherName);
    List<Book> findByStatus(BookStatus status);
    List<Book> findByAuthors_FirstName(String firstName);
//    List<Book> findByAuthors_LastName(String lastName);
}
