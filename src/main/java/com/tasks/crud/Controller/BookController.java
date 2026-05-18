package com.tasks.crud.Controller;

import com.tasks.crud.DTO.AddBookReq;
import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.DTO.BookReq;
import com.tasks.crud.DTO.BookRes;
import com.tasks.crud.Service.BookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookRes addBook(@Valid @RequestBody AddBookReq bookReq) {
        log.info("REST request to add book: {}", bookReq);
        return bookService.saveDetails(bookReq);
    }

    @GetMapping
    public List<BookRes> getBooks() {
        log.info("REST request to get all books");
        return bookService.getBooks();
    }

    @PutMapping("/{id}")
    public ApiRes<BookRes> update(@PathVariable int id, @Valid @RequestBody AddBookReq bookReq) {
        log.info("REST request to update book with ID {}: {}", id, bookReq);
        BookReq fullReq = new BookReq(id, bookReq.title(), bookReq.author());
        return bookService.updateBook(fullReq);
    }

    @DeleteMapping("/{id}")
    public ApiRes<Void> delete(@PathVariable int id) {
        log.info("REST request to delete book with ID: {}", id);
        return bookService.deleteBook(id);
    }

    @GetMapping("/{id}")
    public ApiRes<BookRes> getBookById(@PathVariable int id) {
        log.info("REST request to get book with ID: {}", id);
        return bookService.findBook(id);
    }
}
