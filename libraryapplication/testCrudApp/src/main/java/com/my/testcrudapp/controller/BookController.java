package com.my.testcrudapp.controller;

import com.my.testcrudapp.model.Book;
import com.my.testcrudapp.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@Tag(name = "Книги", description = "Взаимодействие с книгами")
public class BookController {

    private final BookService bookService;
    private final RestTemplate restTemplate;


    @Autowired
    public BookController(BookService bookService, RestTemplate restTemplate) {
        this.bookService = bookService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/books")
    @Operation(summary = "Получение всех книг сохраненных в бд")
    public String findAll(Model model){
        List<Book> books = bookService.findAll();
        Book book = new Book();
        model.addAttribute("books", books);
        model.addAttribute("book", book);
        return "book-list";
    }

    @GetMapping("/book-create")
    @Operation(summary = "Создание книги", description = "Позволяет создать новую книгу")
    public String createBookForm(Book book){
        return "book-create";
    }

    @Operation(summary = "Добавление созданной книги в бд")
    @PostMapping("/book-create")
    public String createBook(Book book){
        bookService.saveBook(book);
        restTemplate.postForObject("http://libraryservice:8081/library/books", book.getId(), Book.class);
        return "redirect:books";
    }

    @GetMapping("/book-delete/{id}")
    public String deleteBookForm(@PathVariable("id") Long id){
        restTemplate.delete("http://libraryservice:8081/library/delete/" + id);
        bookService.deleteById(id);
        return "redirect:http://localhost:8080/books";
    }



    @Operation(summary = "Обновление данных о книге", description = "Позволяет изменить данные книги")
    @GetMapping("/book-update/{id}")
    public String updateBookForm(@PathVariable("id") Long id, Model model){
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "book-update";
    }

    @Operation(summary = "Сохранение изменений данных о книге")
    @PostMapping("/book-update")
    public String updateBook(Book book){
        bookService.updateBook(book);
        return "redirect:books";
    }


    @Operation(summary = "Поиск книги по ID", description = "Позволяет найти книгу введя её ID")
    @PostMapping("/find-book-id")
    public String findBookIdForm(Long id, Model model){
        Book book = bookService.findById(id);
        if(book == null){
            return "not-found";
        }
        else {
            model.addAttribute("book", book);
            return "book";
        }

    }

    @Operation(summary = "Поиск книги по ISBN", description = "Позволяет найти книгу введя её ISBN")
    @PostMapping("/find-book-isbn")
    public String findBookIsbnForm(String isbn, Model model){
        List<Book> books = bookService.findByIsbn(isbn);
        if(books.size()==0){
            return "not-found";
        }
        else {
            model.addAttribute("books", books);
            return "findBookIsbn";
        }
    }

    @GetMapping("/book-info/{id}")
    @Operation(summary = "Получение информации о конкретно выбранной книге")
    public String bookInfoForm(@PathVariable("id") Long id, Model model){
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        boolean status = restTemplate.postForObject("http://libraryservice:8081/library/status", book.getId(), boolean.class);
        model.addAttribute("status", status);
        return "book";
    }


    @GetMapping("/books-free")
    @Operation(summary = "Получение списка всех свободных книг")
    public String freeBookList(Model model){
        List<Book> books = bookService.getFreeBookList();
        model.addAttribute("books", books);
        return "free-book-list";
    }



}
