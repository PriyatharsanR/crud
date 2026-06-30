package com.tasks.crud.Service;

import com.tasks.crud.DTO.AddBookReq;
import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.DTO.BookReq;
import com.tasks.crud.DTO.BookRes;
import com.tasks.crud.Entity.Books;
import com.tasks.crud.Repository.BookRepo;
import com.tasks.crud.Util.ResponseCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepo bookRepo;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldSaveBookSuccessfully() {
        // Arrange
        AddBookReq request = new AddBookReq("திருக்குறள்", "திருவள்ளுவர்");

        Books savedBook = new Books(1 , "திருக்குறள்", "திருவள்ளுவர்");

        when(bookRepo.save(any(Books.class))).thenReturn(savedBook);

        // Act
        BookRes response = bookService.saveDetails(request);


        // Assert
        assertNotNull(response);
        assertEquals(1 , response.id());
        assertEquals("திருக்குறள்", response.title());
        assertEquals("திருவள்ளுவர்", response.author());

        verify(bookRepo).save(any(Books.class));
    }

    @Test
    void shouldGetBooksSuccessfully() {

        //Arrange
        Books book1 = new Books(1, "Clean Code", "Robert Martin");
        Books book2 = new Books(2, "Spring in Action", "Craig Walls");

        List<Books> savedBooks = List.of(book1, book2);

        when(bookRepo.findAll()).thenReturn(savedBooks);


        // ACT
        List<BookRes> response = bookService.getBooks();


        //Assert
        assertEquals(2, response.size());

        assertEquals(1, response.get(0).id());
        assertEquals("Clean Code", response.get(0).title());
        assertEquals("Robert Martin", response.get(0).author());

        assertEquals(2, response.get(1).id());
        assertEquals("Spring in Action", response.get(1).title());
        assertEquals("Craig Walls", response.get(1).author());

        verify(bookRepo).findAll();
    }

    @Test
    void shouldFindBookByIdSuccessfully() {
        Books book2 = new Books(2, "Clean Code", "Robert Martin");

        when(bookRepo.findById(2)).thenReturn(Optional.of(book2));

        ApiRes<BookRes> response = bookService.findBook(2);

        BookRes result = response.data();

        assertNotNull(response);
        assertNotNull(response.data());

        assertEquals(ResponseCodeUtil.SUCCESS_CODE, response.code());
        assertEquals("Success", response.title());
        assertEquals("Book retrieved successfully", response.message());
        assertEquals(2 , result.id());
        assertEquals("Clean Code", result.title());
        assertEquals("Robert Martin", result.author());

        verify(bookRepo).findById(2);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        BookReq book1 = new BookReq(1,"Clean Code", "Robert Martin");

        Books existingBook = new Books(1, "Hello World", "Piri");

        Books updateBook = new Books(1, "Clean Code", "Robert Martin");

        when(bookRepo.findById(1)).thenReturn(Optional.of(existingBook));
        when(bookRepo.save(any(Books.class))).thenReturn(updateBook);

        ApiRes<BookRes> response = bookService.updateBook(book1);

        assertNotNull(response);
        assertNotNull(response.data());

        BookRes result = response.data();

        assertEquals(1, result.id());
        assertEquals("Clean Code", result.title());
        assertEquals("Robert Martin", result.author());

        assertEquals(ResponseCodeUtil.SUCCESS_CODE, response.code());
        assertEquals("Success", response.title());
        assertEquals("Book updated successfully", response.message());

        verify(bookRepo).findById(1);
        verify(bookRepo).save(any(Books.class));

    }
}