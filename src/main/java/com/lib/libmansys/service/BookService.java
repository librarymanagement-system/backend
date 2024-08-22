package com.lib.libmansys.service;

import com.lib.libmansys.dto.CreateBookRequest;
import com.lib.libmansys.entity.Author;
import com.lib.libmansys.entity.Book;
import com.lib.libmansys.entity.Enum.BookStatus;
import com.lib.libmansys.entity.Genre;
import com.lib.libmansys.entity.Publisher;
import com.lib.libmansys.repository.AuthorRepository;
import com.lib.libmansys.repository.BookRepository;
import com.lib.libmansys.repository.GenreRepository;
import com.lib.libmansys.repository.PublisherRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
    }

    // Add a new book
    public Book addBook(CreateBookRequest createBookRequest) {
        Book book = new Book();
        book.setTitle(createBookRequest.getTitle());

        List<Author> authors = authorRepository.findAllById(createBookRequest.getAuthorIds());
        book.setAuthors(authors);

        List<Publisher> publishers = publisherRepository.findAllById(createBookRequest.getPublisherIds());
        book.setPublishers(publishers);

        List<Genre> genres = genreRepository.findAllById(createBookRequest.getGenreIds());
        book.setGenres(genres);

        book.setStatus(BookStatus.AVAILABLE);

        return bookRepository.save(book);
    }

    // Update an existing book
    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthors(updatedBook.getAuthors());
                    book.setPublishers(updatedBook.getPublishers());
                    book.setGenres(updatedBook.getGenres());
                    book.setStatus(updatedBook.getStatus());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }


    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }
    public Book findBooksById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }


    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }


    public List<Book> findBooksByFilters(String author, String genre, String publisher) {

        if (author != null && genre == null && publisher == null) {
            return bookRepository.findByAuthors_FirstName(author);
        } else if (author == null && genre != null && publisher == null) {
            return bookRepository.findByGenresName(genre);
        } else if (author == null && genre == null && publisher != null) {
            return bookRepository.findByPublishersName(publisher);
        } else {
            return findAllBooks(); // Fallback to returning all books
        }
    }


    public List<Book> findBooksByStatus(BookStatus status) {
        return bookRepository.findByStatus(status);
    }
    public void exportBooks(HttpServletResponse response) throws IOException {
        List<Book> books = bookRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Title");
        headerRow.createCell(2).setCellValue("Status");

        int rowNum = 1;
        for (Book book : books) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getTitle());
            row.createCell(2).setCellValue(book.getStatus().toString());
        }

        for(int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}