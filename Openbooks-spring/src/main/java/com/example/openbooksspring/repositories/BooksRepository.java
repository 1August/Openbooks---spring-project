package com.example.openbooksspring.repositories;

import com.example.openbooksspring.classes.Book;
import com.example.openbooksspring.classes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BooksRepository {
    private List<Book> books = new ArrayList<>();
    @Autowired
    UsersRepository useRepository;

    public boolean addBook(Book book){
        for(Book bookEl : books){
            if (bookEl.getId() == book.getId() ||
                    (bookEl.getAuthorLogin().equals(book.getAuthorLogin())
                     && bookEl.getName().equals(book.getName())
                     && bookEl.getYear() == book.getYear()))
            {
                return false;
            }
        }
        return books.add(book);
    }
    public boolean removeBook(Book book) {
        return books.remove(book);
    }

    public List<Book> getBooksOfUser(String authorLogin){
        List<Book> userBooks = new ArrayList<>();
        for (Book book : books)
            if (book.getAuthorLogin().equals(authorLogin)) userBooks.add(book);
        return userBooks;
    }

    Book getBookByName(String bookName){
        for(Book book : books)
            if (book.getName().equals(bookName)) return book;
        return null;
    }

    public List<Book> getBooks() {
        return books;
    }
    public List<User> getUsers() {
        return useRepository.getUsers();
    }
}
