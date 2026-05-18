package com.tasks.crud.Service;

import com.tasks.crud.DTO.AddBookReq;
import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.DTO.BookReq;
import com.tasks.crud.DTO.BookRes;
import com.tasks.crud.Entity.Books;
import com.tasks.crud.Exception.ResourceNotFoundException;
import com.tasks.crud.Repository.BookRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    private Books testBook;

    @BeforeEach
    void setUp() {
        testBook = new Books(1, "Clean Code", "Robert C. Martin");
    }

    @Test
    void saveDetails_ShouldSaveAndReturnBookRes() {
        AddBookReq req = new AddBookReq("Clean Code", "Robert C. Martin");
        when(bookRepo.save(any(Books.class))).thenReturn(testBook);

        BookRes res = bookService.saveDetails(req);

        assertNotNull(res);
        assertEquals(1, res.id());
        assertEquals("Clean Code", res.title());
        assertEquals("Robert C. Martin", res.author());
        verify(bookRepo, times(1)).save(any(Books.class));
    }

    @Test
    void getBooks_ShouldReturnAllBooks() {
        List<Books> booksList = Arrays.asList(
                testBook,
                new Books(2, "Refactoring", "Martin Fowler")
        );
        when(bookRepo.findAll()).thenReturn(booksList);

        List<BookRes> result = bookService.getBooks();

        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).title());
        assertEquals("Refactoring", result.get(1).title());
    }

    @Test
    void updateBook_WhenBookExists_ShouldUpdateAndReturnSuccess() {
        BookReq req = new BookReq(1, "Clean Code Updated", "Robert C. Martin");
        when(bookRepo.findById(1)).thenReturn(Optional.of(testBook));
        when(bookRepo.save(any(Books.class))).thenReturn(new Books(1, "Clean Code Updated", "Robert C. Martin"));

        ApiRes<BookRes> response = bookService.updateBook(req);

        assertEquals("0000", response.code());
        assertEquals("Clean Code Updated", response.data().title());
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldThrowResourceNotFoundException() {
        BookReq req = new BookReq(99, "Title", "Author");
        when(bookRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(req));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteAndReturnSuccess() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepo).delete(testBook);

        ApiRes<Void> response = bookService.deleteBook(1);

        assertEquals("0000", response.code());
        verify(bookRepo, times(1)).delete(testBook);
    }

    @Test
    void findBook_WhenBookExists_ShouldReturnBook() {
        when(bookRepo.findById(1)).thenReturn(Optional.of(testBook));

        ApiRes<BookRes> response = bookService.findBook(1);

        assertEquals("0000", response.code());
        assertEquals("Clean Code", response.data().title());
        assertEquals("Book retrieved successfully", response.message());
    }
}
