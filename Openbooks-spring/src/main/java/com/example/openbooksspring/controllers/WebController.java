package com.example.openbooksspring.controllers;

import com.example.openbooksspring.classes.Book;
import com.example.openbooksspring.classes.User;
import com.example.openbooksspring.helpers.TokenHelper;
import com.example.openbooksspring.helpers.ValidateHelper;
import com.example.openbooksspring.repositories.BooksRepository;
import com.example.openbooksspring.repositories.TokenRepository;
import com.example.openbooksspring.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebController {
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TokenRepository tokenRepository;

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

    @PostMapping("/user/login")
    ResponseEntity login(@RequestBody User userData) {
        User user = usersRepository.login(userData.getLogin(), userData.getPassword());
        if (user == null) return ResponseEntity.badRequest().body("User didn't found.");
        String token = TokenHelper.getToken(user.getLogin());
        boolean hasInBlackList = tokenRepository.hasInBlackList(token);
        if (hasInBlackList) tokenRepository.removeTokenFromBlackList(token);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/user/get")
    ResponseEntity getUser(HttpServletRequest request){
        String token = request.getHeader("token");
        if (tokenRepository.getBlackListTokens().contains(token)) {
            return ResponseEntity.badRequest().body("Token in black list!");
        }
        User user = usersRepository.getUserByToken(token);
        if (user != null) return ResponseEntity.ok(user);
        return ResponseEntity.badRequest().body("Bad request");
    }

    @PostMapping("/user/update")
    ResponseEntity update(@RequestParam(required = false) String login, @RequestParam(required = false) String password, HttpServletRequest request){
        String token = request.getHeader("token");
        User user = usersRepository.getUserByToken(token);
        boolean areDiff = false;
        if (user.getLogin().equals(login)) areDiff = true;
        if (login != null && !ValidateHelper.validate(login)) return ResponseEntity.badRequest().body("Validation error!");
        if (login == null) login = user.getLogin();
        if (password == null) password = user.getPassword();
        boolean isUpdated = usersRepository.updateUser(login, password, token);
        if (!isUpdated) return ResponseEntity.badRequest().body("Can't update");
        if (areDiff){
            tokenRepository.addTokenToBlackList(token);
            token = TokenHelper.getToken(login);
        }
        tokenRepository.addTokenToBlackList(token);
        token = TokenHelper.getToken(login);
        return ResponseEntity.ok(user + "/n" + token);
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
