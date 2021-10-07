package com.example.openbooksspring;

import com.example.openbooksspring.helpers.ValidateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {
    @Autowired
    BooksRepository booksRepository;

    @Autowired
    UsersRepository usersRepository;

    // ---------------------------------------- Home ----------------------------------------

    @GetMapping
    String home(){
        return "Home page";
    }

    // ---------------------------------------- Users ----------------------------------------

    @GetMapping("/user")
    String user(){
        return "User login/register page";
    }

    @PostMapping("/user/sign-up")
    ResponseEntity signUpUser(@RequestBody User user){
        if (!ValidateHelper.validate(user.getLogin())){
            return ResponseEntity.badRequest().body("Can't pass login validation.");
        }
        boolean canAdd = usersRepository.canAddUser(user);
        if (!canAdd) return ResponseEntity.badRequest().body("User already exists! Please, change fields.");
        boolean isRegistered = usersRepository.addUser(user);
        if (!isRegistered) return ResponseEntity.badRequest().body("Something wrong with adding.");
        return new ResponseEntity("User created!", HttpStatus.CREATED);
    }

    @GetMapping("/user/getUsers")
    ResponseEntity getUsers(){
        return ResponseEntity.ok(usersRepository.getUsers());
    }

    // ---------------------------------------- Books ----------------------------------------

    @GetMapping("/books")
    ResponseEntity getBooks(){
        return ResponseEntity.ok(booksRepository.getBooks());
    }

    @PostMapping("/books/add")
    ResponseEntity addBook(@RequestBody Book book){
        boolean hasAuthor = usersRepository.hasAuthor(book.getAuthorLogin());
        if (!hasAuthor) return ResponseEntity.badRequest().body("There is no author with this login.");
        boolean isAdd = booksRepository.addBook(book);
        if (!isAdd) return ResponseEntity.badRequest().body("Can't add the book.");
        return ResponseEntity.ok("Book in a new collection!");
    }

    @PostMapping("/books/remove")
    ResponseEntity removeBook(@RequestBody Book book){
        boolean isRemoved = booksRepository.removeBook(book);
        if (!isRemoved) return ResponseEntity.badRequest().body("Can't remove this book.");
        return ResponseEntity.ok("Book is deleted!");
    }

    @PostMapping("/books/getBooksOf")
    ResponseEntity getBooksOf(@RequestParam String authorLogin){
        boolean hasAuthor = usersRepository.hasAuthor(authorLogin);
        if (!hasAuthor) return ResponseEntity.badRequest().body("There is no author with this login.");
        return ResponseEntity.ok(booksRepository.getBooksOfUser(authorLogin));
    }
}
