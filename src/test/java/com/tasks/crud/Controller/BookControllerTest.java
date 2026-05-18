package com.tasks.crud.Controller;

import tools.jackson.databind.ObjectMapper;
import com.tasks.crud.DTO.AddBookReq;
import com.tasks.crud.DTO.ApiRes;
import com.tasks.crud.DTO.BookReq;
import com.tasks.crud.DTO.BookRes;
import com.tasks.crud.Exception.ResourceNotFoundException;
import com.tasks.crud.Service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBook_WhenPayloadValid_ShouldReturn201Created() throws Exception {
        AddBookReq req = new AddBookReq("Design Patterns", "Gang of Four");
        BookRes res = new BookRes(1, "Design Patterns", "Gang of Four");
        when(bookService.saveDetails(any(AddBookReq.class))).thenReturn(res);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Design Patterns"))
                .andExpect(jsonPath("$.author").value("Gang of Four"));
    }

    @Test
    void addBook_WhenPayloadBlank_ShouldReturn400BadRequest() throws Exception {
        AddBookReq req = new AddBookReq(" ", ""); // Blank values

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("3000"))
                .andExpect(jsonPath("$.title").value("VALIDATION FAILED"))
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.author").exists());
    }

    @Test
    void getBooks_ShouldReturnList() throws Exception {
        when(bookService.getBooks()).thenReturn(Arrays.asList(
                new BookRes(1, "Book 1", "Author 1"),
                new BookRes(2, "Book 2", "Author 2")
        ));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturn200() throws Exception {
        BookRes bookRes = new BookRes(1, "Book 1", "Author 1");
        ApiRes<BookRes> apiRes = ApiRes.<BookRes>builder()
                .code("0000")
                .title("Success")
                .message("Book retrieved successfully")
                .data(bookRes)
                .build();
        when(bookService.findBook(1)).thenReturn(apiRes);

        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.title").value("Book 1"));
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldReturn404() throws Exception {
        when(bookService.findBook(99)).thenThrow(new ResourceNotFoundException("Book not found with ID: 99"));

        mockMvc.perform(get("/api/v1/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("3001"))
                .andExpect(jsonPath("$.message").value("Book not found with ID: 99"));
    }

    @Test
    void updateBook_WhenPayloadValid_ShouldReturn200() throws Exception {
        AddBookReq req = new AddBookReq("Book Updated", "Author Updated");
        BookRes bookRes = new BookRes(1, "Book Updated", "Author Updated");
        ApiRes<BookRes> apiRes = ApiRes.<BookRes>builder()
                .code("0000")
                .title("Success")
                .message("Book updated successfully")
                .data(bookRes)
                .build();
        when(bookService.updateBook(any(BookReq.class))).thenReturn(apiRes);

        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.title").value("Book Updated"));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturn200() throws Exception {
        ApiRes<Void> apiRes = ApiRes.<Void>builder()
                .code("0000")
                .title("SUCCESS")
                .message("Delete Book Successfully")
                .build();
        when(bookService.deleteBook(1)).thenReturn(apiRes);

        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.message").value("Delete Book Successfully"));
    }
}
