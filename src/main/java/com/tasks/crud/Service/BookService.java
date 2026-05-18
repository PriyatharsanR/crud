package com.tasks.crud.Service;

import com.tasks.crud.DTO.AddBookReq;
import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.DTO.BookReq;
import com.tasks.crud.DTO.BookRes;
import com.tasks.crud.Entity.Books;
import com.tasks.crud.Exception.ResourceNotFoundException;
import com.tasks.crud.Repository.BookRepo;
import com.tasks.crud.Util.ResponseCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookService {

    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public BookRes saveDetails(AddBookReq bookReq) {
        log.info("Saving new book: title='{}', author='{}'", bookReq.title(), bookReq.author());
        Books book = new Books();
        book.setAuthor(bookReq.author());
        book.setTitle(bookReq.title());
        Books saved = bookRepo.save(book);

        // Fixed bug: Setting the newly generated database ID on response
        return new BookRes(saved.getId(), saved.getTitle(), saved.getAuthor());
    }

    public List<BookRes> getBooks() {
        log.info("Fetching all books");
        List<Books> booksList = bookRepo.findAll();
        List<BookRes> bookResList = new ArrayList<>();

        for (Books books : booksList) {
            bookResList.add(new BookRes(books.getId(), books.getTitle(), books.getAuthor()));
        }

        return bookResList;
    }

    // Update a Book
    public ApiRes<BookRes> updateBook(BookReq bookReq) {
        log.info("Updating book with ID: {}", bookReq.id());
        Books book = bookRepo.findById(bookReq.id())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookReq.id()));

        book.setTitle(bookReq.title());
        book.setAuthor(bookReq.author());
        Books saved = bookRepo.save(book);

        BookRes bookRes = new BookRes(saved.getId(), saved.getTitle(), saved.getAuthor());

        return ApiRes.<BookRes>builder()
                .code(ResponseCodeUtil.SUCCESS_CODE)
                .title("Success")
                .message("Book updated successfully")
                .data(bookRes)
                .build();
    }

    // Delete Book
    public ApiRes<Void> deleteBook(int id) {
        log.info("Deleting book with ID: {}", id);
        Books book = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        bookRepo.delete(book);

        return ApiRes.<Void>builder()
                .code(ResponseCodeUtil.SUCCESS_CODE)
                .title("SUCCESS")
                .message("Delete Book Successfully")
                .build();
    }

    // Find a book by ID
    public ApiRes<BookRes> findBook(int id) {
        log.info("Fetching book with ID: {}", id);
        Books book = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        BookRes bookRes = new BookRes(book.getId(), book.getTitle(), book.getAuthor());

        return ApiRes.<BookRes>builder()
                .code(ResponseCodeUtil.SUCCESS_CODE)
                .title("Success")
                // Fixed bug: Changed from "Book Updated Successfully" to "Book retrieved successfully"
                .message("Book retrieved successfully")
                .data(bookRes)
                .build();
    }
}
